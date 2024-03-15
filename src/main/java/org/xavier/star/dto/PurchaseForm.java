package org.xavier.star.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("购买表单")
public class PurchaseForm {

    /**
     * @link PurchaseRecord
     */
    @ApiModelProperty("商品id")
    private Long goodsId;

    @ApiModelProperty("微信号")
    private String wx;

    @ApiModelProperty("留言")
    private String message;

    @ApiModelProperty("购买数量")
    private Integer numbers;
}
