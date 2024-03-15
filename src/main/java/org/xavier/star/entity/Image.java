package org.xavier.star.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("图片存储")
public class Image implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("图片url")
    private String img;
}
