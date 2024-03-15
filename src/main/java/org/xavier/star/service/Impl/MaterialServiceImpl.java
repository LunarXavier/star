package org.xavier.star.service.Impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.xavier.star.common.CommonConstants;
import org.xavier.star.common.CommonErrorCode;
import org.xavier.star.common.CommonException;
import org.xavier.star.entity.CurrentEvent;
import org.xavier.star.entity.Material;
import org.xavier.star.entity.MaterialImg;
import org.xavier.star.mapper.MaterialMapper;
import org.xavier.star.service.MaterialImgService;
import org.xavier.star.service.MaterialService;

import java.io.IOException;

import static org.xavier.star.common.CommonConstants.EVENT_FILE_PATH;
import static org.xavier.star.common.CommonConstants.MATERIAL_FILE_PATH;

@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements MaterialService {

    @Autowired
    private MaterialImgService materialImgService;

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        if(id == null) return false;
        this.removeById(id);
        LambdaQueryWrapper<MaterialImg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MaterialImg::getMaterialId, id);
        materialImgService.remove(queryWrapper);
        return true;
    }

    @Override
    public String uploadCover(MultipartFile file, Long id) {
        Material material = this.getById(id);
        String originalFilename = file.getOriginalFilename();
        String flag = IdUtil.fastSimpleUUID();
        String rootFilePath = MATERIAL_FILE_PATH + flag + "-" + originalFilename;
        try {
            FileUtil.writeBytes(file.getBytes(), rootFilePath);
        } catch (IOException e) {
            throw new CommonException(CommonErrorCode.READ_FILE_ERROR);
        }
        String link = CommonConstants.DOWNLOAD_PATH + rootFilePath;
        material.setCover(link);
        if (!this.updateById(material)) throw new CommonException(CommonErrorCode.UPDATE_FAIL);
        return link;
    }

    @Override
    public Integer addDownloadTimes(Long id) {
        Material material = this.getById(id);
        Integer downloadTimes = material.getDownloadTimes() + 1;
        material.setDownloadTimes(downloadTimes);
        this.updateById(material);
        return downloadTimes;
    }
}
