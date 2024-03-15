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
@ApiModel("商品（包括两种）")
public class Goods implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("商品图片")
    private String img;

    @ApiModelProperty("商品价格")
    private Integer value;

    @ApiModelProperty("商品描述")
    private String intro;

    @ApiModelProperty("购买人数统计")
    private Integer buyersCnt;

    @ApiModelProperty("商品种类 0普通商品 1捐赠商品")
    private Integer type;

    @ApiModelProperty("捐赠进度 最大100 最小0")
    private Integer donationProcess;

    @ApiModelProperty("购买限制，0每周一次，1每周三次，2无限制")
    private Integer limitation;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
