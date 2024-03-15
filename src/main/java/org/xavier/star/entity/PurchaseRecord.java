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
@ApiModel("购买记录")
public class PurchaseRecord implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("商品id")
    private Long goodsId;

    @ApiModelProperty("购买者用户名")
    private String buyerName;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品类型 0普通商品 1捐赠商品")
    private Integer type;

    @ApiModelProperty("留言")
    private String message;

    @ApiModelProperty("购买数量")
    private Integer numbers;

    @ApiModelProperty("花费的心愿值总数")
    private Integer cost;

    @ApiModelProperty("微信号")
    private String wx;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
