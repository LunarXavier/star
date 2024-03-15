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
@ApiModel("报名表单")
public class RegistrationForm {

    @ApiModelProperty("报名填写的姓名")
    private String userName;

    @ApiModelProperty("wx号")
    private String wx;

    @ApiModelProperty("手机号")
    private String phone;
}
