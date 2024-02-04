package com.newlecture.web.dao;

import com.newlecture.web.entity.BoardEntity;
import com.newlecture.web.entity.CommentEntity;
import com.newlecture.web.entity.NoteEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoteDao {

    List<NoteEntity> getAllNote();
    public List<NoteEntity> sendList(NoteEntity nEnt);
    public List<NoteEntity> recvList(NoteEntity nEnt);
    public NoteEntity findOneNote(NoteEntity nEnt);
    public int updateReadDate(NoteEntity nEnt);
    public int deleteRecv(NoteEntity nEnt);
    public int deleteSend(NoteEntity nEnt);
    public int insertNote(NoteEntity nEnt);
    public int countReadYN(NoteEntity nEnt);
    public int countRecv(NoteEntity nEnt);
    public int countSend(NoteEntity nEnt);



}
