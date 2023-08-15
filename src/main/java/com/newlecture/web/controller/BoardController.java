
package com.newlecture.web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newlecture.web.dao.BoardDao;
import com.newlecture.web.entity.BoardEntity;

@RestController
public class BoardController {
	
	@Autowired
	BoardDao bDao;
	
	@PostMapping("/getBoardAll")
	public List<BoardEntity> getBoardAll(@RequestBody BoardEntity bEntity) {
		if (bEntity.getOrder().isEmpty()) {
			bEntity.setOrder("0");
		}
		
		List<BoardEntity> list = bDao.getBoardAll(bEntity);
		return list;
	}
	
//	@PostMapping("/addBoard")
//	public int addBoard(@RequestBody BoardEntity bEntity) {
//		int result = bDao.addBoard(bEntity);
//		return result;
//	}
	
	@PostMapping("/searchBoard")
	public List<BoardEntity> searchBoard(@RequestBody BoardEntity bEntity) {
		List<BoardEntity> list = bDao.searchBoard(bEntity);
		return list;
	}
	
	@PostMapping("/getBoardById")
	public BoardEntity getBoardById(@RequestBody BoardEntity bEntity) {
		BoardEntity bEnt = bDao.getBoardById(bEntity);
		return bEnt;
	}
	
	@PostMapping("/updateBoard")
	public int updateBoard(@RequestBody BoardEntity bEntity) {
		int result = bDao.updateBoard(bEntity);
		return result;
	}
	
	@PostMapping("/updateHitBoard")
	public int updateHitBoard(@RequestBody BoardEntity bEntity) {
		int result = bDao.updateHitBoard(bEntity);
		return result;
	}
	
	@PostMapping("/deleteBoard")
	public int deleteBoard(@RequestBody BoardEntity bEntity) {
		int result = bDao.deleteBoard(bEntity);
		return result;
	}
	
	@PostMapping("/updateRecommendBoard")
	public int updateRecommendBoard(@RequestBody BoardEntity bEntity) {
		int result = bDao.updateRecommendHitBoard(bEntity);
		return result;
	}
	@PostMapping("/tempImg")
	public String tempImg(@RequestParam MultipartFile mFile) throws IllegalStateException, IOException {
		// 시간과 originalFilename으로 매핑 시켜서 src 주소를 만들어 낸다.
        Date date = new Date();
        StringBuilder sb = new StringBuilder();
        
     // file image 가 없을 경우
        if (mFile.isEmpty()) {
       	 sb.append("none");
       	 return "none";
        } else {
       	 sb.append(date.getTime());
       	 sb.append(mFile.getOriginalFilename());
        }

        if (!mFile.isEmpty()) {
//        	◆◆로컬
//    	    File dest = new File("C://images/" + sb.toString());
//        	File dest = new File("C://Users//gnsdl//Desktop//test//public/" + sb.toString());
        	
        	// 피시방
//        	File dest = new File("C://Users//Administrator//Downloads//CommunityProject//public/" + sb.toString());
        	
//        	◆◆운영서버
//        	File dest = new File("/gnsdl2846/tomcat/webapps/upload/" + sb.toString());
        	File dest = new File("/gnsdl2846/tomcat/webapps/ROOT/WEB-INF/classes/static/" + sb.toString());
        	
        	
        	// error throw 함
        	mFile.transferTo(dest); 
        }
//        BoardEntity bEnt = new BoardEntity();
//        bEnt.setImage_path("/upload/" + sb.toString());
        
        return sb.toString(); // 로컬테스트
//		return "/upload/" + sb.toString();
	}
	
//	@PostMapping("/pushImage")
	@PostMapping("/addBoard")
	public int pushImage(@RequestPart(required = false)  MultipartFile uploadFile, @RequestPart String param) throws JsonMappingException, JsonProcessingException {
        // 시간과 originalFilename으로 매핑 시켜서 src 주소를 만들어 낸다.
        Date date = new Date();
        StringBuilder sb = new StringBuilder();
        
        ObjectMapper mapper = new ObjectMapper();
        BoardEntity bEnt;
		bEnt = mapper.readValue(param, BoardEntity.class);
		
        try {
        	uploadFile.isEmpty();
        	sb.append(date.getTime());
        	sb.append(uploadFile.getOriginalFilename());
        	
//        	◆◆ 로컬
//    	    File dest = new File("C://Users//gnsdl//Desktop//test//public/" + sb.toString());
        	
        	// 피시방 임시
//        	File dest = new File("C://Users//Administrator//Downloads//CommunityProject//public/" + sb.toString());
        	bEnt.setImage_path(sb.toString());

//        	◆◆운영서버
        	File dest = new File("/gnsdl2846/tomcat/webapps/upload/" + sb.toString());
        	bEnt.setImage_path("/upload/" + sb.toString());        	
        	
        	uploadFile.transferTo(dest);

        } catch (Exception e) {
        	System.out.println(e);
        }
        // 0807 임시 주석 시작
        // file image 가 없을 경우
//        if (uploadFile.isEmpty()) {
//       	 sb.append("none");
//        } else {
//       	 sb.append(date.getTime());
//       	 sb.append(uploadFile.getOriginalFilename());
//        }

//        if (!uploadFile.isEmpty()) {
//        	◆◆로컬
//    	    File dest = new File("C://images/" + sb.toString());
//        	
//        	◆◆운영서버
//        	File dest = new File("/gnsdl2846/tomcat/webapps/upload/" + sb.toString());
//        	
//        	// error throw 함
//    	    try {
//				uploadFile.transferTo(dest);
//			} catch (IllegalStateException | IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 
//    	    
//    	    
//        }
//        bEnt.setImage_path("/upload/" + sb.toString());
        // 0807 임시 주석 끝
        
        // db에 파일 위치랑 번호 등록
        int result = bDao.addBoard(bEnt);
		return 0;
    }
	
	@GetMapping(value = "/display")
    public ResponseEntity<Resource> display(@Param("filename") String filename) throws IOException {
//        String path = "C://images/";
		String path = "./upload/";
//        String path = "/tomcat/webapps/ROOT/WEB-INF/classes/static/images/";
//        filename = "1690988038002다운로드.jpg";
        
        Resource resource = new FileSystemResource(path + filename);
        
        HttpHeaders header = new HttpHeaders();
        Path filePath = null;
        
        filePath = Paths.get(path + filename);
        header.add("Content-Type", Files.probeContentType(filePath));
        
        return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
    }
}

