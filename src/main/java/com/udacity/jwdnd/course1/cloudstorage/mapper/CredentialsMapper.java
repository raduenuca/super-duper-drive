package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialsMapper {
    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES(#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty="credentialId")
    int addCredential(Credential credential);

    @Update("UPDATE CREDENTIALS SET url=#{url}, username=#{username}, password=#{password} WHERE userid=#{userId} AND credentialid=#{credentialId}")
    void updateCredential(Credential credential);

    @Select("SELECT * FROM CREDENTIALS WHERE userId = #{userId}")
    List<Credential> getAllUserCredentials(Integer userId);

    @Select("SELECT * FROM CREDENTIALS WHERE userId = #{userId} AND credentialid=#{credentialId}")
    Credential getUserCredentialById(Integer userId, Integer credentialId);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid=#{credentialId} AND userId = #{userId}")
    void deleteUserCredentialById(Integer userId, Integer credentialId);
}

