package org.xavier.star.service.Impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xavier.star.common.CommonConstants;
import org.xavier.star.common.CommonErrorCode;
import org.xavier.star.common.CommonException;
import org.xavier.star.entity.Image;
import org.xavier.star.entity.MaterialImg;
import org.xavier.star.mapper.ImageMapper;
import org.xavier.star.service.ImageService;

import java.io.IOException;

import static org.xavier.star.common.CommonConstants.IMAGE_PATH;
import static org.xavier.star.common.CommonConstants.MATERIAL_FILE_PATH;

@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {

    @Override
    public void upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String flag = IdUtil.fastSimpleUUID();
        String rootFilePath = IMAGE_PATH + flag + "-" + originalFilename;
        try {
            FileUtil.writeBytes(file.getBytes(), rootFilePath);
        } catch (IOException e) {
            throw new CommonException(CommonErrorCode.READ_FILE_ERROR);
        }
        String link = CommonConstants.DOWNLOAD_PATH + rootFilePath;
        Image image = Image.builder().img(link).build();
        if (this.baseMapper.insert(image) == 0) throw new CommonException(CommonErrorCode.UPDATE_FAIL);
    }
}
