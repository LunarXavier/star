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
@ApiModel("往期活动")
public class PreviousEvent implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("公众号链接")
    private String url;

    @ApiModelProperty("推送标题")
    private String title;

    @ApiModelProperty("推送封面")
    private String cover;

    @ApiModelProperty("真实url")
    private String realUrl;

    @ApiModelProperty("具体内容截图")
    private String image;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
