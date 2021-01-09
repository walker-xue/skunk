package com.skunk.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import com.skunk.core.collectors.Collection2Utils;

/**
 * SQL mapper工具
 *
 * @author walker
 * @since 2019年5月13日
 */
@Component
public class SqlMapper {

    private final MSUtils msUtils;

    private final SqlSession sqlSession;

    /**
     * 构造方法，默认缓存MappedStatement
     *
     * @param sqlSession
     */
    public SqlMapper(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
        this.msUtils = new MSUtils(sqlSession.getConfiguration());
    }

    /**
     * 获取List中最多只有一个的数据
     *
     * @param list
     *     List结果
     * @param <T>
     *     泛型类型
     * @return back query result T
     */
    private <T> T getOne(List<T> list) {
        if (Collection2Utils.isEmpty(list)) {
            return null;
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
    }

    /**
     * 查询返回一个结果，多个结果时抛出异常
     *
     * @param sql
     *     执行的sql
     * @return back query result map
     */
    public Map<String, Object> selectOne(String sql) {
        return getOne(selectList(sql));
    }

    /**
     * 查询返回一个结果，多个结果时抛出异常
     *
     * @param sql
     *     执行的sql
     * @param value
     *     参数
     * @return back query result map
     */
    public Map<String, Object> selectOne(String sql, Object value) {
        return getOne(selectList(sql, value));
    }

    /**
     * 查询返回一个结果，多个结果时抛出异常
     *
     * @param sql
     *     执行的sql
     * @param resultType
     *     返回的结果类型
     * @param <T>
     *     泛型类型
     * @return back query result
     */
    public <T> T selectOne(String sql, Class<T> resultType) {
        return getOne(selectList(sql, resultType));
    }

    /**
     * 查询返回一个结果，多个结果时抛出异常
     *
     * @param sql
     *     执行的sql
     * @param value
     *     参数
     * @param resultType
     *     返回的结果类型
     * @param <T>
     *     泛型类型
     * @return back query result
     */
    public <T> T selectOne(String sql, Object value, Class<T> resultType) {
        return getOne(selectList(sql, value, resultType));
    }

    /**
     * 查询返回List<Map<String, Object>>
     *
     * @param sql
     *     执行的sql
     * @return back query result collections
     */
    public List<Map<String, Object>> selectList(String sql) {
        String msId = msUtils.select(sql);
        List<Map<String, Object>> objects = sqlSession.selectList(msId);
        sqlSession.clearCache();
        return objects;
    }

    /**
     * 查询返回List<Map<String, Object>>
     *
     * @param sql
     *     执行的sql
     * @param value
     *     参数
     * @return back query result collections
     */
    public List<Map<String, Object>> selectList(String sql, Object value) {
        Class<?> parameterType = value != null ? value.getClass() : null;
        String msId = msUtils.selectDynamic(sql, parameterType);
        List<Map<String, Object>> objects = sqlSession.selectList(msId, value);
        sqlSession.clearCache();
        return objects;
    }

    /**
     * 查询返回指定的结果类型
     *
     * @param sql
     *     执行的sql
     * @param resultType
     *     返回的结果类型
     * @param <T>
     *     泛型类型
     * @return back query result collections
     */
    public <T> List<T> selectList(String sql, Class<T> resultType) {
        String msId = Objects.isNull(resultType) ? msUtils.select(sql) : msUtils.select(sql, resultType);
        List<T> objects = sqlSession.selectList(msId);
        sqlSession.clearCache();
        return objects;
    }

    /**
     * 查询返回指定的结果类型
     *
     * @param sql
     *     执行的sql
     * @param value
     *     参数
     * @param resultType
     *     返回的结果类型
     * @param <T>
     *     泛型类型
     * @return back query result collections
     */
    public <T> List<T> selectList(String sql, Object value, Class<T> resultType) {
        Class<?> parameterType = value != null ? value.getClass() : null;
        String msId = Objects.isNull(resultType) ? msUtils.selectDynamic(sql, parameterType) : msUtils.selectDynamic(sql, parameterType, resultType);
        List<T> objects = sqlSession.selectList(msId, value);
        sqlSession.clearCache();
        return objects;
    }

    /**
     * 插入数据
     *
     * @param sql
     *     执行的sql
     * @return back update row num
     */
    public int insert(String sql) {
        String msId = msUtils.insert(sql);
        int insert = sqlSession.insert(msId);
        sqlSession.clearCache();
        return insert;
    }

    /**
     * 插入数据
     *
     * @param sql
     *     执行的sql
     * @param value
     *     参数
     * @return back update row num
     */
    public int insert(String sql, Object value) {
        Class<?> parameterType = value != null ? value.getClass() : null;
        String msId = msUtils.insertDynamic(sql, parameterType);
        int insert = sqlSession.insert(msId, value);
        sqlSession.clearCache();
        return insert;
    }

    /**
     * 更新数据
     *
     * @param sql
     *     执行的sql
     * @return back update row num
     */
    public int update(String sql) {
        String msId = msUtils.update(sql);
        int insert = sqlSession.update(msId);
        sqlSession.clearCache();
        return insert;
    }

    /**
     * 更新数据
     *
     * @param sql
     *     执行的sql
     * @param value
     *     参数
     * @return back update row num
     */
    public int update(String sql, Object value) {
        Class<?> parameterType = value != null ? value.getClass() : null;
        String msId = msUtils.updateDynamic(sql, parameterType);
        int insert = sqlSession.update(msId, value);
        sqlSession.clearCache();
        return insert;
    }

    /**
     * 删除数据
     *
     * @param sql
     *     执行的sql
     * @return back update row num
     */
    public int delete(String sql) {
        String msId = msUtils.delete(sql);
        int insert = sqlSession.delete(msId);
        sqlSession.clearCache();
        return insert;
    }

    /**
     * 删除数据
     *
     * @param sql
     *     执行的sql
     * @param value
     *     参数
     * @return back update row num
     */
    public int delete(String sql, Object value) {
        Class<?> parameterType = value != null ? value.getClass() : null;
        String msId = msUtils.deleteDynamic(sql, parameterType);
        int insert = sqlSession.delete(msId, value);
        sqlSession.clearCache();
        return insert;
    }

    private class MSUtils {

        private final Configuration configuration;
        private final LanguageDriver languageDriver;

        private MSUtils(Configuration configuration) {
            this.configuration = configuration;
            languageDriver = configuration.getDefaultScriptingLanguageInstance();
        }

        /**
         * 创建MSID
         *
         * @param sql
         *     执行的sql
         * @param sql
         *     执行的sqlCommandType
         * @return
         */
        private String newMsId(String sql, SqlCommandType sqlCommandType) {
            StringBuilder msIdBuilder = new StringBuilder(sqlCommandType.toString());
            msIdBuilder.append(".").append(sql.hashCode());
            return msIdBuilder.toString();
        }

        /**
         * 是否已经存在该ID
         *
         * @param msId
         * @return
         */
        private boolean hasMappedStatement(String msId) {
            return configuration.hasStatement(msId, false);
        }

        /**
         * 创建一个查询的MS
         *
         * @param msId
         * @param sqlSource
         *     执行的sqlSource
         * @param resultType
         *     返回的结果类型
         */

        private void newSelectMappedStatement(String msId, SqlSource sqlSource, final Class<?> resultType) {
            MappedStatement ms = new MappedStatement.Builder(configuration, msId, sqlSource, SqlCommandType.SELECT).resultMaps(new ArrayList<ResultMap>() {

                private static final long serialVersionUID = -5372725087115317079L;

                {
                    add(new ResultMap.Builder(configuration, "defaultResultMap", resultType, new ArrayList<>(0)).build());
                }
            }).build();
            // 缓存
            configuration.addMappedStatement(ms);
        }

        /**
         * 创建一个简单的MS
         *
         * @param msId
         * @param sqlSource
         *     执行的sqlSource
         * @param sqlCommandType
         *     执行的sqlCommandType
         */
        private void newUpdateMappedStatement(String msId, SqlSource sqlSource, SqlCommandType sqlCommandType) {
            MappedStatement ms = new MappedStatement.Builder(configuration, msId, sqlSource, sqlCommandType).resultMaps(new ArrayList<ResultMap>() {

                private static final long serialVersionUID = 1L;

                {
                    add(new ResultMap.Builder(configuration, "defaultResultMap", int.class, new ArrayList<>(0)).build());
                }
            }).build();
            // 缓存
            configuration.addMappedStatement(ms);
        }

        private String select(String sql) {
            String msId = newMsId(sql, SqlCommandType.SELECT);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
            newSelectMappedStatement(msId, sqlSource, Map.class);
            return msId;
        }

        private String selectDynamic(String sql, Class<?> parameterType) {
            String msId = newMsId(sql + parameterType, SqlCommandType.SELECT);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
            newSelectMappedStatement(msId, sqlSource, Map.class);
            return msId;
        }

        private String select(String sql, Class<?> resultType) {
            String msId = newMsId(resultType + sql, SqlCommandType.SELECT);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
            newSelectMappedStatement(msId, sqlSource, resultType);
            return msId;
        }

        private String selectDynamic(String sql, Class<?> parameterType, Class<?> resultType) {
            String msId = newMsId(resultType + sql + parameterType, SqlCommandType.SELECT);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
            newSelectMappedStatement(msId, sqlSource, resultType);
            return msId;
        }

        private String insert(String sql) {
            String msId = newMsId(sql, SqlCommandType.INSERT);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
            newUpdateMappedStatement(msId, sqlSource, SqlCommandType.INSERT);
            return msId;
        }

        private String insertDynamic(String sql, Class<?> parameterType) {
            String msId = newMsId(sql + parameterType, SqlCommandType.INSERT);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
            newUpdateMappedStatement(msId, sqlSource, SqlCommandType.INSERT);
            return msId;
        }

        private String update(String sql) {
            String msId = newMsId(sql, SqlCommandType.UPDATE);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
            newUpdateMappedStatement(msId, sqlSource, SqlCommandType.UPDATE);
            return msId;
        }

        private String updateDynamic(String sql, Class<?> parameterType) {
            String msId = newMsId(sql + parameterType, SqlCommandType.UPDATE);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
            newUpdateMappedStatement(msId, sqlSource, SqlCommandType.UPDATE);
            return msId;
        }

        private String delete(String sql) {
            String msId = newMsId(sql, SqlCommandType.DELETE);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
            newUpdateMappedStatement(msId, sqlSource, SqlCommandType.DELETE);
            return msId;
        }

        private String deleteDynamic(String sql, Class<?> parameterType) {
            String msId = newMsId(sql + parameterType, SqlCommandType.DELETE);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
            newUpdateMappedStatement(msId, sqlSource, SqlCommandType.DELETE);
            return msId;
        }
    }
}