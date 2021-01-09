package com.skunk.core.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import com.skunk.core.collectors.Collection2Utils;
import com.skunk.core.utils.CharsetUtils;
import com.skunk.core.utils.Objects2;
import com.skunk.core.utils.String2Utils;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件工具类
 * <p>
 * 针对与文件复制 请使用spring提供的FileCopyUtils
 *
 * @author walker
 */
@Slf4j
public class FileUtils {

    /**
     * 默认缓存大小
     */
    public static final int DEFAULT_BUFFER_SIZE = 1024;
    /**
     * Class文件扩展名
     */
    public static final String CLASS_EXT = ".class";
    /**
     * Jar文件扩展名
     */
    public static final String JAR_FILE_EXT = ".jar";
    /**
     * 在Jar中的路径jar的扩展名形式
     */
    public static final String JAR_PATH_EXT = ".jar!";
    /**
     * 当Path为文件形式时, path会加入一个表示文件的前缀
     */
    public static final String PATH_FILE_PRE = "file:";
    /**
     * The Unix separator character.
     */
    private static final char UNIX_SEPARATOR = '/';
    /**
     * The Windows separator character.
     */
    private static final char WINDOWS_SEPARATOR = '\\';

    /**
     * 文件是否为空<br>
     * 目录：里面没有文件时为空
     * 文件：文件大小为0时为空
     *
     * @param file
     *     文件
     * @return 是否为空，当提供非目录时，返回false
     */
    public static boolean isEmpty(File file) {
        if (null == file) {
            return true;
        }
        if (file.isDirectory()) {
            String[] subFiles = file.list();
            return Collection2Utils.isEmpty(subFiles);
        }
        if (file.isFile()) {
            return file.length() <= 0;
        }
        return false;
    }

    /**
     * 目录是否为空
     *
     * @param file
     *     目录
     * @return 是否为空，当提供非目录时，返回false
     */
    public static boolean isNotEmpty(File file) {
        return !isEmpty(file);
    }

    /**
     * 目录是否为空
     *
     * @param dirPath
     *     目录
     * @return 是否为空
     * @throws IOException
     *     创建文件异常
     */
    public static boolean isDirEmpty(Path dirPath) {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(dirPath)) {
            return !dirStream.iterator().hasNext();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 目录是否为空
     *
     * @param dir
     *     目录
     * @return 是否为空
     * @throws IOException
     *     文件异常
     */
    public static boolean isDirEmpty(File dir) {
        return isDirEmpty(dir.toPath());
    }

    /**
     * 递归遍历目录以及子目录中的所有文件
     *
     * @param file
     *     当前遍历文件
     * @param fileFilter
     *     文件过滤规则对象，选择要保留的文件
     * @return 返回文件列表
     */
    public static List<File> listFiles(File file, FileFilter fileFilter) {

        if (null == file) {
            return Collections.emptyList();
        }
        if (!file.exists()) {
            return Collections.emptyList();
        }
        List<File> fileList = new ArrayList<>();
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                fileList.addAll(listFiles(subFile, fileFilter));
            }
        }
        if (file.isFile()) {
            if (null == fileFilter || fileFilter.accept(file)) {
                fileList.add(file);
            }
        }
        return fileList;
    }

    /**
     * 递归遍历目录以及子目录中的所有文件
     *
     * @param file
     *     当前遍历文件
     * @return 返回文件列表
     */
    public static List<File> listFiles(File file) {
        return listFiles(file, null);
    }

    /**
     * 创建File对象
     *
     * @param parent
     *     父目录
     * @param path
     *     文件路径
     * @return File
     */
    public static File file(String parent, String path) {
        if (String2Utils.isBlank(path)) {
            throw new NullPointerException("File path is blank!");
        }
        return new File(parent, path);
    }

    /**
     * 创建File对象
     *
     * @param parent
     *     父文件对象
     * @param path
     *     文件路径
     * @return File
     */
    public static File file(File parent, String path) {
        if (String2Utils.isBlank(path)) {
            throw new NullPointerException("File path is blank!");
        }
        return new File(parent, path);
    }

    /**
     * 通过URI获取内容，并且创建File对象
     *
     * @param uri
     *     文件URI
     * @return File
     */
    public static File file(@NotNull URI uri) {
        Objects.requireNonNull(uri, "File uri is null!");

        return new File(uri);
    }

    /**
     * 判断文件是否存在，如果file为null，则返回false
     *
     * @param file
     *     文件
     * @return 如果存在返回true
     */
    public static boolean exist(File file) {
        return (null != file) && file.exists();
    }

    /**
     * 是否存在匹配文件
     *
     * @param directory
     *     文件夹路径
     * @param regexp
     *     文件夹中所包含文件名的正则表达式
     * @return 如果存在匹配文件返回true
     */
    public static boolean exist(@NotBlank String directory, @NotBlank String regexp) {

        Objects2.requireNotBlank(directory, "Directory is null.");
        Objects2.requireNotBlank(regexp, "regexp is null.");

        File file = new File(directory);
        if (!file.exists()) {
            return false;
        }

        String[] fileList = file.list();
        if (fileList == null) {
            return false;
        }

        for (String fileName : fileList) {
            if (fileName.matches(regexp)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件最后修改时间
     *
     * @param file
     *     文件
     * @return 最后修改时间
     */
    public static Long lastModified(File file) {
        if (!exist(file)) {
            return null;
        }
        return file.lastModified();
    }

    /**
     * 指定路径文件最后修改时间
     *
     * @param path
     *     路径
     * @return 最后修改时间
     */
    public static Long lastModified(@NotBlank String path) {
        Objects2.requireNotBlank(path, "File path is null.");

        File file = new File(path);
        if (!exist(file)) {
            return null;
        }
        return file.lastModified();
    }

    /**
     * 创建文件，如果这个文件存在，直接返回这个文件
     *
     * @param file
     *     文件对象
     * @return 文件，若路径为null，返回null
     * @throws IOException
     *     文件异常
     */
    public static File touch(File file) throws IOException {
        if (null == file) {
            return null;
        }

        if (!file.exists()) {
            mkParentDirs(file);
            boolean newFile = file.createNewFile();
            if (!newFile) {
                return null;
            }
        }
        return file;
    }

    /**
     * 创建所给文件或目录的父目录
     *
     * @param file
     *     文件或目录
     */
    public static void mkParentDirs(@NotNull File file) {

        Objects.requireNonNull(file, "File is null.");

        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            boolean mkDirs = parentFile.mkdirs();
            if (!mkDirs) {
                throw new RuntimeException(String.format("Create file Dirs err. %s", file.getParentFile()));
            }
        }
    }

    /**
     * 删除文件或者文件夹
     *
     * @param file
     *     文件对象
     * @return 成功与否
     * @throws IOException
     *     文件异常
     */
    public static boolean delete(@NotNull File file) {

        Objects.requireNonNull(file, "File is null.");

        if (!file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File childFile : files) {
                if (!delete(childFile)) {
                    return false;
                }
            }
        }
        return file.delete();
    }

    /**
     * 创建临时文件<br>
     * 创建后的文件名为 prefix[Randon].tmp
     *
     * @param dir
     *     临时文件创建的所在目录
     * @return 临时文件
     * @throws IOException
     *     文件异常
     */
    public static File createTempFile(File dir) throws IOException {
        return createTempFile("hutool", null, dir, true);
    }

    /**
     * 创建临时文件<br>
     * 创建后的文件名为 prefix[Randon].tmp
     *
     * @param dir
     *     临时文件创建的所在目录
     * @param isReCreat
     *     是否重新创建文件（删掉原来的，创建新的）
     * @return 临时文件
     * @throws IOException
     *     文件异常
     */
    public static File createTempFile(File dir, boolean isReCreat) throws IOException {
        return createTempFile("hutool", null, dir, isReCreat);
    }

    /**
     * 创建临时文件<br>
     * 创建后的文件名为 prefix[Randon].suffix From com.jodd.io.FileUtil
     *
     * @param prefix
     *     前缀，至少3个字符
     * @param suffix
     *     后缀，如果null则使用默认.tmp
     * @param dir
     *     临时文件创建的所在目录
     * @param isReCreat
     *     是否重新创建文件（删掉原来的，创建新的）
     * @return 临时文件
     * @throws IOException
     *     文件异常
     */
    public static File createTempFile(String prefix, String suffix, File dir, boolean isReCreat) throws IOException {
        int exceptionsCount = 0;
        while (true) {
            try {
                File file = File.createTempFile(prefix, suffix, dir).getCanonicalFile();
                if (isReCreat) {
                    file.delete();
                    file.createNewFile();
                }
                return file;
            } catch (IOException ioex) { // fixes java.io.WinNTFileSystem.createFileExclusively access denied
                if (++exceptionsCount >= 50) {
                    throw ioex;
                }
            }
        }
    }

    /**
     * 移动文件或者目录
     *
     * @param source
     *     源文件或者目录
     * @param target
     *     目标文件或者目录
     * @param isOverride
     *     是否覆盖目标
     */
    public static void move(@NotNull File source, @NotNull File target, boolean isOverride) throws IOException {

        Objects.requireNonNull(source, "Source file is null.");
        Objects.requireNonNull(target, "Target file is null.");

        // check
        if (!source.exists()) {
            throw new FileNotFoundException("File already exist: " + source);
        }
        if (target.exists() && isOverride) {
            boolean delete = delete(target);
            if (!delete) {
                throw new RuntimeException(String.format("Delete target file fail, %s ", target.getName()));
            }
        }

        // 来源为文件夹，目标为文件
        if (source.isDirectory() && target.isFile()) {
            throw new IOException(String.format("Can not move directory [{}] to file [{}]", source, target));
        }

        // 来源为文件，目标为文件夹
        if (source.isFile() && target.isDirectory()) {
            target = new File(target, source.getName());
        }

        if (!source.renameTo(target)) {
            // 在文件系统不同的情况下，renameTo会失败，此时使用copy，然后删除原文件
            try {
                FileCopyUtils.copy(source, target);
                boolean delete = delete(source);
                if (!delete) {
                    throw new RuntimeException(String.format("Delete source file fail, %s ", source.getName()));
                }
            } catch (Exception e) {
                throw new IOException(String.format("Move [{}] to [{}] failed!", source, target), e);
            }
        }
    }

    /**
     * 获取标准的绝对路径
     *
     * @param file
     *     文件
     * @return 绝对路径
     */
    public static String getAbsolutePath(File file) {
        if (file == null) {
            return null;
        }

        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            return file.getAbsolutePath();
        }
    }

    /**
     * 判断是否为目录，如果file为null，则返回false
     *
     * @param file
     *     文件
     * @return 如果为目录true
     */
    public static boolean isDirectory(File file) {
        return (file != null) && file.isDirectory();
    }

    /**
     * 判断是否为文件，如果file为null，则返回false
     *
     * @param file
     *     文件
     * @return 如果为文件true
     */
    public static boolean isFile(File file) {
        return (file != null) && file.isDirectory();
    }

    /**
     * 检查两个文件是否是同一个文件
     *
     * @param file1
     *     文件1
     * @param file2
     *     文件2
     * @return 是否相同
     */
    public static boolean equals(File file1, File file2) {
        try {
            file1 = file1.getCanonicalFile();
            file2 = file2.getCanonicalFile();
        } catch (IOException ignore) {
            return false;
        }
        return file1.equals(file2);
    }

    /**
     * 获得最后一个文件路径分隔符的位置
     *
     * @param filePath
     *     文件路径
     * @return 最后一个文件路径分隔符的位置
     */
    public static int indexOfLastSeparator(String filePath) {
        if (filePath == null) {
            return -1;
        }
        int lastUnixPos = filePath.lastIndexOf(UNIX_SEPARATOR);
        int lastWindowsPos = filePath.lastIndexOf(WINDOWS_SEPARATOR);
        return (lastUnixPos >= lastWindowsPos) ? lastUnixPos : lastWindowsPos;
    }

    /**
     * 判断文件是否被改动<br>
     * 如果文件对象为 null 或者文件不存在，被视为改动
     *
     * @param file
     *     文件对象
     * @param lastModifyTime
     *     上次的改动时间
     * @return 是否被改动
     */
    public static boolean isModifed(File file, long lastModifyTime) {
        if (null == file || false == file.exists()) {
            return true;
        }
        return file.lastModified() != lastModifyTime;
    }

    /**
     * 修复路径<br>
     * 1. 统一用 / <br>
     * 2. 多个 / 转换为一个
     *
     * @param path
     *     原路径
     * @return 修复后的路径
     */
    public static String normalize(String path) {
        return path.replaceAll("[/\\\\]{1,}", "/");
    }

    /**
     * 获得相对子路径
     *
     * @param rootDir
     *     绝对父路径
     * @param file
     *     文件
     * @return 相对子路径
     */
    public static String subPath(String rootDir, File file) throws IOException {
        if (StringUtils.isEmpty(rootDir)) {

        }

        String subPath = null;
        subPath = file.getCanonicalPath();

        if (String2Utils.isNotEmpty(rootDir) && String2Utils.isNotEmpty(subPath)) {
            rootDir = normalize(rootDir);
            subPath = normalize(subPath);

            if (subPath != null && subPath.toLowerCase().startsWith(subPath.toLowerCase())) {
                subPath = subPath.substring(rootDir.length() + 1);
            }
        }
        return subPath;
    }

    // --------------------------------------------------------------------------------------------
    // name start

    /**
     * 返回主文件名
     *
     * @param file
     *     文件
     * @return 主文件名
     */
    public static String mainName(File file) {
        if (file.isDirectory()) {
            return file.getName();
        }
        return mainName(file.getName());
    }

    /**
     * 返回主文件名
     *
     * @param fileName
     *     完整文件名
     * @return 主文件名
     */
    public static String mainName(String fileName) {

        if (String2Utils.isBlank(fileName) || false == fileName.contains(String2Utils.DOT)) {
            return fileName;
        }
        return String2Utils.subPre(fileName, fileName.lastIndexOf(String2Utils.DOT));
    }

    /**
     * 获取文件扩展名
     *
     * @param file
     *     文件
     * @return 扩展名
     */
    public static String extName(File file) {
        if (null == file) {
            return null;
        }
        if (file.isDirectory()) {
            return null;
        }
        return extName(file.getName());
    }

    /**
     * 获得文件的扩展名
     *
     * @param fileName
     *     文件名
     * @return 扩展名
     */
    public static String extName(String fileName) {
        if (fileName == null) {
            return null;
        }
        int index = fileName.lastIndexOf(String2Utils.DOT);
        if (index == -1) {
            return String2Utils.EMPTY;
        } else {
            String ext = fileName.substring(index + 1);
            // 扩展名中不能包含路径相关的符号
            return (ext.contains(String.valueOf(UNIX_SEPARATOR)) || ext.contains(String.valueOf(WINDOWS_SEPARATOR))) ? String2Utils.EMPTY : ext;
        }
    }

    /**
     * 获得输入流
     *
     * @param file
     *     文件
     * @return 输入流
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static BufferedInputStream getInputStream(File file) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(file));
    }

    /**
     * 获得一个文件读取器
     *
     * @param file
     *     文件
     * @return BufferedReader对象
     * @throws IOException
     */
    public static BufferedReader getUtf8Reader(File file) throws IOException {
        return getReader(file, CharsetUtils.CHARSET_UTF_8);
    }

    /**
     * 获得一个文件读取器
     *
     * @param file
     *     文件
     * @param charset
     *     字符集
     * @return BufferedReader对象
     * @throws IOException
     *     获取文件异常
     */
    public static BufferedReader getReader(File file, Charset charset) throws IOException {
        return getReader(getInputStream(file), charset);
    }

    // --------------------------------------------------------------------------------------------
    // in end

    /**
     * 读取文件所有数据<br>
     * 文件的长度不能超过Integer.MAX_VALUE
     *
     * @param file
     *     文件
     * @return 字节码
     * @throws IOException
     *     读取文件异常
     */
    public static byte[] readBytes(@NotNull File file) throws IOException {

        Objects.requireNonNull(file, "File is null");

        // check
        if (!file.exists()) {
            throw new FileNotFoundException("File not exist: " + file);
        }
        if (!file.isFile()) {
            throw new IOException("Not a file:" + file);
        }

        long len = file.length();
        if (len >= Integer.MAX_VALUE) {
            throw new IOException("File is larger then max array size");
        }

        byte[] bytes = new byte[(int) len];
        try (FileInputStream in = new FileInputStream(file)) {
            in.read(bytes);
        }
        return bytes;
    }

    /**
     * 读取文件内容
     *
     * @param file
     *     文件
     * @return 内容
     * @throws IOException
     *     读取文件异常
     */
    public static String readUtf8String(File file) throws IOException {
        return readString(file, CharsetUtils.CHARSET_UTF_8);
    }

    /**
     * 读取文件内容
     *
     * @param file
     *     文件
     * @param charset
     *     字符集
     * @return 内容
     * @throws IOException
     *     读取文件异常
     */
    public static String readString(File file, Charset charset) throws IOException {
        return new String(readBytes(file), charset);
    }
    /**
     * 从Reader中读取String
     *
     * @param reader
     *     Reader
     * @return String
     */
    public static String readString(Reader reader) throws IOException {
        final StringBuilder builder = String2Utils.builder();
        final CharBuffer buffer = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);
        while (-1 != reader.read(buffer)) {
            builder.append(buffer.flip().toString());
        }
        return builder.toString();
    }

    /**
     * 获得一个文件读取器
     *
     * @param in
     *     输入流
     * @param charset
     *     字符集
     * @return BufferedReader对象
     */
    public static BufferedReader getReader(InputStream in, Charset charset) {
        if (null == in) {
            return null;
        }

        InputStreamReader reader = null;
        if (null == charset) {
            reader = new InputStreamReader(in);
        } else {
            reader = new InputStreamReader(in, charset);
        }

        return new BufferedReader(reader);
    }

    /**
     * 从流中读取内容并且转成Object对象
     * <p>
     * 注意：对象要实现序列化接口；并且该对象的class文件存在
     *
     * @param in
     *     输入流
     * @return 输出流
     */
    public static <T> T readObject(@NotNull InputStream in) {
        Objects.requireNonNull(in, "The InputStream must not be null");

        try (ObjectInputStream ois = new ObjectInputStream(in)) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    /**
     * 从文件中读取每一行数据
     *
     * @param file
     *     文件路径
     * @param charset
     *     字符集
     * @param collection
     *     集合
     * @return 文件中的每行内容的集合
     * @throws IOException
     *     文件异常
     */
    public static <T extends Collection<String>> T readLines(File file, Charset charset, T collection) throws IOException {
        Objects.requireNonNull(file, "The File must not be null");

        try (BufferedReader reader = getReader(file, charset)) {
            String line;
            while (true) {
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                collection.add(line);
            }
            return collection;
        }
    }

    /**
     * 从文件中读取每一行数据
     *
     * @param url
     *     文件的URL
     * @param charset
     *     字符集
     * @return 文件中的每行内容的集合List
     * @throws IOException
     *     文件异常
     */
    public static List<String> readLines(URL url, Charset charset) {
        Objects.requireNonNull(url, "URL is null");
        return readLines(url, charset);
    }

    /**
     * 从文件中读取每一行数据
     *
     * @param file
     *     文件
     * @param charset
     *     字符集
     * @return 文件中的每行内容的集合List
     * @throws IOException
     *     文件异常
     */
    public static List<String> readLines(File file, Charset charset) {
        return readLines(file, charset);
    }

    /**
     * 获得一个输出流对象
     *
     * @param file
     *     文件
     * @return 输出流对象
     * @throws IOException
     *     文件异常
     */
    public static BufferedOutputStream getOutputStream(File file) throws IOException {
        return new BufferedOutputStream(new FileOutputStream(touch(file)));
    }

    /**
     * 获得一个带缓存的写入对象
     *
     * @param file
     *     输出文件
     * @param charsetName
     *     字符集
     * @param isAppend
     *     是否追加
     * @return BufferedReader对象
     * @throws IOException
     *     文件异常
     */
    public static BufferedWriter getWriter(File file, String charsetName, boolean isAppend) throws IOException {
        return getWriter(file, Charset.forName(charsetName), isAppend);
    }

    /**
     * 获得一个带缓存的写入对象
     *
     * @param file
     *     输出文件
     * @param charset
     *     字符集
     * @param isAppend
     *     是否追加
     * @return BufferedReader对象
     * @throws IOException
     *     文件异常
     */
    public static BufferedWriter getWriter(File file, Charset charset, boolean isAppend) throws IOException {
        if (false == file.exists()) {
            file.createNewFile();
        }
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, isAppend), charset));
    }

    /**
     * 获得一个打印写入对象，可以有print
     *
     * @param file
     *     文件
     * @param charset
     *     字符集
     * @param isAppend
     *     是否追加
     * @return 打印对象
     * @throws IOException
     *     文件异常
     */
    public static PrintWriter getPrintWriter(File file, Charset charset, boolean isAppend) throws IOException {
        return new PrintWriter(getWriter(file, charset, isAppend));
    }
    /**
     * String 转为流
     *
     * @param content
     *     内容
     * @param charsetName
     *     编码
     * @return 字节流
     */
    public static ByteArrayInputStream toStream(String content, String charsetName) {
        return toStream(content, CharsetUtils.charset(charsetName));
    }

    /**
     * String 转为流
     *
     * @param content
     *     内容
     * @param charset
     *     编码
     * @return 字节流
     */
    public static ByteArrayInputStream toStream(String content, Charset charset) {
        if (content == null) {
            return null;
        }
        return new ByteArrayInputStream(String2Utils.getBytes(content, charset));
    }

    /**
     * 将多部分内容写到流中
     *
     * @param out
     *     输出流
     * @param charset
     *     写出的内容的字符集
     * @param isCloseOut
     *     写入完毕是否关闭输出流
     * @param contents
     *     写入的内容
     */
    public static void writeObjects(OutputStream out, String charset, boolean isCloseOut, Serializable... contents) throws IOException {
        try (ObjectOutputStream osw = out instanceof ObjectOutputStream ? (ObjectOutputStream) out : new ObjectOutputStream(out)) {
            for (Object content : contents) {
                if (content != null) {
                    osw.writeObject(content);
                    osw.flush();
                }
            }
        } catch (Exception e) {
            throw new IOException("Write content to OutputStream error!", e);
        }
    }

    /**
     * 给指定文件追加内容
     *
     * @param filePath
     *     目标文件Path
     * @param contents
     *     append contents
     * @throws IOException
     *     文件为找到异常
     */
    public static void appendContent(String filePath, List<String> contents) throws IOException {
        try {
            writeLines(new File(filePath), contents);
        } catch (IOException ioException) {
            log.error("io error,error:" + ioException.getMessage(), ioException);
            throw ioException;
        }
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * the specified <code>File</code> line by line.
     * The default VM encoding and the default line ending will be used.
     *
     * @param file
     *     the file to write to
     * @param lines
     *     the lines to write, {@code null} entries produce blank lines
     * @throws IOException
     *     in case of an I/O error
     * @since 1.3
     */
    public static void writeLines(final File file, final Collection<?> lines) throws IOException {
        writeLines(file, null, lines, null, false);
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * the specified <code>File</code> line by line.
     * The default VM encoding and the default line ending will be used.
     *
     * @param file
     *     the file to write to
     * @param lines
     *     the lines to write, {@code null} entries produce blank lines
     * @param append
     *     if {@code true}, then the lines will be added to the
     *     end of the file rather than overwriting
     * @throws IOException
     *     in case of an I/O error
     * @since 2.1
     */
    public static void writeLines(final File file, final Collection<?> lines, final boolean append) throws IOException {
        writeLines(file, null, lines, null, append);
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * the specified <code>File</code> line by line.
     * The default VM encoding and the specified line ending will be used.
     *
     * @param file
     *     the file to write to
     * @param lines
     *     the lines to write, {@code null} entries produce blank lines
     * @param lineEnding
     *     the line separator to use, {@code null} is system default
     * @throws IOException
     *     in case of an I/O error
     * @since 1.3
     */
    public static void writeLines(final File file, final Collection<?> lines, final String lineEnding)
        throws IOException {
        writeLines(file, null, lines, lineEnding, false);
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * the specified <code>File</code> line by line.
     * The default VM encoding and the specified line ending will be used.
     *
     * @param file
     *     the file to write to
     * @param lines
     *     the lines to write, {@code null} entries produce blank lines
     * @param lineEnding
     *     the line separator to use, {@code null} is system default
     * @param append
     *     if {@code true}, then the lines will be added to the
     *     end of the file rather than overwriting
     * @throws IOException
     *     in case of an I/O error
     * @since 2.1
     */
    public static void writeLines(final File file, final Collection<?> lines, final String lineEnding,
        final boolean append) throws IOException {
        writeLines(file, null, lines, lineEnding, append);
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * the specified <code>File</code> line by line.
     * The specified character encoding and the default line ending will be used.
     * <p>
     * NOTE: As from v1.3, the parent directories of the file will be created
     * if they do not exist.
     * </p>
     *
     * @param file
     *     the file to write to
     * @param charsetName
     *     the name of the requested charset, {@code null} means platform default
     * @param lines
     *     the lines to write, {@code null} entries produce blank lines
     * @throws IOException
     *     in case of an I/O error
     * @throws java.io.UnsupportedEncodingException
     *     if the encoding is not supported by the VM
     * @since 1.1
     */
    public static void writeLines(final File file, final String charsetName, final Collection<?> lines)
        throws IOException {
        writeLines(file, charsetName, lines, null, false);
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * the specified <code>File</code> line by line, optionally appending.
     * The specified character encoding and the default line ending will be used.
     *
     * @param file
     *     the file to write to
     * @param charsetName
     *     the name of the requested charset, {@code null} means platform default
     * @param lines
     *     the lines to write, {@code null} entries produce blank lines
     * @param append
     *     if {@code true}, then the lines will be added to the
     *     end of the file rather than overwriting
     * @throws IOException
     *     in case of an I/O error
     * @throws java.io.UnsupportedEncodingException
     *     if the encoding is not supported by the VM
     * @since 2.1
     */
    public static void writeLines(final File file, final String charsetName, final Collection<?> lines,
        final boolean append) throws IOException {
        writeLines(file, charsetName, lines, null, append);
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * the specified <code>File</code> line by line.
     * The specified character encoding and the line ending will be used.
     * <p>
     * NOTE: As from v1.3, the parent directories of the file will be created
     * if they do not exist.
     * </p>
     *
     * @param file
     *     the file to write to
     * @param charsetName
     *     the name of the requested charset, {@code null} means platform default
     * @param lines
     *     the lines to write, {@code null} entries produce blank lines
     * @param lineEnding
     *     the line separator to use, {@code null} is system default
     * @throws IOException
     *     in case of an I/O error
     * @throws java.io.UnsupportedEncodingException
     *     if the encoding is not supported by the VM
     * @since 1.1
     */
    public static void writeLines(final File file, final String charsetName, final Collection<?> lines,
        final String lineEnding) throws IOException {
        writeLines(file, charsetName, lines, lineEnding, false);
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * the specified <code>File</code> line by line.
     * The specified character encoding and the line ending will be used.
     *
     * @param file
     *     the file to write to
     * @param charsetName
     *     the name of the requested charset, {@code null} means platform default
     * @param lines
     *     the lines to write, {@code null} entries produce blank lines
     * @param lineEnding
     *     the line separator to use, {@code null} is system default
     * @param append
     *     if {@code true}, then the lines will be added to the
     *     end of the file rather than overwriting
     * @throws IOException
     *     in case of an I/O error
     * @throws java.io.UnsupportedEncodingException
     *     if the encoding is not supported by the VM
     * @since 2.1
     */
    public static void writeLines(final File file, final String charsetName, final Collection<?> lines,
        final String lineEnding, final boolean append) throws IOException {
        try (OutputStream out = new BufferedOutputStream(openOutputStream(file, append))) {
            writeLines(lines, lineEnding, out, charsetName);
        }
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * an <code>OutputStream</code> line by line, using the specified character
     * encoding and the specified line ending.
     *
     * @param lines
     *     the lines to write, null entries produce blank lines
     * @param lineEnding
     *     the line separator to use, null is system default
     * @param output
     *     the <code>OutputStream</code> to write to, not null, not closed
     * @param charset
     *     the charset to use, null means platform default
     * @throws NullPointerException
     *     if the output is null
     * @throws IOException
     *     if an I/O error occurs
     * @since 2.3
     */
    public static void writeLines(final Collection<?> lines, String lineEnding, final OutputStream output,
        final Charset charset) throws IOException {
        if (lines == null) {
            return;
        }
        if (lineEnding == null) {
            lineEnding = System.lineSeparator();
        }
        final Charset cs = charset;
        for (final Object line : lines) {
            if (line != null) {
                output.write(line.toString().getBytes(cs));
            }
            output.write(lineEnding.getBytes(cs));
        }
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * an <code>OutputStream</code> line by line, using the specified character
     * encoding and the specified line ending.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     *
     * @param lines
     *     the lines to write, null entries produce blank lines
     * @param lineEnding
     *     the line separator to use, null is system default
     * @param output
     *     the <code>OutputStream</code> to write to, not null, not closed
     * @param charsetName
     *     the name of the requested charset, null means platform default
     * @throws NullPointerException
     *     if the output is null
     * @throws IOException
     *     if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException
     *     thrown instead of {@link java.io
     *     .UnsupportedEncodingException} in version 2.2 if the
     *     encoding is not supported.
     * @since 1.1
     */
    public static void writeLines(final Collection<?> lines, final String lineEnding,
        final OutputStream output, final String charsetName) throws IOException {
        writeLines(lines, lineEnding, output, Charset.forName(charsetName));
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * a <code>Writer</code> line by line, using the specified line ending.
     *
     * @param lines
     *     the lines to write, null entries produce blank lines
     * @param lineEnding
     *     the line separator to use, null is system default
     * @param writer
     *     the <code>Writer</code> to write to, not null, not closed
     * @throws NullPointerException
     *     if the input is null
     * @throws IOException
     *     if an I/O error occurs
     * @since 1.1
     */
    public static void writeLines(final Collection<?> lines, String lineEnding,
        final Writer writer) throws IOException {
        if (lines == null) {
            return;
        }
        if (lineEnding == null) {
            lineEnding = System.lineSeparator();
        }
        for (final Object line : lines) {
            if (line != null) {
                writer.write(line.toString());
            }
            writer.write(lineEnding);
        }
    }

    /**
     * Opens a {@link FileOutputStream} for the specified file, checking and
     * creating the parent directory if it does not exist.
     * <p>
     * At the end of the method either the stream will be successfully opened,
     * or an exception will have been thrown.
     * </p>
     * <p>
     * The parent directory will be created if it does not exist.
     * The file will be created if it does not exist.
     * An exception is thrown if the file object exists but is a directory.
     * An exception is thrown if the file exists but cannot be written to.
     * An exception is thrown if the parent directory cannot be created.
     * </p>
     *
     * @param file
     *     the file to open for output, must not be {@code null}
     * @param append
     *     if {@code true}, then bytes will be added to the
     *     end of the file rather than overwriting
     * @return a new {@link FileOutputStream} for the specified file
     * @throws IOException
     *     if the file object is a directory
     * @throws IOException
     *     if the file cannot be written to
     * @throws IOException
     *     if a parent directory needs creating but that fails
     * @since 2.1
     */
    public static FileOutputStream openOutputStream(final File file, final boolean append) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canWrite() == false) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            final File parent = file.getParentFile();
            if (parent != null) {
                if (!parent.mkdirs() && !parent.isDirectory()) {
                    throw new IOException("Directory '" + parent + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file, append);
    }

    /**
     * 将String写入文件，覆盖模式，字符集为UTF-8
     *
     * @param content
     *     写入的内容
     * @param file
     *     文件
     * @return 写入的文件
     * @throws IOException
     *     文件异常
     */
    public static File writeUtf8String(String content, File file) throws IOException {
        return writeString(content, file, CharsetUtils.CHARSET_UTF_8);
    }

    /**
     * 将String写入文件，覆盖模式
     *
     * @param content
     *     写入的内容
     * @param file
     *     文件
     * @param charset
     *     字符集
     * @return 文件
     * @throws IOException
     *     文件异常
     */
    public static File writeString(String content, File file, Charset charset) throws IOException {
        try (PrintWriter writer = getPrintWriter(file, charset, false)) {
            writer.print(content);
            writer.flush();
        }
        return file;
    }

    /**
     * 将String写入文件，追加模式
     *
     * @param content
     *     写入的内容
     * @param file
     *     文件
     * @param charset
     *     字符集
     * @return 写入的文件
     * @throws IOException
     *     文件异常
     */
    public static File appendString(String content, File file, Charset charset) throws IOException {
        try (PrintWriter writer = getPrintWriter(file, charset, true)) {
            writer.print(content);
            writer.flush();
        }
        return file;
    }

    /**
     * 写数据到文件中
     *
     * @param dest
     *     目标文件
     * @param data
     *     数据
     * @return dest
     * @throws IOException
     *     文件异常
     */
    public static File writeBytes(byte[] data, File dest) throws IOException {
        return writeBytes(data, dest, 0, data.length, false);
    }

    /**
     * 写入数据到文件
     *
     * @param data
     *     数据
     * @param dest
     *     目标文件
     * @param off
     *     off
     * @param len
     *     长度
     * @param append
     *     是否拼接
     * @return 文件
     * @throws IOException
     *     文件异常
     */
    public static File writeBytes(byte[] data, File dest, int off, int len, boolean append) throws IOException {
        if (dest.exists() == true) {
            if (dest.isFile() == false) {
                throw new IOException("Not a file: " + dest);
            }
        }
        try (FileOutputStream out = new FileOutputStream(dest, append)) {
            out.write(data, off, len);
            out.flush();
        }
        return dest;
    }

    /**
     * 将流的内容写入文件<br>
     *
     * @param dest
     *     目标文件
     * @param in
     *     输入流
     * @return dest
     */
    public static File writeFromStream(InputStream in, File dest) throws IOException {
        try (FileOutputStream out = new FileOutputStream(dest)) {
            FileCopyUtils.copy(in, out);
        }
        return dest;
    }

    /**
     * 将文件写入流中
     *
     * @param file
     *     文件
     * @param out
     *     流
     */
    public static void writeToStream(File file, OutputStream out) throws IOException {
        try (FileInputStream in = new FileInputStream(file)) {
            FileCopyUtils.copy(in, out);
        }
    }

    /**
     * 可读的文件大小
     *
     * @param file
     *     文件
     * @return 大小
     */
    public static String readableFileSize(File file) {
        return readableFileSize(file.length());
    }

    /**
     * 可读的文件大小<br>
     * 参考 http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc
     *
     * @param size
     *     Long类型大小
     * @return 大小
     */
    public static String readableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB", "EB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
    /**
     * 创建多层目录
     *
     * @param file
     */
    public static void mkDirs(@NotNull File file) {
        Objects.requireNonNull(file);
        boolean mkDirs = file.mkdirs();
        if (!mkDirs) {
            throw new RuntimeException(String.format("Mkdir file error, path: %s", file.getPath()));
        }
    }
}
