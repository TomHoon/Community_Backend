
package com.newlecture.web.controller;

import com.newlecture.web.dao.NoteDao;
import com.newlecture.web.entity.CommentEntity;
import com.newlecture.web.entity.NoteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
public class NoteController {

	@Autowired
	NoteDao nDao;

	@GetMapping("/getAllNote")
	public List<NoteEntity> getAllNote() {
		return nDao.getAllNote();
	}

	@PostMapping("/sendList")
	public List<NoteEntity> sendList(@RequestBody NoteEntity nEnt) {
		return nDao.sendList(nEnt);
	}
	@PostMapping("/recvList")
	public List<NoteEntity> recvList(@RequestBody NoteEntity nEnt) {
		return nDao.recvList(nEnt);
	}

	@PostMapping("/findOneNote")
	public NoteEntity findOneNote(@RequestBody NoteEntity nEnt) {
		return nDao.findOneNote(nEnt);
	}

	@PostMapping("/updateReadDate")
	public int updateReadDate(@RequestBody NoteEntity nEnt) {
		return nDao.updateReadDate(nEnt);
	}

	@PostMapping("/deleteRecv")
	public int deleteRecv(@RequestBody NoteEntity nEnt) {
		return nDao.deleteRecv(nEnt);
	}

	@PostMapping("/deleteSend")
	public int deleteSend(@RequestBody NoteEntity nEnt) {
		return nDao.deleteSend(nEnt);
	}

	@PostMapping("/insertNote")
	public int insertNote(@RequestBody NoteEntity nEnt) {
		return nDao.insertNote(nEnt);
	}
	@PostMapping("/countReadYN")
	public int countReadYN(@RequestBody NoteEntity nEnt) {
		return nDao.countReadYN(nEnt);
	}
	public int countRecv(@RequestBody NoteEntity nEnt) {
		return nDao.countRecv(nEnt);
	}
	public int countSend(@RequestBody NoteEntity nEnt) {
		return nDao.countSend(nEnt);
	}

}
