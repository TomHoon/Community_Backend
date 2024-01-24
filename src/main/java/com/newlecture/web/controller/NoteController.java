
package com.newlecture.web.controller;

import com.newlecture.web.dao.NoteDao;
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

	@PostMapping("/getNoteById")
	public List<NoteEntity> getNoteById(@RequestBody NoteEntity nEnt) {
		return nDao.getNoteById(nEnt);
	}

}
