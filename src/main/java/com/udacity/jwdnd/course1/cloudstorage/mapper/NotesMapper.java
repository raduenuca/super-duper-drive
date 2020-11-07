package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotesMapper {
    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty="noteId")
    int addNote(Note note);

    @Update("UPDATE NOTES SET notetitle=#{noteTitle}, notedescription=#{noteDescription} WHERE userid=#{userId} AND noteid=#{noteId}")
    void updateNote(Note note);

    @Select("SELECT * FROM NOTES WHERE userId = #{userId}")
    List<Note> getAllUserNotes(Integer userId);

    @Select("SELECT * FROM NOTES WHERE userId = #{userId} AND noteId=#{noteId}")
    Note getUserNoteById(Integer userId, Integer noteId);

    @Delete("DELETE FROM NOTES WHERE noteId=#{noteId} AND userId = #{userId}")
    void deleteUserNoteById(Integer userId, Integer noteId);
}
