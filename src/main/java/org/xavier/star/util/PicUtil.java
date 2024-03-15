package org.xavier.star.util;

import net.coobird.thumbnailator.*;
import net.coobird.thumbnailator.name.Rename;

import java.io.File;
import java.util.Objects;

public class PicUtil {

    public static void compressPics(String filePath) {
        try {
            Thumbnails.of(Objects.requireNonNull(new File(filePath).listFiles()))
                    .scale(0.4f)
                    .outputQuality(1)
                    .toFiles(Rename.NO_CHANGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        compressPics("E:\\destop\\小程序卡牌图文内容\\小程序卡牌详情正面卡牌讲解对应卡图");
    }

}
