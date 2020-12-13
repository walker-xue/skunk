package com.skunk.core.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


import com.skunk.core.collectors.CollectionUtils;
import com.skunk.core.utils.CharsetUtils;
import com.skunk.core.utils.StringUtils;
import org.springframework.util.FileCopyUtils;

/**
 * 文件工具类
 *
 * @author xiaoleilu
 * @since 0.0.1
 */
public class FileUtils {

    /**
     * The Unix separator character.
     */
    private static final char UNIX_SEPARATOR = '/';
    /**
     * The Windows separator character.
     */
    private static final char WINDOWS_SEPARATOR = '\\';

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
     * 文件是否为空<br>
     * 目录：里面没有文件时为空 文件：文件大小为0时为空
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
            if (CollectionUtils.isEmpty(subFiles)) {
                return true;
            }
        } else if (file.isFile()) {
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
        return false == isEmpty(file);
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
    public static boolean isDirEmpty(Path dirPath) throws IOException {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(dirPath)) {
            return false == dirStream.iterator().hasNext();
        } catch (IOException e) {
            throw new IOException(e);
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
    public static boolean isDirEmpty(File dir) throws IOException {
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
    public static List<File> loopFiles(File file, FileFilter fileFilter) {
        List<File> fileList = new ArrayList<File>();
        if (file == null) {
            return fileList;
        } else if (file.exists() == false) {
            return fileList;
        }

        if (file.isDirectory()) {
            for (File tmp : file.listFiles()) {
                fileList.addAll(loopFiles(tmp, fileFilter));
            }
        } else {
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
     * @return
     */
    public static List<File> loopFiles(File file) {
        return loopFiles(file, null);
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
        if (StringUtils.isBlank(path)) {
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
        if (StringUtils.isBlank(path)) {
            throw new NullPointerException("File path is blank!");
        }
        return new File(parent, path);
    }

    /**
     * 创建File对象
     *
     * @param uri
     *     文件URI
     * @return File
     */
    public static File file(URI uri) {
        if (uri == null) {
            throw new NullPointerException("File uri is null!");
        }
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
        return (file == null) ? false : file.exists();
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
    public static boolean exist(String directory, String regexp) {
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
     * 指定文件最后修改时间
     *
     * @param file
     *     文件
     * @return 最后修改时间
     */
    public static Date lastModifiedTime(File file) {
        if (!exist(file)) {
            return null;
        }
        return new Date(file.lastModified());
    }

    /**
     * 指定路径文件最后修改时间
     *
     * @param path
     *     路径
     * @return 最后修改时间
     */
    public static Date lastModifiedTime(String path) {
        File file = new File(path);
        if (!exist(file)) {
            return null;
        }
        return new Date(file.lastModified());
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

        if (false == file.exists()) {
            mkParentDirs(file);
            file.createNewFile();
        }
        return file;
    }

    /**
     * 创建所给文件或目录的父目录
     *
     * @param file
     *     文件或目录
     * @return 父目录
     */
    public static File mkParentDirs(File file) {
        final File parentFile = file.getParentFile();
        if (null != parentFile && false == parentFile.exists()) {
            parentFile.mkdirs();
        }
        return parentFile;
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
    public static boolean del(File file) throws IOException {
        if (file == null || file.exists() == false) {
            return true;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File childFile : files) {
                boolean isOk = del(childFile);
                if (isOk == false) {
                    // 删除一个出错则本次删除任务失败
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
     * 复制文件或目录<br>
     * 情况如下：<br>
     * 1、src和dest都为目录，则讲src下所有文件目录拷贝到dest下<br>
     * 2、src和dest都为文件，直接复制，名字为dest<br>
     * 3、src为文件，dest为目录，将src拷贝到dest目录下<br>
     *
     * @param src
     *     源文件
     * @param dest
     *     目标文件或目录
     * @param isOverride
     *     是否覆盖目标文件
     * @return 目标目录或文件
     * @throws IOException
     *     文件异常
     */
    public static File copy(File src, File dest, boolean isOverride) throws IOException {
        // check
        if (!src.exists()) {
            throw new FileNotFoundException("File not exist: " + src);
        }
        if (equals(src, dest)) {
            throw new IOException("Files '" + src + "' and '" + dest + "' are equal");
        }

        // 复制目录
        if (src.isDirectory()) {
            if (dest.isFile()) {
                throw new IOException(String.format("Src [%s] is a directory but Dest [%s] is a file!", src.getPath(), dest.getPath()));
            }

            if (!dest.exists()) {
                dest.mkdirs();
            }
            String files[] = src.list();
            for (String file : files) {
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                // 递归复制
                copy(srcFile, destFile, isOverride);
            }
            return dest;
        }

        // 检查目标
        if (dest.exists()) {
            if (dest.isDirectory()) {
                dest = new File(dest, src.getName());
            }
            if (false == isOverride) {
                // 不覆盖，直接跳过
                return dest;
            }
        } else {
            touch(dest);
        }

        // do copy file

        try (FileInputStream input = new FileInputStream(src); FileOutputStream output = new FileOutputStream(dest)) {
            IoUtils.copy(input, output);
        }

        if (src.length() != dest.length()) {
            throw new IOException("Copy file failed of '" + src + "' to '" + dest + "' due to different sizes");
        }

        return dest;
    }

    /**
     * 移动文件或者目录
     *
     * @param src
     *     源文件或者目录
     * @param dest
     *     目标文件或者目录
     * @param isOverride
     *     是否覆盖目标
     */
    public static void move(File src, File dest, boolean isOverride) throws IOException {
        // check
        if (!src.exists()) {
            throw new FileNotFoundException("File already exist: " + src);
        }
        if (dest.exists()) {
            if (isOverride) {
                dest.delete();
            }
        }

        // 来源为文件夹，目标为文件
        if (src.isDirectory() && dest.isFile()) {

            throw new IOException(String.format("Can not move directory [{}] to file [{}]", src, dest));
        }

        // 来源为文件，目标为文件夹
        if (src.isFile() && dest.isDirectory()) {
            dest = new File(dest, src.getName());
        }

        if (src.renameTo(dest) == false) {
            // 在文件系统不同的情况下，renameTo会失败，此时使用copy，然后删除原文件
            try {
                copy(src, dest, isOverride);
                src.delete();
            } catch (Exception e) {
                throw new IOException(String.format("Move [{}] to [{}] failed!", src, dest), e);
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
        return (file == null) ? false : file.isDirectory();
    }

    /**
     * 判断是否为文件，如果file为null，则返回false
     *
     * @param file
     *     文件
     * @return 如果为文件true
     */
    public static boolean isFile(File file) {
        return (file == null) ? false : file.isDirectory();
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

        if (StringUtils.isNotEmpty(rootDir) && StringUtils.isNotEmpty(subPath)) {
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

        if (StringUtils.isBlank(fileName) || false == fileName.contains(StringUtils.DOT)) {
            return fileName;
        }
        return StringUtils.subPre(fileName, fileName.lastIndexOf(StringUtils.DOT));
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
        int index = fileName.lastIndexOf(StringUtils.DOT);
        if (index == -1) {
            return StringUtils.EMPTY;
        } else {
            String ext = fileName.substring(index + 1);
            // 扩展名中不能包含路径相关的符号
            return (ext.contains(String.valueOf(UNIX_SEPARATOR)) || ext.contains(String.valueOf(WINDOWS_SEPARATOR))) ? StringUtils.EMPTY : ext;
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
        return getReader(file, CharsetUtils.UTF_8);
    }

    /**
     * 获得一个文件读取器
     *
     * @param file
     *     文件
     * @param charsetName
     *     字符集
     * @return BufferedReader对象
     * @throws IOException
     */
    public static BufferedReader getReader(File file, String charsetName) throws IOException {
        return IoUtils.getReader(getInputStream(file), charsetName);
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
        return IoUtils.getReader(getInputStream(file), charset);
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
    public static byte[] readBytes(File file) throws IOException {
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
        return readString(file, CharsetUtils.UTF_8);
    }

    /**
     * 读取文件内容
     *
     * @param file
     *     文件
     * @param charsetName
     *     字符集
     * @return 内容
     * @throws IOException
     *     读取文件异常
     */
    public static String readString(File file, String charsetName) throws IOException {
        return new String(readBytes(file), charsetName);
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
     * 读取文件内容
     *
     * @param url
     * @param charset
     * @return
     * @throws IOException
     *     文件异常
     */
    public static String readString(URL url, String charset) throws IOException {
        if (url == null) {
            throw new RuntimeException("Empty url provided!");
        }

        try (InputStream in = url.openStream()) {
            return IoUtils.read(in, charset);
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
    public static <T extends Collection<String>> T readLines(File file, String charset, T collection) throws IOException {
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
     * @param collection
     *     集合
     * @return 文件中的每行内容的集合
     * @throws IOException
     *     文件异常
     */
    public static <T extends Collection<String>> T readLines(URL url, String charset, T collection) throws IOException {
        try (InputStream in = url.openStream()) {
            return IoUtils.readLines(in, charset, collection);
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
    public static List<String> readLines(URL url, String charset) throws IOException {
        return readLines(url, charset, new ArrayList<String>());
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
    public static List<String> readLines(File file, String charset) throws IOException {
        return readLines(file, charset, new ArrayList<String>());
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
    public static PrintWriter getPrintWriter(File file, String charset, boolean isAppend) throws IOException {
        return new PrintWriter(getWriter(file, charset, isAppend));
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
        return writeString(content, file, CharsetUtils.UTF_8);
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
    public static File writeString(String content, File file, String charset) throws IOException {
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
    public static File appendString(String content, File file, String charset) throws IOException {
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

    // --------------------------------------------------------------------------
    // Interface start

    /**
     * Reader处理接口
     *
     * @param <T>
     * @author Luxiaolei
     */
    public interface ReaderHandler<T> {

        public T handle(BufferedReader reader) throws IOException;
    }
    // --------------------------------------------------------------------------
    // Interface end
}
