package com.skunk.oss.data;

import java.io.File;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.skunk.core.filter.PageFilter;
import com.skunk.core.filter.PageResult;
import com.skunk.core.utils.JdkUUIDGenerator;
import com.skunk.core.utils.Objects2;
import com.skunk.core.validation.HibernateValidatorUtils;
import com.skunk.data.PageHelperResult;
import com.skunk.data.SqlMapper;
import com.skunk.oss.OSSClientService;
import com.skunk.oss.data.domain.OSSFile;
import com.skunk.oss.OSSFileStatus;

/**
 * @author walker
 * @since 2019年5月12日
 */
@Service
public class OSSFileService {

    private static final String LIST_SQL = "select * from oss_file where 1=1 ";

    private static final String OSS_FILE_BY_ID_SQL = " select * from oss_file where file_id = #{fileId} ";

    private static final String UPDATE_OSS_FILE_STATUS_BY_ID_SQL = " update oss_file set `status`=?  where file_id=? ";

    private static final String DELETE_OSS_FILE_BY_ID_SQL = " delete from  oss_file where file_id= ? ";

    private static final String INSERT_SQL = "INSERT INTO `oss_file` (`file_id`, `name`, `oss_key`, `file_type`, `meta`, `status`,`file_md5`, `file_size`, `local_path`, `create_time`) VALUES (?,?,?,?,?,?,?,?,?,?) ";

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    OSSClientService ossService;
    @Autowired
    SqlMapper sqlMapper;

    /**
     * 通过异步方式上传文件到OSS
     *
     * @param file
     */
    public void asyncSaveFile(@NotNull OSSFile file) {

        Objects.requireNonNull(file, "file is null.");
        HibernateValidatorUtils.validate(file);

        saveRecode(file);
        File localOssFile = new File(file.getLocalPath());
        ossService.asyncUploadFile(localOssFile, file.getOssKey(), file.getFileId());
    }

    /**
     * 通过同步方式上传文件到OSS
     *
     * @param ossFile
     */
    public void syncSaveFile(@NotNull OSSFile ossFile) {
        Objects.requireNonNull(ossFile, "file is null.");
        HibernateValidatorUtils.validate(ossFile);

        saveRecode(ossFile);
        File localOssFile = new File(ossFile.getLocalPath());
        ossService.syncUploadFile(localOssFile, ossFile.getOssKey());
    }

    /**
     * 在db记录oss file文件信息
     *
     * @param record
     */
    public void saveRecode(OSSFile record) {
        record.setFileId(JdkUUIDGenerator.generateRandom8());
        Object[] params = new Object[] { record.getFileId(), record.getName(), record.getOssKey(), record.getFileType(), record.getMeta(), record.getStatus(),
            record.getFileMD5(), record.getFileSize(), record.getLocalPath(), new Date() };
        int[] types = new int[] {
            Types.VARCHAR,
            Types.VARCHAR,
            Types.VARCHAR,
            Types.VARCHAR,
            Types.VARCHAR,
            Types.VARCHAR,
            Types.VARCHAR,
            Types.BIGINT,
            Types.VARCHAR,
            Types.DATE };
        jdbcTemplate.update(INSERT_SQL, params, types);
    }

    /**
     * @param pageFilter
     * @return
     */
    public PageResult<OSSFile> getPageList(PageFilter pageFilter) {
        StringBuffer querySql = new StringBuffer(LIST_SQL);

        pageFilter.getParamValueToString("fileType").ifPresent(value -> {
            querySql.append(" and file_type=#{fileType} ");
        });

        pageFilter.getParamValueToString("fileName").ifPresent(value -> {
            querySql.append(" and name like concat('%',#{fileName},'%') ");
        });

        PageHelper.startPage(pageFilter.getPageNo(), pageFilter.getPageSize(), pageFilter.getOrderBy().orElse(""));
        List<OSSFile> ossFiles = sqlMapper.selectList(querySql.toString(), pageFilter.getParams(), OSSFile.class);
        return new PageHelperResult(ossFiles);
    }

    /**
     * @param fileId
     * @param status
     */
    public void updateStatus(@NotBlank String fileId, @NotNull OSSFileStatus status) {
        Objects2.requireNotBlank(fileId);
        Objects.requireNonNull(status);

        jdbcTemplate.update(UPDATE_OSS_FILE_STATUS_BY_ID_SQL, new Object[] { status.getCode(), fileId });
    }

    /**
     * @param fileId
     * @return
     */
    public OSSFile getOssFile(@NotBlank String fileId) {
        Objects2.requireNotBlank(fileId);

        return sqlMapper.selectOne(OSS_FILE_BY_ID_SQL, fileId, OSSFile.class);
    }

    /**
     * @param fileId
     */
    public void deleteOssFile(@NotBlank String fileId) {

        Objects2.requireNotBlank(fileId);

        OSSFile ossFile = getOssFile(fileId);
        ossService.asyncDeleteFile(ossFile.getOssKey(), fileId);
    }

    /**
     * @param fileId
     */
    public void deleteById(@NotBlank String fileId) {
        Objects2.requireNotBlank(fileId);

        jdbcTemplate.update(DELETE_OSS_FILE_BY_ID_SQL, new Object[] { fileId });
    }
}
