package com.newlecture.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.newlecture.web.entity.NoteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newlecture.web.dao.MemberDao;
import com.newlecture.web.entity.FileEntity;
import com.newlecture.web.entity.MemberEntity;
import com.newlecture.web.service.SecurityService;

@RestController
public class LoginController {
	
	@Autowired
	MemberDao mDao;
	
	@Autowired
	private SecurityService securityService;
	
	@GetMapping("/getMemberAll")
	public List<MemberEntity> getMemberAll() {
		List<MemberEntity> list = mDao.getMemberAll();
		return list;
	}
	
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
	        String token = securityService.createToken(mEnt.getMember_pw(), 1000 * 60 * 60 * 24L);    // 24시간
	        map.put("token", token);
	        map.put("userid", mEnt.getMember_id());
			return map;
		} else {
			map.put("result", 0);
			return map;
		}
	}
	
	// 회원가입
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
	
	@PostMapping("/getOneMember")
	public MemberEntity getOneMember(@RequestBody MemberEntity mEnt) {
		return mDao.getOneMember(mEnt);
	}
	
	@PostMapping("/joinOut")
	public String joinOut(@RequestBody MemberEntity mEnt) {
		if (mDao.joinOut(mEnt) == 1) {
			return "삭제완료";
		} else {
			return "삭제실패";
		}
	}
	
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
	@PostMapping("/findIdNote")
	public int findIdNote(@RequestBody MemberEntity mEnt) {
		return mDao.findIdNote(mEnt);
	}

}
