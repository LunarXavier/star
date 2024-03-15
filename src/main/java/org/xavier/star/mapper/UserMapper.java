package org.xavier.star.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.xavier.star.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM user WHERE open_id=#{openId};")
    User getUserByOpenId(@Param("openId") String openId);

    @Update("UPDATE user SET is_sign_in = 0")
    void resetIsSignIn();

    @Update("UPDATE user SET share_times = 3")
    void resetShareTimes();

    @Update("UPDATE user SET registration_days = registration_days + 1")
    void addRegistrationDays();
}
