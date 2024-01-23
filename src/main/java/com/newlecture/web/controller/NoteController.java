
package com.newlecture.web.controller;

import com.newlecture.web.dao.NoteDao;
import com.newlecture.web.entity.BoardEntity;
import com.newlecture.web.entity.CommentEntity;
import com.newlecture.web.entity.NoteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NoteController {

	@Autowired
	NoteDao nDao;

	@GetMapping("/getAllNote")
	public List<NoteEntity> getAllNote() {
		return nDao.getAllNote();
	}

	@PostMapping("/getNoteById")
	public List<NoteEntity> NoteEntity(@RequestBody NoteEntity nEnt) {
		List<NoteEntity> bEnt = nDao.getNoteById(nEnt);
		return bEnt;
	}


}
