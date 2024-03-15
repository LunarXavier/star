package org.xavier.star.service.Impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xavier.star.common.CommonConstants;
import org.xavier.star.common.CommonErrorCode;
import org.xavier.star.common.CommonException;
import org.xavier.star.entity.Material;
import org.xavier.star.entity.MaterialImg;
import org.xavier.star.mapper.MaterialImgMapper;
import org.xavier.star.service.MaterialImgService;

import java.io.IOException;
import java.util.List;

import static org.xavier.star.common.CommonConstants.MATERIAL_FILE_PATH;

@Service
public class MaterialImgServiceImpl extends ServiceImpl<MaterialImgMapper, MaterialImg> implements MaterialImgService {
    @Override
    public String uploadImg(MultipartFile file, Long materialId) {
        String originalFilename = file.getOriginalFilename();
        String flag = IdUtil.fastSimpleUUID();
        String rootFilePath = MATERIAL_FILE_PATH + flag + "-" + originalFilename;
        try {
            FileUtil.writeBytes(file.getBytes(), rootFilePath);
        } catch (IOException e) {
            throw new CommonException(CommonErrorCode.READ_FILE_ERROR);
        }
        String link = CommonConstants.DOWNLOAD_PATH + rootFilePath;
        MaterialImg materialImg = MaterialImg.builder()
                .img(link)
                .materialId(materialId)
                .build();
        if (!this.save(materialImg)) throw new CommonException(CommonErrorCode.UPDATE_FAIL);
        return link;
    }

    @Override
    public List<MaterialImg> getImg(Long materialId) {
        LambdaQueryWrapper<MaterialImg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MaterialImg::getMaterialId, materialId);
        queryWrapper.orderByAsc(MaterialImg::getCreateTime);
        return this.list(queryWrapper);
    }
}
