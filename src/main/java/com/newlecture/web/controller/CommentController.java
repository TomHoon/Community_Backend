package com.newlecture.web.controller;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
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
			// 이미 비추천을 눌렀다.


			// 기존에 updown한 리스트에 아이디를 삭제하기
			String downId = cEnt.getComment_ud_temp_id();
			CommentEntity findComment = findOneComment(cEnt);

			String up_list = findComment.getComment_up_id_list();

			// 추천을 클릭해놓은 상태라면 지운다.
			if (up_list != null && up_list.contains(downId)) {

				CommentEntity tempCent = new CommentEntity();
				tempCent.setComment_idx(comment_idx);
				tempCent.setComment_recommend("-1");
				cDao.recommendUpDown(tempCent);
				/*if(up_list.contains(downId)) {
						up_list = up_list.replace("," + downId, "");
				}*/
				ArrayList<String> arr = new ArrayList<>();
				arr.add(downId);

				for(int i = 0; i < arr.size(); i++){
					String str = arr.get(i);
					if(str.equals(downId)){
						arr.remove(downId);

					}
					up_list = String.join(",",arr);

					/*if(up_list.contains(downId)){
						up_list = up_list.replace( downId,  "");
					}*/

					System.out.println("1arr = " + arr);
					System.out.println("1str = " + str);
					System.out.println("1str.equals(downId) = " + str.equals(downId));
					System.out.println("1arr.remove(downId)= " + arr.remove(downId));
					System.out.println("1up_list = " + up_list);
					System.out.println("1downId = " + downId);
					System.out.println("1up_list.split(downId) = " + up_list.split(downId));
					System.out.println("1up_list.contains(downId) = " + up_list.contains(downId));
					System.out.println("1downId.contains(up_list) = " + downId.contains(up_list));
					System.out.println("1up_list.equals(downId) = " + up_list.equals(downId));
					System.out.println("1downId.equals(up_list) = " + downId.equals(up_list));
				}

				System.out.println("Arrays.toString(up_list.split(downId)" + Arrays.toString(up_list.split(downId)));
				findComment.setComment_up_id_list(up_list);
				findComment.setComment_down_id_list(null);
				changeUpList(findComment);
			}

			String list = findComment.getComment_down_id_list();

			if (list != null && list.contains(downId)) {
				cEnt.setComment_unrecommend("-1");
				cEnt.setComment_recommend(null);
				cDao.recommendUpDown(cEnt);
				ArrayList<String> arr = new ArrayList<>();
				arr.add(downId);
				for(int i = 0; i < arr.size(); i++) {
					String str = arr.get(i);
					if (str.equals(downId)) {
						arr.remove(downId);
					}
					list = String.join(",", arr);
					System.out.println("2arr = " + arr);
					System.out.println("2str = " + str);
					System.out.println("2str.equals(downId) = " + str.equals(downId));
					System.out.println("2arr.remove(downId)= " + arr.remove(downId));
					System.out.println("2up_list = " + up_list);
					System.out.println("2downId = " + downId);
					System.out.println("2up_list.split(downId) = " + up_list.split(downId));
					System.out.println("2up_list.contains(downId) = " + up_list.contains(downId));
					System.out.println("2downId.contains(up_list) = " + downId.contains(up_list));
					System.out.println("up_list.equals(downId) = " + up_list.equals(downId));
					System.out.println("21downId.equals(up_list) = " + downId.equals(up_list));
				}
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

			String down_list = findComment.getComment_down_id_list();

			// 추천을 눌렀으나 비추천 누른게 있다면 지운다.
			if (down_list != null && down_list.contains(upId)) {
				CommentEntity resetDownEnt = new CommentEntity();
				resetDownEnt.setComment_idx(comment_idx);
				resetDownEnt.setComment_unrecommend("-1");
				cDao.recommendUpDown(resetDownEnt);
				ArrayList<String> arr = new ArrayList<>();
				arr.add(upId);
				for(int i = 0; i < arr.size(); i++) {
					String str = arr.get(i);
					if (str.equals(upId)) {
						arr.remove(upId);
					}
					down_list = String.join(",", arr);
					System.out.println("3arr = " + arr);
					System.out.println("3str = " + str);
					System.out.println("3str.equals(downId) = " + str.equals(upId));
					System.out.println("3arr.remove(downId)= " + arr.remove(upId));
					System.out.println("3up_list = " + down_list);
					System.out.println("3downId = " + upId);
					System.out.println("3up_list.split(downId) = " + down_list.split(upId));
					System.out.println("3up_list.contains(downId) = " + down_list.contains(upId));
					System.out.println("3downId.contains(up_list) = " + upId.contains(down_list));
					System.out.println("3up_list.equals(downId) = " + down_list.equals(upId));
					System.out.println("3downId.equals(up_list) = " + upId.equals(down_list));
				}
				findComment.setComment_down_id_list(down_list);
				findComment.setComment_up_id_list(null);
				changeUpList(findComment);
			}

			String list = findComment.getComment_up_id_list();

			if (list != null && list.contains(upId)) {
				cEnt.setComment_recommend("-1");
				cDao.recommendUpDown(cEnt);
				ArrayList<String> arr = new ArrayList<>();
				arr.add(upId);
				for(int i = 0; i < arr.size(); i++) {
					String str = arr.get(i);
					if (str.equals(upId)) {
						arr.remove(upId);
					}
					list = String.join(",", arr);
					down_list = String.join(",", arr);
					System.out.println("4arr = " + arr);
					System.out.println("4str = " + str);
					System.out.println("4str.equals(downId) = " + str.equals(upId));
					System.out.println("4arr.remove(downId)= " + arr.remove(upId));
					System.out.println("4up_list = " + down_list);
					System.out.println("4downId = " + upId);
					System.out.println("4up_list.split(downId) = " + down_list.split(upId));
					System.out.println("4up_list.contains(downId) = " + down_list.contains(upId));
					System.out.println("4downId.contains(up_list) = " + upId.contains(down_list));
					System.out.println("4up_list.equals(downId) = " + down_list.equals(upId));
					System.out.println("4downId.equals(up_list) = " + upId.equals(down_list));
				}
				findComment.setComment_up_id_list(list);
				findComment.setComment_down_id_list(null);
				changeUpList(findComment);
				return 2;
			}

			list = list + "," + upId;

			findComment.setComment_up_id_list(list);
			findComment.setComment_down_id_list(null);

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
