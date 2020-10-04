package com.github.skunk.core.file;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;

import com.github.skunk.core.io.FastByteArrayOutputStream;
import com.github.skunk.core.utils.CharsetUtils;
import com.github.skunk.core.utils.StringUtils;

/**
 * IO工具类
 *
 * @author xiaoleilu
 * @since 0.0.1
 */
@Slf4j
public class IoUtils {

    /**
     * 默认缓存大小
     */
    public static final int DEFAULT_BUFFER_SIZE = 1024;
    /**
     * 数据流末尾
     */
    public static final int EOF = -1;

    // --------------------------------------------------------------------------------------
    // Copy start

    /**
     * 拷贝文件
     *
     * @param resFilePath
     *     源文件Path
     * @param distFolder
     *     目标文件Path
     * @throws IOException
     *     读取源文件异常
     */
    public static void copyFile(String resFilePath, String distFolder) throws IOException {
        File resFile = new File(resFilePath);
        File distFile = new File(distFolder);
        // 目录时
        if (resFile.isDirectory()) {
            org.apache.commons.io.FileUtils.copyDirectoryToDirectory(resFile, distFile);
            return;
        }
        // 文件时
        if (resFile.isFile()) {
            FileUtils.copyFileToDirectory(resFile, distFile);
        }
    }

    /**
     * 将Reader中的内容复制到Writer中 使用默认缓存大小
     *
     * @param reader
     *     Reader
     * @param writer
     *     Writer
     * @return 拷贝的字节数
     */
    public static long copy(Reader reader, Writer writer) throws IOException {
        return copy(reader, writer, DEFAULT_BUFFER_SIZE);
    }

    /**
     * 将Reader中的内容复制到Writer中
     *
     * @param reader
     *     Reader
     * @param writer
     *     Writer
     * @param bufferSize
     *     缓存大小
     * @return 传输的byte数
     */
    public static long copy(Reader reader, Writer writer, int bufferSize) throws IOException {
        char[] buffer = new char[bufferSize];
        long size = 0;
        int readSize;

        while ((readSize = reader.read(buffer, 0, bufferSize)) != EOF) {
            writer.write(buffer, 0, readSize);
            size += readSize;
            writer.flush();
        }
        return size;
    }

    /**
     * 拷贝流，使用默认Buffer大小
     *
     * @param in
     *     输入流
     * @param out
     *     输出流
     * @return 传输的byte数
     */
    public static long copy(InputStream in, OutputStream out) throws IOException {
        return copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    /**
     * 拷贝流
     *
     * @param in
     *     输入流
     * @param out
     *     输出流
     * @param bufferSize
     *     缓存大小
     * @return 传输的byte数
     */
    public static long copy(InputStream in, OutputStream out, int bufferSize) throws IOException {
        if (null == in) {
            throw new NullPointerException("InputStream is null!");
        }
        if (null == out) {
            throw new NullPointerException("OutputStream is null!");
        }
        if (bufferSize <= 0) {
            bufferSize = DEFAULT_BUFFER_SIZE;
        }

        byte[] buffer = new byte[bufferSize];
        long size = 0;

        for (int readSize = -1; (readSize = in.read(buffer)) != EOF; ) {
            out.write(buffer, 0, readSize);
            size += readSize;
            out.flush();
        }
        return size;
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
        if (null == in) {
            throw new NullPointerException("FileInputStream is null!");
        }
        if (null == out) {
            throw new NullPointerException("FileOutputStream is null!");
        }

        FileChannel inChannel = in.getChannel();
        FileChannel outChannel = out.getChannel();

        return inChannel.transferTo(0, inChannel.size(), outChannel);
    }
    // --------------------------------------------------------------------------------------
    // Copy end

    /**
     * 获得一个文件读取器
     *
     * @param in
     *     输入流
     * @param charsetName
     *     字符集名称
     * @return BufferedReader对象
     */
    public static BufferedReader getReader(InputStream in, String charsetName) throws IOException {
        return getReader(in, Charset.forName(charsetName));
    }

    /**
     * 获取文件内容
     *
     * @param filePath
     *     目标文件路径
     * @return 文件内容
     * @throws IOException
     */
    public static List<String> getContentFromFile(String filePath) throws IOException {
        try {
            if (!(new File(filePath).exists())) {
                log.error("file not found! file:" + filePath);
                throw new RuntimeException("file not found!");
            }
            return FileUtils.readLines(new File(filePath), Charset.defaultCharset().name());
        } catch (IOException ioException) {
            log.error("io error,error:" + ioException.getMessage(), ioException);
            throw ioException;
        }
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
    public static BufferedReader getReader(InputStream in, Charset charset) throws IOException {
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
     * 从流中读取bytes
     *
     * @param in
     *     {@link InputStream}
     * @return bytes
     */
    public static byte[] readBytes(InputStream in) throws IOException {
        final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        copy(in, out);
        return out.toByteArray();
    }

    /**
     * 读取指定长度的byte数组
     *
     * @param in
     *     {@link InputStream}
     * @param length
     *     长度
     * @return bytes
     */
    public static byte[] readBytes(InputStream in, int length) throws IOException {
        byte[] b = new byte[length];
        in.read(b);
        return b;
    }

    /**
     * 从流中读取内容
     *
     * @param in
     *     输入流
     * @param charsetName
     *     字符集
     * @return 内容
     */
    public static String read(InputStream in, String charsetName) throws IOException {
        FastByteArrayOutputStream out = read(in);
        return StringUtils.isBlank(charsetName) ? out.toString() : out.toString(charsetName);
    }

    /**
     * 从流中读取内容
     *
     * @param in
     *     输入流
     * @param charset
     *     字符集
     * @return 内容
     */
    public static String read(InputStream in, Charset charset) throws IOException {
        FastByteArrayOutputStream out = read(in);
        return null == charset ? out.toString() : out.toString(charset);
    }

    /**
     * 从流中读取内容，读到输出流中
     *
     * @param in
     *     输入流
     * @return 输出流
     */
    public static FastByteArrayOutputStream read(InputStream in) throws IOException {
        final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        copy(in, out);
        return out;
    }

    /**
     * 从流中读取内容，读到输出流中
     *
     * @param in
     *     输入流
     * @return 输出流
     */
    @SuppressWarnings("unchecked")
    public static <T> T readObj(InputStream in) throws IOException {
        if (in == null) {
            throw new IllegalArgumentException("The InputStream must not be null");
        }
        try (ObjectInputStream ois = new ObjectInputStream(in);) {
            final T obj = (T) ois.readObject();
            return obj;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    /**
     * 从Reader中读取String
     *
     * @param reader
     *     Reader
     * @return String
     */
    public static String read(Reader reader) throws IOException {
        final StringBuilder builder = StringUtils.builder();
        final CharBuffer buffer = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);
        while (-1 != reader.read(buffer)) {
            builder.append(buffer.flip().toString());
        }
        return builder.toString();
    }

    /**
     * 从流中读取内容
     *
     * @param in
     *     输入流
     * @param charsetName
     *     字符集
     * @param collection
     *     返回集合
     * @return 内容
     */
    public static <T extends Collection<String>> T readLines(InputStream in, String charsetName, T collection) throws IOException {
        return readLines(in, CharsetUtils.charset(charsetName), collection);
    }

    /**
     * 从流中读取内容
     *
     * @param in
     *     输入流
     * @param charset
     *     字符集
     * @param collection
     *     返回集合
     * @return 内容
     */
    public static <T extends Collection<String>> T readLines(InputStream in, Charset charset, T collection) throws IOException {
        // 从返回的内容中读取所需内容
        BufferedReader reader = getReader(in, charset);
        String line = null;
        while ((line = reader.readLine()) != null) {
            collection.add(line);
        }

        return collection;
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
        return new ByteArrayInputStream(StringUtils.getBytes(content, charset));
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
            FileUtils.writeLines(new File(filePath), contents);
        } catch (IOException ioException) {
            log.error("io error,error:" + ioException.getMessage(), ioException);
            throw ioException;
        }
    }
}
