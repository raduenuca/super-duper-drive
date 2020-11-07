package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty="fileId")
    int addFile(File file);

    @Select("SELECT * FROM FILES WHERE userId = #{userId}")
    List<File> getAllUserFiles(Integer userId);

    @Select("SELECT * FROM FILES WHERE filename=#{fileName} AND userId = #{userId}")
    File getUserFileByName(Integer userId, String fileName);

    @Select("SELECT * FROM FILES WHERE fileId=#{fileId} AND userId = #{userId}")
    File getUserFileById(Integer userId, Integer fileId);

    @Delete("DELETE FROM FILES WHERE fileId=#{fileId} AND userId = #{userId}")
    void deleteUserFileById(Integer userId, Integer fileId);
}
