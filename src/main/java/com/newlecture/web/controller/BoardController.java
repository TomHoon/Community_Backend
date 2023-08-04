
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	
	@PostMapping("/updateRecommendBoard")
	public int updateRecommendBoard(@RequestBody BoardEntity bEntity) {
		int result = bDao.updateRecommendHitBoard(bEntity);
		return result;
	}
	
//	@PostMapping("/pushImage")
	@PostMapping("/addBoard")
	public int pushImage(@RequestPart MultipartFile uploadFile, @RequestPart String param) throws IllegalStateException, IOException {
        // 시간과 originalFilename으로 매핑 시켜서 src 주소를 만들어 낸다.
        Date date = new Date();
        StringBuilder sb = new StringBuilder();
        
        ObjectMapper mapper = new ObjectMapper();
        BoardEntity bEnt = mapper.readValue(param, BoardEntity.class);
        
        // file image 가 없을 경우
        if (uploadFile.isEmpty()) {
       	 sb.append("none");
        } else {
       	 sb.append(date.getTime());
       	 sb.append(uploadFile.getOriginalFilename());
        }

        if (!uploadFile.isEmpty()) {
//        	◆◆로컬
//    	    File dest = new File("C://images/" + sb.toString());
        	
//        	◆◆운영서버
        	File dest = new File("/gnsdl2846/tomcat/webapps/upload/" + sb.toString());
        	
        	// error throw 함
    	    uploadFile.transferTo(dest); 
    	    
    	    
        }
        bEnt.setImage_path("/upload/" + sb.toString());
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
