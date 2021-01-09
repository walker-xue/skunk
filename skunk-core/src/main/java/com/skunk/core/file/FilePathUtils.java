package com.skunk.core.file;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

/**
 * 系统文件路径工具类
 *
 * @author walker
 * @since 2019年5月12日
 */
@Slf4j
public class FilePathUtils {

    /**
     * 获取当前环境的用户临时目录
     *
     * @return
     */
    public static String getUserTmpDir() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * @param clazz
     * @return
     */
    public static <T> String getRootPath(Class<T> clazz) {
        URL url = clazz.getResource("/");
        if (url == null) {
            return ".";
        }
        log.debug(url.getPath());
        try {
            return Paths.get(url.toURI()).toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param clazz
     * @return
     */
    public static <T> Path getClassRootPath(Class<T> clazz) {
        Optional<URL> url = Optional.of(clazz.getResource("/"));
        try {
            return Paths.get(url.get().toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param path
     *     路径
     * @param base
     *     当path是相对路径时,base为起始路径
     * @return path
     */
    public static Path getDirectory(String path, String base) {
        Path dir = Paths.get(path);
        if (dir.isAbsolute()) {
            return dir;
        }
        return Paths.get(base, path);
    }
}
