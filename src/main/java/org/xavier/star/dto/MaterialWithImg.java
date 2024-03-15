package org.xavier.star.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xavier.star.entity.Material;
import org.xavier.star.entity.MaterialImg;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("学习资料信息和对应预览图的组合")
public class MaterialWithImg {

    /**
     * {@link Material}
     */
    private Material material;

    private List<MaterialImg> materialImgList;

}
