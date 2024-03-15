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
@ApiModel("捐赠记录(用于感谢名单)")
public class DonateRecord {

    @ApiModelProperty("用户头像")
    String portrait;

    @ApiModelProperty("用户名")
    String userName;

    @ApiModelProperty("留言")
    String message;
}
