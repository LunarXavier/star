package org.xavier.star.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@ApiModel("近期活动")
public class CurrentEvent implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("活动海报地址")
    private String poster;

    @ApiModelProperty("活动组织者")
    private String organizer;

    @ApiModelProperty("奖励心愿值")
    private Integer wishValue;

    @TableField("`require`")
    @ApiModelProperty("活动要求")
    private String require;

    @ApiModelProperty("活动状态 0报名中 1报名结束 2活动已结束")
    private Integer status;

    @ApiModelProperty("活动名称")
    private String name;

    @ApiModelProperty("活动信息")
    private String info;

    @ApiModelProperty("活动简介")
    private String intro;

    @ApiModelProperty("报名开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("报名结束时间")
    private LocalDateTime endTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
