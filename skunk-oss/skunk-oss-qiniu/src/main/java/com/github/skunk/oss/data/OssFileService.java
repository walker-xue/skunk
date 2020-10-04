package com.github.skunk.oss.data;

import java.io.File;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.skunk.core.collectors.CollectorUtils;
import com.github.skunk.core.filter.PageFilter;
import com.github.skunk.data.SqlMapper;
import com.github.skunk.oss.config.OssConfigProperties;
import com.github.skunk.oss.data.domain.OssFile;

/**
 * @author walker
 * @since 2019年5月12日
 */
@Service
public class OssFileService {

    private static final String LIST_SQL = "select * from oss_file where 1=1 ";

    private static final String OSS_FILE_BY_ID_SQL = " select * from oss_file where file_id = #{fileId} ";

    private static final String UPDATE_OSS_FILE_STATUS_BY_ID_SQL = " update oss_file set `status`=?  where file_id=? ";
    private static final String DELETE_OSS_FILE_BY_ID_SQL = " delete from  oss_file where file_id= ? ";
    private static final String INSERT_SQL = "INSERT INTO `oss_file` (`file_id`, `name`, `oss_key`, `file_type`, `meta`, `status`,`file_md5`, `file_size`, `local_path`, `create_time`) VALUES (?,?,?,?,?,?,?,?,?,?) ";

    @Autowired
    OssConfigProperties configProperties;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    OssClientService ossService;

    @Autowired
    UploadFileEvent uploadFileEvent;

    @Autowired
    SqlMapper sqlMapper;

    /**
     * 通过异步方式上传文件到OSS
     *
     * @param ossFile
     */
    public OssFile asyncUploadFile(@NotNull OssFile ossFile) {
        insertRecode(ossFile);
        File localOssFile = new File(ossFile.getLocalPath());
        ossService.asyncUploadFile(localOssFile, ossFile.getOssKey(), uploadFileEvent, ossFile.getFileId());
        ossFile.setCdnPath(configProperties.getCdnPath() + (ossFile.getOssKey().startsWith("/") ? "" : "/") + ossFile.getOssKey());
        return ossFile;
    }

    /**
     * 通过同步方式上传文件到OSS
     *
     * @param ossFile
     */
    public OssFile syncUploadFile(@NotNull OssFile ossFile) {
        insertRecode(ossFile);
        File localOssFile = new File(ossFile.getLocalPath());
        ossService.syncUploadFile(localOssFile, ossFile.getOssKey());
        ossFile.setCdnPath(configProperties.getCdnPath() + (ossFile.getOssKey().startsWith("/") ? "" : "/") + ossFile.getOssKey());
        return ossFile;
    }

    /**
     * 在db记录oss file文件信息
     *
     * @param record
     */
    public void insertRecode(OssFile record) {
        record.setFileId(UUID.randomUUID().toString().replaceAll("-", ""));
        Object[] params = new Object[] { record.getFileId(), record.getName(), record.getOssKey(), record.getFileType(), record.getMeta(), record.getStatus(), record.getFileMD5(), record.getFileSize(), record.getLocalPath(), new Date() };
        int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.BIGINT, Types.VARCHAR, Types.DATE };
        jdbcTemplate.update(INSERT_SQL, params, types);
        record.setCdnPath(configProperties.getCdnPath() + (record.getOssKey().startsWith("/") ? "" : "/") + record.getOssKey());
    }

    /**
     * @param pageFilter
     * @return
     */
    public PageInfo<OssFile> getPageList(PageFilter pageFilter) {
        StringBuffer querySql = new StringBuffer(LIST_SQL);
        if (CollectorUtils.isNotEmpty(pageFilter.getParams())) {
            if (StringUtils.isNoneBlank(pageFilter.getParams().get("fileType").toString())) {
                querySql.append(" and file_type=#{fileType} ");
            }
            if (StringUtils.isNoneBlank(pageFilter.getParams().get("fileName").toString())) {
                querySql.append(" and name like concat('%',#{fileName},'%') ");
            }
        }
        PageHelper.startPage(pageFilter.getPageNo(), pageFilter.getPageSize(), pageFilter.getOrderBy());
        List<OssFile> ossFiles = sqlMapper.selectList(querySql.toString(), pageFilter.getParams(), OssFile.class);
        ossFiles.stream().filter(item -> StringUtils.isNoneBlank(item.getOssKey())).forEach(item -> item.setCdnPath(configProperties.getCdnPath() + (item.getOssKey().startsWith("/") ? "" : "/") + item.getOssKey()));
        return new PageInfo<>(ossFiles);
    }

    /**
     * @param fileId
     * @param status
     */
    public void updateStatus(String fileId, String status) {
        jdbcTemplate.update(UPDATE_OSS_FILE_STATUS_BY_ID_SQL, new Object[] { status, fileId });
    }

    /**
     * @param fileId
     * @return
     */
    public OssFile getOssFile(String fileId) {
        return sqlMapper.selectOne(OSS_FILE_BY_ID_SQL, fileId, OssFile.class);
    }

    /**
     * @param fileId
     */
    public void deleteOssFile(String fileId) {
        OssFile ossFile = getOssFile(fileId);
        ossService.asyncDeleteFile(ossFile.getOssKey(), uploadFileEvent, fileId);
    }

    /**
     * @param fileId
     */
    public void deleteById(String fileId) {
        jdbcTemplate.update(DELETE_OSS_FILE_BY_ID_SQL, new Object[] { fileId });
    }
}
