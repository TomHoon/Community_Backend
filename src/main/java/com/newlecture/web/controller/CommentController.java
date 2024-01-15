package com.newlecture.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.newlecture.web.dao.CommentDao;
import com.newlecture.web.dao.MemberDao;
import com.newlecture.web.entity.CommentEntity;
import com.newlecture.web.entity.MemberEntity;

@RestController
public class CommentController {
	
	@Autowired
	CommentDao cDao;
	
	@Autowired
	MemberDao mDao;
	
	@GetMapping("/getAllComment")
	public List<CommentEntity> getAllComment() {
		return cDao.getAllComment();
	}
	
	@PostMapping("/getCommentByBoard")
	public List<CommentEntity> getCommentByBoard(@RequestBody CommentEntity cEnt) {
		return cDao.getCommentByBoard(cEnt);
	}
	
	@PostMapping("/addComment")
	public int addComment(@RequestBody CommentEntity cEnt) {
		return cDao.addComment(cEnt);
	}
	
	@PostMapping("/recommendUpDown")
	public int recommendUpDown(@RequestBody CommentEntity cEnt) {
		
		
		// TODO: 내가 쓴 글 upDown 못하게 막기
		String comment_idx = cEnt.getComment_idx();
		
		if (cEnt.getComment_recommend() == null) {
			// 비추천누른 경우
			
			// 기존에 updown한 리스트에 아이디를 삭제하기
			String downId = cEnt.getComment_ud_temp_id();
			CommentEntity findComment = findOneComment(cEnt);
			
			String list = findComment.getComment_down_id_list();
			
			if (list != null && list.contains(downId)) {
				cEnt.setComment_unrecommend("-1");
				cEnt.setComment_recommend(null);
				cDao.recommendUpDown(cEnt);
				list = list.replace("," + downId, "");
				findComment.setComment_down_id_list(list);
				findComment.setComment_up_id_list(null);
				changeUpList(findComment);
				return 1;
			}

			list = list + "," + downId;
			
			findComment.setComment_down_id_list(list);
			findComment.setComment_up_id_list(null);
			changeUpList(findComment);
			
			findComment.setComment_recommend(null);
			findComment.setComment_unrecommend("1");
			cDao.recommendUpDown(findComment);
			return -1;
		} else {
			// 추천누른 경우
			String upId = cEnt.getComment_ud_temp_id();
			CommentEntity findComment = findOneComment(cEnt);
			
			String list = findComment.getComment_up_id_list();
			if (list != null && list.contains(upId)) {
				cEnt.setComment_recommend("-1");
				cDao.recommendUpDown(cEnt);
				list = list.replace((","+upId), "");
				findComment.setComment_up_id_list(list);
				changeUpList(findComment);
				return 2;
			}
			
			list = list + "," + upId;
			
			findComment.setComment_up_id_list(list);
			
			changeUpList(findComment);
			
			cDao.recommendUpDown(cEnt);
			return -1;
		}
		
	}
	
	@PostMapping("/deleteComment")
	public int deleteComment(@RequestBody CommentEntity cEnt) {
		return cDao.deleteComment(cEnt);
	}
	
	@PostMapping("/findOneComment")
	public CommentEntity findOneComment(@RequestBody CommentEntity cEnt) {
		return cDao.findOneComment(cEnt);
	}
	
	@PostMapping("/changeUpList")
	public int changeUpList(@RequestBody CommentEntity cEnt) {
		return cDao.changeUpList(cEnt);
	}
}
