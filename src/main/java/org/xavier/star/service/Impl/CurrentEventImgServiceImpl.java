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
import org.xavier.star.entity.CurrentEventImg;
import org.xavier.star.mapper.CurrentEventImgMapper;
import org.xavier.star.service.CurrentEventImgService;

import java.io.IOException;
import java.util.List;

import static org.xavier.star.common.CommonConstants.EVENT_FILE_PATH;

@Service
public class CurrentEventImgServiceImpl extends ServiceImpl<CurrentEventImgMapper, CurrentEventImg> implements CurrentEventImgService {
    @Override
    public String uploadImg(MultipartFile file, Long currentEventId) {
        String originalFilename = file.getOriginalFilename();
        String flag = IdUtil.fastSimpleUUID();
        String rootFilePath = EVENT_FILE_PATH + flag + "-" + originalFilename;
        try {
            FileUtil.writeBytes(file.getBytes(), rootFilePath);
        } catch (IOException e) {
            throw new CommonException(CommonErrorCode.READ_FILE_ERROR);
        }
        String link = CommonConstants.DOWNLOAD_PATH + rootFilePath;
        CurrentEventImg currentEventImg = CurrentEventImg.builder()
                .img(link)
                .currentEventId(currentEventId)
                .build();
        if (!this.save(currentEventImg)) throw new CommonException(CommonErrorCode.UPDATE_FAIL);
        return link;
    }

    @Override
    public List<CurrentEventImg> getImg(Long currentEventId) {
        LambdaQueryWrapper<CurrentEventImg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CurrentEventImg::getCurrentEventId, currentEventId);
        queryWrapper.orderByAsc(CurrentEventImg::getCreateTime);
        return this.list(queryWrapper);
    }
}
