
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
import org.springframework.beans.factory.annotation.Value;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class BoardController {

	@Value("${temp-img-url}")
	private String tempImgUrl;
	
	@Value("${addBoard-img-url}")
	private String addBoardImgUrl;
	
	@Value("${addBoard-setImgPath-img-url}")
	private String addBoardSetImgPathImgUrl;

	@Autowired
	BoardDao bDao;

	@PostMapping("/getBoardAll")
	public List<BoardEntity> getBoardAll(@RequestBody BoardEntity bEntity) {
		log.info("addBoard >>> ");
		if (bEntity.getOrder().isEmpty()) {
			bEntity.setOrder("0");
		}

		List<BoardEntity> list = bDao.getBoardAll(bEntity);
		return list;
	}

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
		BoardEntity bEnt = new BoardEntity();

		// file image 가 없을 경우
		if (mFile.isEmpty()) {
			sb.append("none");
			return "none";
		} else {
			sb.append(date.getTime());
			sb.append(mFile.getOriginalFilename());
		}

		if (!mFile.isEmpty()) {
			File dest = new File(tempImgUrl + sb.toString());

			mFile.transferTo(dest); // error는 throw해버림
		}

		return "/upload/" + sb.toString(); // nas
	}

	@PostMapping("/addBoard")
	public int pushImage(@RequestPart(required = false) MultipartFile uploadFile, @RequestPart String param)
			throws JsonMappingException, JsonProcessingException {
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

			/**
			 * 상대경로는 프로젝트 폴더 최초 진입점임 src보이는 위치 주의) File은 절대경로로 처리해야함
			 */
			File dest = new File(addBoardImgUrl + sb.toString());
			bEnt.setImage_path(addBoardSetImgPathImgUrl + sb.toString());
			
			uploadFile.transferTo(dest);

		} catch (Exception e) {
			System.out.println(e);
		}

		// db에 파일 위치랑 번호 등록
		int result = bDao.addBoard(bEnt);
		return 0;
	}

	@GetMapping(value = "/display")
	public ResponseEntity<Resource> display(@Param("filename") String filename) throws IOException {
		String path = "./upload/";

		Resource resource = new FileSystemResource(path + filename);

		HttpHeaders header = new HttpHeaders();
		Path filePath = null;

		filePath = Paths.get(path + filename);
		header.add("Content-Type", Files.probeContentType(filePath));

		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}
}
