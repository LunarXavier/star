package org.xavier.star.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageUtils {
    /**
     * 通过BufferedImage图片流调整图片大小
     */
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_AREA_AVERAGING);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }


    /**
     * BufferedImage图片流转byte[]数组
     */
    public static byte[] imageToBytes(BufferedImage bImage) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, "jpg", out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }


    /**
     * byte[]数组转BufferedImage图片流
     */
    private static BufferedImage bytesToBufferedImage(byte[] ImageByte) {
        ByteArrayInputStream in = new ByteArrayInputStream(ImageByte);
        BufferedImage image = null;
        try {
            image = ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static void main(String[] args) {
        try {
            //读取原始图片
            BufferedImage image = ImageIO.read(new FileInputStream("E:/destop/效果01.png"));
            System.out.println("Width: " + image.getWidth());
            System.out.println("Height: " + image.getHeight());
            //调整图片大小
            BufferedImage newImage = ImageUtils.resizeImage(image,2000,2000);
            //图像缓冲区图片保存为图片文件(文件不存在会自动创建文件保存，文件存在会覆盖原文件保存)
            ImageIO.write(newImage, "png", new File("E:/destop/pic2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
