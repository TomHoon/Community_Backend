package com.newlecture.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newlecture.web.dao.MemberDao;
import com.newlecture.web.entity.FileEntity;
import com.newlecture.web.entity.MemberEntity;

@RestController
public class LoginController {
	
	@Autowired
	MemberDao mDao;
	
	
	@GetMapping("/getMemberAll")
	public List<MemberEntity> getMemberAll() {
		List<MemberEntity> list = mDao.getMemberAll();
		return list;
	}
	
	@PostMapping("/loginMember")
	public int loginMember(@RequestBody MemberEntity mEnt) {
		int result;
		try {
			result = mDao.loginMember(mEnt);
		} catch (Exception e) {
			System.out.println("e : " + e);
			result = -1;
			return result;
		}
		if (result > 0) return 1;
		else return 0;
		
	}
	
	// 회원가입
	@PostMapping("/joinMember")
	public int insertMember(@RequestPart MultipartFile mFile, @RequestPart String param) throws IllegalStateException, IOException {
		ObjectMapper mapper = new ObjectMapper();
        MemberEntity mEnt = mapper.readValue(param, MemberEntity.class);
        
        if (!mFile.isEmpty()) {
        	// 시간과 originalFilename으로 매핑 시켜서 src 주소를 만들어 낸다.
            Date date = new Date();
            StringBuilder sb = new StringBuilder();
            
           	 sb.append(date.getTime());
           	 sb.append(mFile.getOriginalFilename());

            if (!mFile.isEmpty()) {
//            	◆◆로컬
//        	    File dest = new File("C://images/" + sb.toString());
            	
            	//피시방 임시, c파일에다 넣으면 접근이 안돼서 정적폴더에 잠시 넣은경우임
//        	    File dest = new File("C://Users//Administrator//Downloads//CommunityProject//public/" + sb.toString());
//            	◆◆운영서버
            	File dest = new File("/gnsdl2846/tomcat/webapps/upload/" + sb.toString());
            	
            	// error throw 함
        	    mFile.transferTo(dest); 
        	    
        	    
            }
            FileEntity fEnt = new FileEntity();
            fEnt.setFile_name(sb.toString());
            // local
//            fEnt.setFile_path("C://images/" + sb.toString());
            
            // prod
            fEnt.setFile_path("/upload/" + sb.toString());
            
    		mDao.insertFile(fEnt);
        	
        	mEnt.setFile_idx(getFileData(fEnt).getFile_idx()); 
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
//    	    File dest = new File("C://images/" + sb.toString());
        	
//        	◆◆운영서버
        	File dest = new File("/gnsdl2846/tomcat/webapps/upload/" + sb.toString());
        	
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
}
