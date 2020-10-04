package com.github.skunk.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.skunk.core.file.FilePathUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author walker
 */
@Slf4j
public class JsonCfgUtils {

    private static final String suffix = "*.json";

    private static final String base = FilePathUtils.getClassRootPath(JsonCfgUtils.class).toAbsolutePath().toString();

    private static Path getDirectory(String path) {
        Path dir = Paths.get(path);
        if (dir.isAbsolute()) {
            return dir;
        }
        return Paths.get(base, path);
    }

    private static List<File> getConfigFile(final String path, final String pattern) {
        final String fullPattern = pattern.endsWith(".json") ? pattern : pattern + suffix;
        final Path directory = getDirectory(path);
        final List<File> configFile = new ArrayList<>();
        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {

                final PathMatcher matcher = directory.getFileSystem().getPathMatcher("glob:" + fullPattern);

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Objects.requireNonNull(file);
                    Objects.requireNonNull(attrs);
                    if (!attrs.isDirectory()) {
                        if (matcher.matches(file.getFileName())) {
                            configFile.add(file.toFile());
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (configFile.isEmpty()) {
            log.warn("该目录下没有文件");
        }
        return configFile;
    }

    /**
     * @param path
     * @return
     */
    private static String getString(Path path) {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            CharBuffer buf = CharBuffer.allocate(1024);
            while (reader.read(buf) > 0) {
                buf.flip();
                builder.append(buf);
                buf.clear();
            }
            return builder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Map<String, T> search(String path, String pattern, Class<T> clazz) {
        List<File> files = getConfigFile(path, pattern);
        Map<String, T> ret = new HashMap<>();
        for (File file : files) {
            ret.put(file.getName(), JSON.parseObject(getString(file.toPath()), clazz));
        }
        return ret;
    }

    public static <T> Map<String, T> search(String path, String pattern, TypeReference<T> type) {
        List<File> files = getConfigFile(path, pattern);
        Map<String, T> ret = new HashMap<>();
        for (File file : files) {
            ret.put(file.getName(), JSON.parseObject(getString(file.toPath()), type));
        }
        return ret;
    }
}
