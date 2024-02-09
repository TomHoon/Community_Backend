package com.newlecture.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newlecture.web.dao.MemberDao;
import com.newlecture.web.entity.FileEntity;
import com.newlecture.web.entity.MemberEntity;
import com.newlecture.web.service.SecurityService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LoginController {
	
	@Autowired
	MemberDao mDao;
	
	@Autowired
	private SecurityService securityService;
	
	@ApiOperation(value = "모든 회원 정보 조회", notes = "")
	@GetMapping("/getMemberAll")
	public List<MemberEntity> getMemberAll() {
		List<MemberEntity> list = mDao.getMemberAll();
		return list;
	}
	
	@ApiOperation(value = "로그인", notes = "")
	@PostMapping("/loginMember")
	public Map<String, Object> loginMember(@RequestBody MemberEntity mEnt) {
		int result;
		Map<String, Object> map = new HashMap<>();
		try {
			result = mDao.loginMember(mEnt);
		} catch (Exception e) {
			System.out.println("e : " + e);
			result = -1;
			map.put("result", result);
			return map;
		}
		if (result > 0) {
	        String token = securityService.createToken(mEnt.getMember_pw(), 1000 * 60 * 60 * 1L);    // 24시간
	        map.put("token", token);
	        map.put("userid", mEnt.getMember_id());
			return map;
		} else {
			map.put("result", 0);
			return map;
		}
	}
	
	// 회원가입
	@ApiOperation(value = "회원가입", notes = "")
	@PostMapping("/joinMember")
	public int insertMember(@RequestPart(value="file",required = false) MultipartFile mFile, @RequestPart String param) throws IllegalStateException, IOException {
		ObjectMapper mapper = new ObjectMapper();
        MemberEntity mEnt = mapper.readValue(param, MemberEntity.class);
        
        if (mFile != null) {
        	// 시간과 originalFilename으로 매핑 시켜서 src 주소를 만들어 낸다.
            Date date = new Date();
            StringBuilder sb = new StringBuilder();
            
           	 sb.append(date.getTime());
           	 sb.append(mFile.getOriginalFilename());

           	if (mFile != null) {
//            	◆◆로컬
//            	File file = new File(".");
//            	File dest = new File(file.getAbsolutePath()+ "src/main/webapp/" + sb.toString());        
//            	File dest = new File("C:/Users/gnsdl/Documents/workspace-spring-tool-suite-4-4.16.0.RELEASE/CommunityProject-1/src/main/webapp/" + sb.toString());

            	
            	//피시방 임시, c파일에다 넣으면 접근이 안돼서 정적폴더에 잠시 넣은경우임
//        	    File dest = new File("C://Users//Administrator//Downloads//CommunityProject//public/" + sb.toString());
//            	◆◆운영서버
//            	File dest = new File("/gnsdl2846/tomcat/webapps/upload/" + sb.toString());
            	
//            	◆◆NAS서버            	
            	File dest = new File("/usr/local/tomcat/webapps/upload/" + sb.toString());

            	// error throw 함
        	    // 파일 생성되는 코드
        	    mFile.transferTo(dest); 
            }
            
            // 🎈 이미지 파일 DB 넣기 시작
            FileEntity fEnt = new FileEntity();
            fEnt.setFile_name(sb.toString());
            
            // 로컬
//            fEnt.setFile_path(sb.toString());
            
        	// 운영 & NAS
            fEnt.setFile_path("/upload/" + sb.toString());
            
    		mDao.insertFile(fEnt);
        	mEnt.setFile_idx(getFileData(fEnt).getFile_idx());
        	
        	// 🎈 이미지 파일 DB 넣기 끝
    	}
        
		int result;
		try {
			result = mDao.joinMember(mEnt);
		} catch (Exception e ) {
			result = -1;
			System.out.println("e : " + e);
		}
		return result;
	}
	
	public FileEntity insertFile(@RequestPart MultipartFile uploadFile) throws IllegalStateException, IOException {
        // 시간과 originalFilename으로 매핑 시켜서 src 주소를 만들어 낸다.
        Date date = new Date();
        StringBuilder sb = new StringBuilder();
        
       	 sb.append(date.getTime());
       	 sb.append(uploadFile.getOriginalFilename());

        if (!uploadFile.isEmpty()) {
//        	◆◆로컬
    	    File dest = new File("C://images/" + sb.toString());
        	
//        	◆◆nas
//        	File dest = new File("/upload/" + sb.toString());
        	
//        	◆◆운영서버
//        	File dest = new File("/gnsdl2846/tomcat/webapps/upload/" + sb.toString());
        	
        	// error throw 함
    	    uploadFile.transferTo(dest); 
    	    
    	    
        }
        FileEntity fEnt = new FileEntity();
        fEnt.setFile_name(sb.toString());
//        local
//        fEnt.setFile_path("C://images/" + sb.toString());
        
//        prod
        fEnt.setFile_path("/upload/" + sb.toString());
        
		mDao.insertFile(fEnt);
		return getFileData(fEnt);
	}
	
	public FileEntity getFileData(FileEntity fEnt) {
		return mDao.getFileData(fEnt);
	}
	
	@ApiOperation(value = "하나의 회원정보 조회", notes = "")
	@PostMapping("/getOneMember")
	public MemberEntity getOneMember(@RequestBody MemberEntity mEnt) {
		return mDao.getOneMember(mEnt);
	}
	
	@ApiOperation(value = "회원탈퇴", notes = "")
	@PostMapping("/joinOut")
	public String joinOut(@RequestBody MemberEntity mEnt) {
		if (mDao.joinOut(mEnt) == 1) {
			return "삭제완료";
		} else {
			return "삭제실패";
		}
	}
	
	@ApiOperation(value = "회원정보 업데이트", notes = "")
	@PostMapping("/memberUpdate")
	public String memberUpdate(@RequestPart(value="file",required = false) MultipartFile mFile, @RequestPart String param) throws IllegalStateException, IOException {
		ObjectMapper mapper = new ObjectMapper();
        MemberEntity mEnt = mapper.readValue(param, MemberEntity.class);
        if (mFile != null) {
        	// 시간과 originalFilename으로 매핑 시켜서 src 주소를 만들어 낸다.
            Date date = new Date();
            StringBuilder sb = new StringBuilder();
            
           	 sb.append(date.getTime());
           	 sb.append(mFile.getOriginalFilename());

             if (mFile != null) {
//            	◆◆로컬
//            	File file = new File(".");
//            	File dest = new File(file.getAbsolutePath()+ "src/main/webapp/" + sb.toString());        
//            	File dest = new File("C:/Users/gnsdl/Documents/workspace-spring-tool-suite-4-4.16.0.RELEASE/CommunityProject-1/src/main/webapp/" + sb.toString());

            	
            	//피시방 임시, c파일에다 넣으면 접근이 안돼서 정적폴더에 잠시 넣은경우임
//        	    File dest = new File("C://Users//Administrator//Downloads//CommunityProject//public/" + sb.toString());
//            	◆◆운영서버
//            	File dest = new File("/gnsdl2846/tomcat/webapps/upload/" + sb.toString());
            	
//            	◆◆NAS서버            	
            	File dest = new File("/usr/local/tomcat/webapps/upload/" + sb.toString());

            	// error throw 함
        	    // 파일 생성되는 코드
        	    mFile.transferTo(dest); 
            }
            
            // 🎈 이미지 파일 DB 넣기 시작
            FileEntity fEnt = new FileEntity();
            fEnt.setFile_name(sb.toString());
            
            // 로컬
//            fEnt.setFile_path(sb.toString());
            
        	// 운영 & NAS
            fEnt.setFile_path("/upload/" + sb.toString());
            
    		mDao.insertFile(fEnt);
        	mEnt.setFile_idx(getFileData(fEnt).getFile_idx());
        	
        	// 🎈 이미지 파일 DB 넣기 끝
    	}
        
		if (mDao.memberUpdate(mEnt) == 1) {
			return "수정완료";
		} else {
			return "수정실패";
		}
	}
	
	@ApiOperation(value = "특정 쪽지 조회", notes = "")
	@PostMapping("/findIdNote")
	public int findIdNote(@RequestBody MemberEntity mEnt) {
		return mDao.findIdNote(mEnt);
	}
	
	@ApiOperation(value = "로그아웃", notes = "")
	@PostMapping("/logout")
	public void logout(@RequestBody MemberEntity mEnt) {
		MemberEntity logout_member = mDao.getOneMember(mEnt);
		String _blacklist = logout_member.getToken_blacklist(); // check existed blacklist
		
		ArrayList<String> new_arrlist = new ArrayList<String>(); // 가공할 빈 arrayList 생성
		
		List<String> temp_blacklist = Arrays.asList(_blacklist.split(",")); // existed array list

		// fixed size List라서 for로 돌려서 가변list 사용
		for (int i = 0; i < temp_blacklist.size(); i ++ ) {
			new_arrlist.add(temp_blacklist.get(i)); // 가공할 array list
		}
		new_arrlist.add(mEnt.getToken_blacklist()); // logout 시킬 토큰 저장

		String joined_token_blacklist = String.join(",", new_arrlist); // arrayList -> String으로 변환
		
		// DB에 기존 + 만료시킬토큰 문자열을 저장 시작
		MemberEntity ent_for_update = new MemberEntity();
		ent_for_update.setToken_blacklist(joined_token_blacklist);
		ent_for_update.setMember_id(mEnt.getMember_id());

		mDao.updateTokenBlacklist(ent_for_update);
	}
	
	@ApiOperation(value = "토큰 유효성 조회", notes = "")
	@PostMapping("/checkToken")
	public String checkToken(@RequestBody Map<String, String> request) {
		String token = request.get("token");
		String id = request.get("id");
		
		log.info(">>> request token: " + token);
		log.info(">>> request id: " + id);
		
		// 아이디를 갖고서 비밀번호를 뽑기
		MemberEntity mEnt = new MemberEntity();
		mEnt.setMember_id(id);
		MemberEntity res_entity = mDao.getOneMember(mEnt);
		
		if (res_entity.getMember_id() == null) {
			log.info(">>> 일치하는 아이디가 없습니다");
			return "일치하는 아이디가 없습니다";
		}
		
		String req_password = securityService.getSubject(token); // 요청한 토큰의 패스워드값
		
		if (req_password.equals(res_entity.getMember_pw())) {
			MemberEntity ent_for_check_invalid_token = mDao.getOneMember(mEnt);
			String[] str_list = ent_for_check_invalid_token.getToken_blacklist().split(",");
			List<String> list = Arrays.asList(str_list);

			if (list.indexOf(token) > -1) {
				log.info(">>> 만료된 토큰입니다 ");
				return "만료된 토큰입니다.";
			}
			log.info(">>> 유효한 토큰입니다 ");
			return "유효한 토큰입니다";
		}

		log.info(">>> 아이디가 존재하지만 유효하지 않은 토큰입니다");
		return "아이디가 존재하지만 유효하지 않은 토큰입니다";
	}

}
