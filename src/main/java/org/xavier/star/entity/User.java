package org.xavier.star.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("用户")
public class User implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("会话id")
    private String sessionId;

    @ApiModelProperty("用户唯一标识")
    private String openId;

    @ApiModelProperty("会话密钥")
    private String sessionKey;

    @ApiModelProperty("unionId")
    private String unionId;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("头像")
    private String portrait;

    @ApiModelProperty("身份 0用户 1管理员")
    private Integer type;

    @ApiModelProperty("学校")
    private String school;

    @ApiModelProperty("性别 0保密1男2女")
    private Integer sex;

    @ApiModelProperty("心愿值")
    private Integer wishValue;

    @TableField("is_subscribe")
    @ApiModelProperty("是否关注公众号 1已关注0未关注")
    private Integer isSubscribe;

    @TableField("is_sign_in")
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

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
