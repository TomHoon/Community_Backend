package com.newlecture.web.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.newlecture.web.service.EncryptService;
import com.newlecture.web.service.S3UploadService;
import com.newlecture.web.service.SecurityService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LoginController {
	
//    @Autowired
//    private AmazonS3Client amazonS3Client;
    
    @Autowired
    S3UploadService s3Svc;
    
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    
	@Autowired
	MemberDao mDao;
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private EncryptService encService;
	
	@GetMapping("/getMemberAll")
	public List<MemberEntity> getMemberAll() {
		List<MemberEntity> list = mDao.getMemberAll();
		return list;
	}
	
	@PostMapping("/loginMember")
	public Map<String, Object> loginMember(@RequestBody MemberEntity mEnt) {
		int result;
		Map<String, Object> map = new HashMap<>();
		
		String pw = mEnt.getMember_pw();
		String salt = "tomhoon";
		String encrypted = encService.getEncrypt(pw, salt);
		
		mEnt.setMember_pw(encrypted);
		try {
			result = mDao.loginMember(mEnt);
		} catch (Exception e) {
			System.out.println("e : " + e);
			result = -1;
			map.put("result", result);
			return map;
		}
		if (result > 0) {
			// 1second = 1000 ms
			// 1 minute = 60 second = 1000 * 60
			// 30 minute = 1000 * 30
			// 1 hour = 60 minute = 60 second * 60 = 1000* 60 * 60
	        String token = securityService.createToken(mEnt.getMember_pw(), 1000 * 30);    // 30 minute
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
	public int insertMember(@RequestPart(required = false) MultipartFile mFile, @RequestPart String param) throws IllegalStateException, IOException {
		ObjectMapper mapper = new ObjectMapper();
        MemberEntity mEnt = mapper.readValue(param, MemberEntity.class);
        
        // 비밀번호 난수화 처리시작
        String pw = mEnt.getMember_pw();
//        String salt = encService.getSalt();
        String salt = "tomhoon";
        
        String encrypted = encService.getEncrypt(pw, salt);
        mEnt.setMember_pw(encrypted);
        // 비밀번호 난수화 처리끝

        if (mFile != null) {
//        	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmm");
//        	 Date now = new Date();         
//        	 String nowTime1 = sdf1.format(now);
        	
        	String fileURL = s3Svc.saveFile(mFile);
        	FileEntity fEnt = new FileEntity();
        	fEnt.setFile_name(mFile.getOriginalFilename());
        	fEnt.setFile_path(fileURL);
        	mDao.insertFile(fEnt);
        	
        	String input_fileIdx = getFileData(fEnt).getFile_idx();
        	
        	mEnt.setFile_idx(input_fileIdx);
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
	
//	public FileEntity insertFile(@RequestPart MultipartFile uploadFile) throws IllegalStateException, IOException {
//        // 시간과 originalFilename으로 매핑 시켜서 src 주소를 만들어 낸다.
//        Date date = new Date();
//        StringBuilder sb = new StringBuilder();
//        
//       	 sb.append(date.getTime());
//       	 sb.append(uploadFile.getOriginalFilename());
//
//        if (!uploadFile.isEmpty()) {
////        	◆◆로컬
////    	    File dest = new File("C://images/" + sb.toString());
//        	
////        	◆◆nas
////        	File dest = new File("/upload/" + sb.toString());
//        	
////        	◆◆운영서버
////        	File dest = new File("/gnsdl2846/tomcat/webapps/upload/" + sb.toString());
//        	
//        	// error throw 함
////    	    uploadFile.transferTo(dest); 
//    	    
//    	    
//        }
//        FileEntity fEnt = new FileEntity();
//        fEnt.setFile_name(sb.toString());
////        local
////        fEnt.setFile_path("C://images/" + sb.toString());
//        
////        prod
//        fEnt.setFile_path("/upload/" + sb.toString());
//        
//		mDao.insertFile(fEnt);
//		return getFileData(fEnt);
//	}
	
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
	public String memberUpdate(@RequestPart(required = false) MultipartFile mFile, @RequestPart String param) throws IllegalStateException, IOException {
		ObjectMapper mapper = new ObjectMapper();
        MemberEntity mEnt = mapper.readValue(param, MemberEntity.class);
        if (mFile != null) {
        	// 시간과 originalFilename으로 매핑 시켜서 src 주소를 만들어 낸다.
            Date date = new Date();
            StringBuilder sb = new StringBuilder();
            
           	 sb.append(date.getTime());
           	 sb.append(mFile.getOriginalFilename());
           	 
           	String fileURL = null;
             if (mFile != null) {
            	 fileURL = s3Svc.saveFile(mFile);
            }
            
            // 🎈 이미지 파일 DB 넣기 시작
            FileEntity fEnt = new FileEntity();
            fEnt.setFile_name(mFile.getOriginalFilename());
            
            // 로컬
//            fEnt.setFile_path(sb.toString());
            
        	// 운영 & NAS
            fEnt.setFile_path(fileURL);
            
    		mDao.insertFile(fEnt);
        	mEnt.setFile_idx(getFileData(fEnt).getFile_idx());
        	
        	// 🎈 이미지 파일 DB 넣기 끝
    	}
        
		String pw = mEnt.getMember_pw();
		String salt = "tomhoon";
		String encrypted = encService.getEncrypt(pw, salt);
		String token = securityService.createToken(mEnt.getMember_pw(), 1000 * 30);    // 30 minute
		
		mEnt.setMember_pw(encrypted);
		
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
		
		String req_password = null;
		
		try {
			req_password = securityService.getSubject(token); // 요청한 토큰의 패스워드값
		} catch(Exception e )  {
			return "시간이 경과하여 만료된 토큰입니다";
		}
		
		if (req_password.equals(res_entity.getMember_pw())) {
			MemberEntity ent_for_check_invalid_token = mDao.getOneMember(mEnt);
			if (ent_for_check_invalid_token.getToken_blacklist() != null) {
				String[] str_list = ent_for_check_invalid_token.getToken_blacklist().split(",");
				List<String> list = Arrays.asList(str_list);
				if (list.indexOf(token) > -1) {
					log.info(">>> 만료된 토큰입니다 ");
					return "만료된 토큰입니다.";
				}
			}

			log.info(">>> 유효한 토큰입니다 ");
			return "유효한 토큰입니다";
		}

		log.info(">>> 아이디가 존재하지만 유효하지 않은 토큰입니다");
		return "아이디가 존재하지만 유효하지 않은 토큰입니다";
	}
	
    /**
     * S3로 업로드
     * @param uploadFile : 업로드할 파일
     * @param fileName : 업로드할 파일 이름
     * @return 업로드 경로
     * @throws JsonProcessingException 
     * @throws JsonMappingException 
     */
//    public String putS3(File uploadFile, String fileName) throws JsonMappingException, JsonProcessingException {
//        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(
//                CannedAccessControlList.PublicRead));
//        return amazonS3Client.getUrl(bucket, fileName).toString();
//    }
//        
//    /**
//     * S3에 있는 파일 삭제
//     * 영어 파일만 삭제 가능 -> 한글 이름 파일은 안됨
//     */
//    public void deleteS3(String filePath) throws Exception {
//        try{
//            String key = filePath.substring(56); // 폴더/파일.확장자
//
//            try {
//                amazonS3Client.deleteObject(bucket, key);
//            } catch (AmazonServiceException e) {
//                log.info(e.getErrorMessage());
//            }
//
//        } catch (Exception exception) {
//            log.info(exception.getMessage());
//        }
//        log.info("[S3Uploader] : S3에 있는 파일 삭제");
//    }
//    
}
