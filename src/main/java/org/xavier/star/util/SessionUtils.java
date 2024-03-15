package org.xavier.star.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xavier.star.common.CommonConstants;
import org.xavier.star.dto.SessionData;
import org.xavier.star.entity.User;
import org.xavier.star.mapper.UserMapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

@Component
public class SessionUtils {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private RedisUtils redisUtil;

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取用户id
     * @return
     */
    public Long getUserId(){
        return Optional
                .ofNullable(getSessionData())
                .orElse(new SessionData())
                .getId();
    }

    /**
     * 获取会话实体
     * @return
     */
    public SessionData getSessionData(){
        String key = request.getHeader(CommonConstants.SESSION);
        if(key == null) return null;
        SessionData sessionData = null;
        try {
            sessionData = (SessionData) redisUtil.get(key);
        }catch (Exception e){
            return getSessionDataFromDB(key);
        }
        if(sessionData != null) return sessionData;
        return getSessionDataFromDB(key);
    }

    public String getSessionId() {
        String key = request.getHeader(CommonConstants.SESSION);
        if(key == null) return null;
        return  key;
    }

    public void setSessionId(String sessionId){
        response.setHeader(CommonConstants.SESSION, sessionId);
    }

    /**
     * 随机生成sessionId
     * @return
     */
    public String generateSessionId(){
        String sessionId = UUID.randomUUID().toString();
        response.setHeader(CommonConstants.SESSION, sessionId);
        return sessionId;
    }

    public void invalidate(){
        request.removeAttribute(CommonConstants.SESSION);
    }

    private SessionData getSessionDataFromDB(String key) {
        SessionData sessionData;
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("session_id",key);
        User user = userMapper.selectOne(userQueryWrapper);
        if(user != null){
            sessionData = new SessionData(user);
            redisUtil.set(key,sessionData);
            return sessionData;
        }else{
            redisUtil.set(key,null,3600);
            return null;
        }
    }
}
