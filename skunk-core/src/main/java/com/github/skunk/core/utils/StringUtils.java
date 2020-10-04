package com.github.skunk.core.utils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.Validate;
import org.apache.commons.text.StringEscapeUtils;

import com.github.skunk.core.collectors.CollectorUtils;

import javax.validation.constraints.NotNull;

/**
 * 扩展 Apache 字符串工具类（StringUtils）
 *
 * @author walker
 * @since 2018年12月31日
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static final String DOT = ".";
    public static final String EMPTY = "";

    /**
     * 过滤空串
     *
     * @param str
     *     字符串
     * @return 返回结果
     */
    public static String filterNull(String str) {
        return Objects.isNull(str) ? "" : str.trim();
    }

    public static boolean containsAny(String str, String... flag) {
        if (str != null) {
            if (flag == null || flag.length == 0) {
                flag = "[-{-}-]-,".split("-");
            }
            for (String s : flag) {
                if (str.contains(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String[] listToArray(List<String> list) {

        Validate.notEmpty(list);

        if (CollectorUtils.isNotEmpty(list)) {
            return list.toArray(new String[list.size()]);
        }
        return new String[0];
    }

    public static List<String> arrayToList(String[] strings) {
        if (strings != null && strings.length > 0) {
            return Arrays.asList(strings);
        }
        return new ArrayList<>();
    }

    public static List<String> stringToList(String... strings) {
        if (strings != null && strings.length > 0) {
            return Arrays.asList(strings);
        }
        return new ArrayList<String>();
    }

    /**
     * 获得字符串对应byte数组
     *
     * @param str
     *     字符串
     * @param charset
     *     编码，如果为<code>null</code>使用系统默认编码
     * @return bytes
     */
    public static byte[] getBytes(String str, Charset charset) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return null == charset ? str.getBytes() : str.getBytes(charset);
    }

    /**
     * 创建StringBuilder对象
     *
     * @return StringBuilder对象
     */
    public static StringBuilder builder() {
        return new StringBuilder();
    }

    /**
     * 是否包含空字符串
     *
     * @param strs
     *     字符串列表
     * @return 是否包含空字符串
     */
    public static boolean hasBlank(String... strs) {

        for (String str : strs) {
            if (isBlank(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 切分字符串<br>
     *
     * <code> a#b#c    -&gt;  [a,b,c]     </code><br>
     * <code> a##b#c  -&gt;  [a,"",b,c]  </code>
     *
     * @param str
     *     被切分的字符串
     * @param separator
     *     分隔符字符
     * @return 切分后的集合
     */
    public static List<String> splits(String str, char separator) {
        return split(str, separator, 0);
    }

    /**
     * 切分字符串
     *
     * @param str
     *     被切分的字符串
     * @param separator
     *     分隔符字符
     * @param limit
     *     限制分片数
     * @return 切分后的集合
     */
    public static List<String> split(String str, char separator, int limit) {
        if (str == null) {
            return null;
        }
        List<String> list = new ArrayList<String>(limit == 0 ? 16 : limit);
        if (limit == 1) {
            list.add(str);
            return list;
        }

        boolean isNotEnd = true; // 未结束切分的标志
        int strLen = str.length();
        StringBuilder sb = new StringBuilder(strLen);
        for (int i = 0; i < strLen; i++) {
            char c = str.charAt(i);
            if (isNotEnd && c == separator) {
                list.add(sb.toString());
                // 清空StringBuilder
                sb.delete(0, sb.length());

                // 当达到切分上限-1的量时，将所剩字符全部作为最后一个串
                if (limit != 0 && list.size() == limit - 1) {
                    isNotEnd = false;
                }
            } else {
                sb.append(c);
            }
        }
        list.add(sb.toString());// 加入尾串
        return list;
    }

    /**
     * 切分字符串<br>
     * from jodd
     *
     * @param str
     *     被切分的字符串
     * @param delimiter
     *     分隔符
     * @return 字符串
     */
    public static String[] split(@NotNull String str, String delimiter) {
        if (str == null) {
            return null;
        }
        if (str.trim().length() == 0) {
            return new String[] { str };
        }

        int dellen = delimiter.length(); // del length
        int maxparts = (str.length() / dellen) + 2; // one more for the last
        int[] positions = new int[maxparts];

        int i, j = 0;
        int count = 0;
        positions[0] = -dellen;
        while ((i = str.indexOf(delimiter, j)) != -1) {
            count++;
            positions[count] = i;
            j = i + dellen;
        }
        count++;
        positions[count] = str.length();

        String[] result = new String[count];

        for (i = 0; i < count; i++) {
            result[i] = str.substring(positions[i] + dellen, positions[i + 1]);
        }
        return result;
    }

    /**
     * 去掉指定前缀
     *
     * @param str
     *     字符串
     * @param prefix
     *     前缀
     * @return 切掉后的字符串，若前缀不是 preffix， 返回原字符串
     */
    public static String removePrefix(String str, String prefix) {
        if (isEmpty(str) || isEmpty(prefix)) {
            return str;
        }

        if (str.startsWith(prefix)) {
            return subSuf(str, prefix.length());// 截取后半段
        }
        return str;
    }

    /**
     * 切割后部分
     *
     * @param string
     *     字符串
     * @param fromIndex
     *     切割开始的位置（包括）
     * @return 切割后的字符串
     */
    public static String subSuf(String string, int fromIndex) {
        if (isEmpty(string)) {
            return null;
        }
        return sub(string, fromIndex, string.length());
    }

    public static String sub(String string, int fromIndex, int toIndex) {
        int len = string.length();

        if (fromIndex < 0) {
            fromIndex = len + fromIndex;
            if (fromIndex < 0) {
                fromIndex = 0;
            }
        } else if (fromIndex > len) {
            fromIndex = len;
        }

        if (toIndex < 0) {
            toIndex = len + toIndex;
            if (toIndex < 0) {
                toIndex = len;
            }
        } else if (toIndex > len) {
            toIndex = len;
        }

        if (toIndex < fromIndex) {
            int tmp = fromIndex;
            fromIndex = toIndex;
            toIndex = tmp;
        }

        if (fromIndex == toIndex) {
            return EMPTY;
        }

        char[] strArray = string.toCharArray();
        char[] newStrArray = Arrays.copyOfRange(strArray, fromIndex, toIndex);
        return new String(newStrArray);
    }

    /**
     * 切割前部分
     *
     * @param string
     *     字符串
     * @param toIndex
     *     切割到的位置（不包括）
     * @return 切割后的字符串
     */
    public static String subPre(String string, int toIndex) {
        return sub(string, 0, toIndex);
    }

    /**
     * 字符串转html内容
     *
     * @param html
     * @return
     */
    public static String unescapeHtml4(String html) {
        return StringEscapeUtils.unescapeHtml4(html);
    }

    /**
     * 字符串转html内容
     * <p>Escapes the characters in a {@code String} using HTML entities.</p>
     *
     * <p>
     * For example:
     * </p>
     * <p><code>"bread" &amp; "butter"</code></p>
     * becomes:
     * <p>
     * <code>&amp;quot;bread&amp;quot; &amp;amp; &amp;quot;butter&amp;quot;</code>.
     * </p>
     *
     * <p>Supports all known HTML 4.0 entities, including funky accents.
     * Note that the commonly used apostrophe escape character (&amp;apos;)
     * is not a legal entity and so is not supported). </p>
     *
     * @param input
     * @return
     */
    public static String escapeHtml4(String input) {
        return StringEscapeUtils.escapeHtml4(input);
    }

}
