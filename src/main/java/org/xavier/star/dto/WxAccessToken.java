package org.xavier.star.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WxAccessToken {

    private String access_token;
    private Integer expires_in;
    private Integer errcode;
    private String errmsg;

}