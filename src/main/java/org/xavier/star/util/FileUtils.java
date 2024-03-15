package org.xavier.star.util;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    /**
     * 将源文件或者文件夹复制到目标路径中。
     *
     * @param sourcePath 源文件或文件夹的路径
     * @param targetPath 目标路径
     * @throws IOException 复制过程中发生的 IO 异常
     */
    public static void copy(Path sourcePath, Path targetPath, boolean copySourceDir) throws IOException {
        if (copySourceDir && Files.isDirectory(sourcePath)) {
            // 复制源目录本身到目标路径
            Path targetDir = targetPath.resolve(sourcePath.getFileName());
            Files.createDirectories(targetDir);
            copyDirectory(sourcePath.getParent(), targetDir);
        } else {
            // 复制源文件或者目录到目标路径
            if (Files.isDirectory(sourcePath)) {
                copyDirectory(sourcePath, targetPath);
            } else {
                copyFile(sourcePath, targetPath);
            }
        }
    }


    /**
     * 将源文件复制到目标路径中。
     *
     * @param sourcePath 源文件的路径
     * @param targetPath 目标路径
     * @throws IOException 复制过程中发生的 IO 异常
     */
    public static void copyFile(Path sourcePath, Path targetPath) throws IOException {
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 将源文件夹复制到目标路径中。
     *
     * @param sourcePath 源文件夹的路径
     * @param targetPath 目标路径
     * @throws IOException 复制过程中发生的 IO 异常
     */
    public static void copyDirectory(Path sourcePath, Path targetPath) throws IOException {
        // 创建目标目录
        Files.createDirectories(targetPath);

        // 复制源目录中的所有文件和子目录
        Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetDir = targetPath.resolve(sourcePath.relativize(dir));
                Files.createDirectories(targetDir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (Files.size(file) == 0) {
                    // 如果源文件为空，则跳过该文件的复制
                    return FileVisitResult.CONTINUE;
                }
                Path targetFile = targetPath.resolve(sourcePath.relativize(file));
                Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                return FileVisitResult.CONTINUE;
            }
        });
    }



    public static List<String> getAllFiles(String folderPath) {
        List<String> filePaths = new ArrayList<>();
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            // 处理文件夹不存在或不是文件夹的情况
            return null;
        }
        File[] fileList = folder.listFiles();
        if (fileList == null) {
            // 处理文件夹为空的情况
            return null;
        }
        for (File file : fileList) {
            if (file.isFile()) {
                filePaths.add(file.getAbsolutePath());
            }
        }
        return filePaths;
    }

    public static void downloadFile(String filePath, HttpServletResponse response) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("File path does not exist.");
        }
        if (file.isDirectory()) {
            throw new IllegalArgumentException("File path is a directory.");
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(),"UTF-8"));
        response.setContentType("application/octet-stream");
        FileInputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) > 0) {
            response.getOutputStream().write(buffer, 0, len);
        }
        inputStream.close();
    }

    public static void main(String[] args) {
        System.out.println(getAllFiles("E:\\destop\\helloo"));
    }

}

