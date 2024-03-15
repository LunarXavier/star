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
@ApiModel("学习资料")
public class Material implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("类型 0社会情感 1数字素养 2人物专访 3学习资料 4学习方法")
    private Integer tag;

    @ApiModelProperty("资料名称")
    private String title;

    @ApiModelProperty("贡献者")
    private String contributor;

    @ApiModelProperty("下载次数")
    private Integer downloadTimes;

    @ApiModelProperty("资料简介")
    private String intro;

    @ApiModelProperty("资料封面url")
    private String cover;

    @ApiModelProperty("下载信息")
    private String downloadInfo;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
