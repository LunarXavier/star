package org.xavier.star.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xavier.star.common.CommonErrorCode;
import org.xavier.star.entity.User;
import org.xavier.star.util.AssertUtil;
import java.io.Serializable;

/**
 * session缓存实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("会话实体, 缓存前端")
public class SessionData implements Serializable {

    /**
     * {@link User}
     */
    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("头像")
    private String portrait;

    @ApiModelProperty("类型(0为普通用户，1为管理员)")
    private Integer type;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("会话id")
    private String sessionId;

    @ApiModelProperty("学校")
    private String school;

    @ApiModelProperty("性别 0保密1男2女")
    private Integer sex;

    @ApiModelProperty("心愿值")
    private Integer wishValue;

    @ApiModelProperty("是否关注公众号 1已关注0未关注")
    private Integer isSubscribe;

    @ApiModelProperty("今日是否签到 1已签到0未签到")
    private Integer isSignIn;

    @ApiModelProperty("今日剩余分享次数 最大为3")
    private Integer shareTimes;

    @ApiModelProperty("注册天数 默认1")
    private Integer registrationDays;

    @ApiModelProperty("参与活动数")
    private Integer participationTimes;

    @ApiModelProperty("签到获取心愿数")
    private Integer fromSignIn;

    @ApiModelProperty("其他获取心愿数")
    private Integer fromOther;

    @ApiModelProperty("参与活动获取心愿数")
    private Integer fromParticipation;

    @ApiModelProperty("用户身份 0志愿者 1小朋友")
    private Integer identity;

    //通过user对象构建sessionData对象
    public SessionData(User user){
        AssertUtil.isNotNull(user, CommonErrorCode.USER_NOT_EXIST);
        id = user.getId();
        nickname = user.getNickname();
        type = user.getType();
        portrait = user.getPortrait();
        sessionId = user.getSessionId();
        school = user.getSchool();
        sex = user.getSex();
        wishValue = user.getWishValue();
        isSubscribe = user.getIsSubscribe();
        isSignIn = user.getIsSignIn();
        shareTimes = user.getShareTimes();
        registrationDays = user.getRegistrationDays();
        participationTimes = user.getParticipationTimes();
        fromSignIn = user.getFromSignIn();
        fromOther = user.getFromOther();
        fromParticipation = user.getFromParticipation();
        identity = user.getIdentity();
    }
}
