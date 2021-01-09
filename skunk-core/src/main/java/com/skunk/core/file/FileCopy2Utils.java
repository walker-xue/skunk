package com.skunk.core.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;

import com.skunk.core.utils.Objects2;

import lombok.extern.slf4j.Slf4j;

/**
 * FileCopy2Utils 工具类
 *
 * @author walker
 * @since 0.0.1
 */
@Slf4j
public class FileCopy2Utils {

    /**
     * 拷贝文件
     * 支持拷贝文件/目录
     *
     * @param sourceFilePath
     *     源文件Path
     * @param targetPath
     *     目标文件Path
     * @throws IOException
     *     读取源文件异常
     */
    public static void copy(String sourceFilePath, String targetPath) throws IOException {

        Objects2.requireNotBlank(sourceFilePath, "No sourceFilePath File specified");
        Objects2.requireNotBlank(targetPath, "No targetPath File specified");

        File sourceFile = new File(sourceFilePath);
        File targetFile = new File(targetPath);
        // 目录时
        if (sourceFile.isDirectory()) {
            FileCopyUtils.copy(sourceFile, targetFile);
            return;
        }
        // 文件时
        if (sourceFile.isFile()) {
            FileCopyUtils.copy(sourceFile, targetFile);
        }
    }

    /**
     * 拷贝文件流，使用NIO
     *
     * @param in
     *     输入
     * @param out
     *     输出
     * @return 拷贝的字节数
     */
    public static long copy(FileInputStream in, FileOutputStream out) throws IOException {

        Assert.notNull(in, "No FileInputStream specified");
        Assert.notNull(out, "No FileOutputStream specified");

        FileChannel inChannel = in.getChannel();
        FileChannel outChannel = out.getChannel();
        return inChannel.transferTo(0, inChannel.size(), outChannel);
    }

}
