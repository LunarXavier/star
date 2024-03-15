package org.xavier.star.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import org.xavier.star.dto.SessionData;
import org.xavier.star.dto.WxSession;
import org.xavier.star.entity.User;

public interface UserService extends IService<User> {

    User getUserById(Long id);

    String uploadPortrait(MultipartFile file, Long id);

    /**
     * 用户登录
     * @param code
     * @return
     */
    SessionData login(String code);

    WxSession getWxSessionByCode(String code);

    void checkWxSession(WxSession wxSession);

    Integer addShareTimes(Long userId);

    Integer subscribe(Long userId);

    Integer signIn(Long userId);

    void updateIsSignIn();

    void updateShareTimes();

    void updateRegistrationDays();

    Integer isSignIn(Long userId);
}
