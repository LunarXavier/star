package org.xavier.star.service.Impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.xavier.star.common.CommonConstants;
import org.xavier.star.common.CommonErrorCode;
import org.xavier.star.common.CommonException;
import org.xavier.star.config.YmlConfig;
import org.xavier.star.dto.SessionData;
import org.xavier.star.dto.WxSession;
import org.xavier.star.entity.User;
import org.xavier.star.entity.WishValueRecord;
import org.xavier.star.mapper.UserMapper;
import org.xavier.star.service.UserService;
import org.xavier.star.service.WishValueRecordService;
import org.xavier.star.util.*;

import java.io.IOException;
import java.util.*;

import static org.xavier.star.common.CommonConstants.USER_FILE_PATH;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SessionUtils sessionUtils;

    @Autowired
    private YmlConfig ymlConfig;

    @Autowired
    private WishValueRecordService wishValueRecordService;

    public User getUserById(Long id) throws CommonException {
        User user = userMapper.selectById(id);
        if (user == null) throw new CommonException(CommonErrorCode.USER_NOT_EXIST);
        return user;
    }

    @Override
    public String uploadPortrait(MultipartFile file, Long id) throws CommonException {
        User user = userMapper.selectById(id);
        if (user == null) throw new CommonException(CommonErrorCode.USER_NOT_EXIST);
        String originalFilename = file.getOriginalFilename();
        String flag = IdUtil.fastSimpleUUID();
        String rootFilePath = USER_FILE_PATH + flag + "-" + originalFilename;
        try {
            FileUtil.writeBytes(file.getBytes(), rootFilePath);
        } catch (IOException e) {
            throw new CommonException(CommonErrorCode.READ_FILE_ERROR);
        }
        String link = CommonConstants.DOWNLOAD_PATH + rootFilePath;
        user.setPortrait(link);
        if (userMapper.updateById(user) == 0) throw new CommonException(CommonErrorCode.UPDATE_FAIL);
        return link;
    }

    /**
     * 用户登录
     * @param code
     * @return
     */
    @Override
    public SessionData login(String code) {
        //shadow test
        if (CommonConstants.SHADOW_TEST.equals(code)) {
            //设置响应头
            sessionUtils.setSessionId(CommonConstants.SHADOW_TEST);
            return new SessionData();
        }

        WxSession wxSession = Optional.ofNullable(getWxSessionByCode(code))
                .orElse(new WxSession());
        checkWxSession(wxSession);

        //根据openid查询DB
        User user = userMapper.getUserByOpenId(wxSession.getOpenId());
        //如果用户已注册，则返回用户
        if (user != null) {
            sessionUtils.setSessionId(user.getSessionId());
            return new SessionData(user);
        }
        //如果未注册，则将用户信息存入DB
        //生成sessionId
        String sessionId = sessionUtils.generateSessionId();
        User newUser = User.builder()
                .sessionId(sessionId)
                .openId(wxSession.getOpenId())
                .unionId(wxSession.getUnionId())
                .sessionKey(wxSession.getSessionKey())
                .nickname("小星星")
                .build();
        userMapper.insert(newUser);
        return new SessionData(newUser);
    }

    /**
     * 使用 code 换取 openid、session_key、unionId 等信息
     * @param code
     * @return
     */
    @Override
    public WxSession getWxSessionByCode(String code) {
        Map<String, String> requestUrlParam = new HashMap<>();
        //小程序appId
        requestUrlParam.put("appid", ymlConfig.getAppId());
        //小程序secret
        requestUrlParam.put("secret", ymlConfig.getAppSecret());
        //小程序端返回的code
        requestUrlParam.put("js_code", code);
        //默认参数：授权类型
        requestUrlParam.put("grant_type", "authorization_code");
        //发送post请求读取调用微信接口获取openid用户唯一标识
        String result = HttpUtil.get(CommonConstants.WX_SESSION_REQUEST_URL, requestUrlParam);
        return JsonUtil.toObject(result, WxSession.class);
    }

    @Override
    public void checkWxSession(WxSession wxSession) {
        if (wxSession.getErrcode() != null) {
            AssertUtil.isFalse(-1 == wxSession.getErrcode(), CommonErrorCode.WX_LOGIN_BUSY, wxSession.getErrmsg());
            AssertUtil.isFalse(40029 == wxSession.getErrcode(), CommonErrorCode.WX_LOGIN_INVALID_CODE, wxSession.getErrmsg());
            AssertUtil.isFalse(45011 == wxSession.getErrcode(), CommonErrorCode.WX_LOGIN_FREQUENCY_REFUSED, wxSession.getErrmsg());
            AssertUtil.isTrue(wxSession.getErrcode() == 0, CommonErrorCode.WX_LOGIN_UNKNOWN_ERROR, wxSession.getErrmsg());
        }
    }

    @Override
    @Transactional
    public Integer addShareTimes(Long userId) {
        User user = this.getUserById(userId);
        Integer shareTimes = user.getShareTimes();
        if(shareTimes == 0) return 0;
        shareTimes--;
        user.setShareTimes(shareTimes);
        user.setWishValue(user.getWishValue() + CommonConstants.SHARE_WISH_VALUE);
        user.setFromOther(user.getFromOther() + CommonConstants.SHARE_WISH_VALUE);
        this.updateById(user);
        //创建心愿值获取记录
        WishValueRecord wishValueRecord = WishValueRecord.builder()
                .numbers(CommonConstants.SHARE_WISH_VALUE)
                .access("转发奖励")
                .userId(userId)
                .build();
        wishValueRecordService.save(wishValueRecord);
        return shareTimes;
    }

    @Override
    @Transactional
    public Integer subscribe(Long userId) {
        User user = this.getUserById(userId);
        Integer isSubscribe = user.getIsSubscribe();
        if(isSubscribe == 1) return 1;
        user.setIsSubscribe(1);
        user.setWishValue(user.getWishValue() + CommonConstants.SUBSCRIBE_WISH_VALUE);
        user.setFromOther(user.getFromOther() + CommonConstants.SUBSCRIBE_WISH_VALUE);
        this.updateById(user);
        //创建心愿值获取记录
        WishValueRecord wishValueRecord = WishValueRecord.builder()
                .numbers(CommonConstants.SUBSCRIBE_WISH_VALUE)
                .access("关注公众号奖励")
                .userId(userId)
                .build();
        wishValueRecordService.save(wishValueRecord);
        return 1;
    }

    @Override
    @Transactional
    public Integer signIn(Long userId) {
        User user = this.getUserById(userId);
        Integer isSignIn = user.getIsSignIn();
        if(isSignIn == 1) return 1;
        user.setIsSignIn(1);
        user.setWishValue(user.getWishValue() + CommonConstants.SIGN_IN_WISH_VALUE);
        user.setFromSignIn(user.getFromOther() + CommonConstants.SIGN_IN_WISH_VALUE);
        this.updateById(user);
        //创建心愿值获取记录
        WishValueRecord wishValueRecord = WishValueRecord.builder()
                .numbers(CommonConstants.SIGN_IN_WISH_VALUE)
                .access("每日签到奖励")
                .userId(userId)
                .build();
        wishValueRecordService.save(wishValueRecord);
        return 1;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void updateIsSignIn() {
        userMapper.resetIsSignIn();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void updateShareTimes() {
        userMapper.resetShareTimes();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void updateRegistrationDays() {
        userMapper.addRegistrationDays();
    }

    @Override
    public Integer isSignIn(Long userId) {
        if(userId == null) throw new CommonException(CommonErrorCode.USER_ID_EMPTY);
        User user = this.getById(userId);
        if(user == null) throw new CommonException(CommonErrorCode.USER_NOT_EXIST);
        return user.getIsSignIn();
    }
}