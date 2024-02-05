package com.newlecture.web.controller;

import java.sql.Array;
import java.util.*;

import com.newlecture.web.entity.BoardEntity;
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

		// 비추천누른 경우
		if (cEnt.getComment_recommend() == null) {
			// 기존에 updown한 리스트에 아이디를 삭제하기
			String downId = cEnt.getComment_ud_temp_id();
			
			CommentEntity findComment = findOneComment(cEnt);
			
			String up_list = findComment.getComment_up_id_list();
			String down_list = findComment.getComment_down_id_list();
			
			String[] up_temp_arr = up_list.split(",");
			String[] down_temp_arr = down_list.split(",");
			
			ArrayList<String> up_arr_list = new ArrayList<String>(Arrays.asList(up_temp_arr));
			ArrayList<String> down_arr_list = new ArrayList<String>(Arrays.asList(down_temp_arr));
			
			String list = findComment.getComment_down_id_list();
			
			// 추천을 클릭해놓은 상태라면 지운다.
			if (up_list != null && up_arr_list.contains(downId)) { // 추천이 눌려있는 경우

				CommentEntity tempCent = new CommentEntity();
				tempCent.setComment_idx(comment_idx);
				tempCent.setComment_recommend("-1");
				cDao.recommendUpDown(tempCent);

				for (int i = 0; i < up_arr_list.size(); i++) {
					if (up_arr_list.get(i).equals(downId)) {
						up_arr_list.remove(i);
					}
				}
				
				up_list = String.join(",", up_arr_list);

				findComment.setComment_up_id_list(up_list);
				findComment.setComment_down_id_list(null);
				cDao.recommendUpDown(cEnt);
				changeUpList(findComment);
				
				down_list = "," + downId; 
				findComment.setComment_up_id_list(null);
				findComment.setComment_down_id_list(down_list);
				changeUpList(findComment);
				return 1;
			}


			if (down_list != null && down_arr_list.contains(downId)) { // 비추천이 눌려있는 경우
				cEnt.setComment_unrecommend("-1");
				cEnt.setComment_recommend(null);
				cDao.recommendUpDown(cEnt);
				
				for(int i = 0; i < down_arr_list.size(); i++) {
					if (down_arr_list.get(i).equals(downId)) {
						down_arr_list.remove(i);
					}
				}
				
				down_list = String.join(",", down_arr_list);
				findComment.setComment_down_id_list(down_list);
				findComment.setComment_up_id_list(null);
				changeUpList(findComment);
				return 2;
			}
			
			down_list = "," + downId; 
			findComment.setComment_up_id_list(null);
			findComment.setComment_down_id_list(down_list);
			cDao.recommendUpDown(cEnt);
			changeUpList(findComment);
			return -1;

		} else {
			// 추천누른 경우
			String upId = cEnt.getComment_ud_temp_id();
			CommentEntity findComment = findOneComment(cEnt);

			String down_list = findComment.getComment_down_id_list();
			String up_list = findComment.getComment_up_id_list();
			
			ArrayList<String> up_arr_list = new ArrayList<String>(Arrays.asList(up_list.split(",")));
			ArrayList<String> down_arr_list = new ArrayList<String>(Arrays.asList(down_list.split(",")));
			
			// 추천을 눌렀을 때, 비추천을 이미 누른상태이면 비추천을 삭제 후 추천을 올려야함
			if (down_list != null && down_arr_list.contains(upId)) {
				CommentEntity resetDownEnt = new CommentEntity();
				resetDownEnt.setComment_idx(comment_idx);
				resetDownEnt.setComment_unrecommend("-1");
				cDao.recommendUpDown(resetDownEnt);
				
				for(int i = 0; i < down_arr_list.size(); i++) {
					if (down_arr_list.get(i).equals(upId)) {
						down_arr_list.remove(i);
					}
				}
				down_list = String.join(",", down_arr_list);
				findComment.setComment_down_id_list(down_list);
				findComment.setComment_up_id_list(null);
				changeUpList(findComment);
				cDao.recommendUpDown(cEnt);
				
				up_list = "," + upId; 
				findComment.setComment_up_id_list(up_list);
				findComment.setComment_down_id_list(null);
				changeUpList(findComment);
				return 3;
			}

			// 추천을 눌렀을 때, 추천을 이미 누른상태이면 추천을 해제해야함
			if (up_arr_list != null && up_arr_list.contains(upId)) {
				CommentEntity resetDownEnt = new CommentEntity();
				resetDownEnt.setComment_idx(comment_idx);
				resetDownEnt.setComment_recommend("-1");
				cDao.recommendUpDown(resetDownEnt);
				
				for(int i = 0; i < up_arr_list.size(); i++) {
					if (up_arr_list.get(i).equals(upId)) {
						up_arr_list.remove(i);
					}
				}
				String list = String.join(",", up_arr_list);
				
				findComment.setComment_up_id_list(list);
				findComment.setComment_down_id_list(null);
				
				changeUpList(findComment);
				return 4;
			}
			
			up_list = "," + upId; 
			findComment.setComment_up_id_list(up_list);
			findComment.setComment_down_id_list(null);
			cDao.recommendUpDown(cEnt);
			changeUpList(findComment);
			return -2;
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
