
package com.newlecture.web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newlecture.web.dao.BoardDao;
import com.newlecture.web.entity.BoardEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
//@Api(tags = {"API 정보를 제공하는 Controller"})
public class BoardController {
	
	// aws config start
    @Autowired
    private AmazonS3Client amazonS3Client;
    
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
 // aws config end
	
	@Value("${temp-img-url}")
	private String tempImgUrl;
	
	@Value("${addBoard-img-url}")
	private String addBoardImgUrl;
	
	@Value("${addBoard-setImgPath-img-url}")
	private String addBoardSetImgPathImgUrl;

	@Autowired
	BoardDao bDao;
	
//	@ApiOperation(value = "모든 게시글 조회", notes = "모든 게시글 조회")
	@PostMapping("/getBoardAll")
	public List<BoardEntity> getBoardAll(@RequestBody BoardEntity bEntity) {
		log.info("addBoard >>> ");
		if (bEntity.getOrder().isEmpty()) {
			bEntity.setOrder("0");
		}

		List<BoardEntity> list = bDao.getBoardAll(bEntity);
		return list;
	}

//	@ApiOperation(value = "제목으로 게시글 조회", notes = "제목으로 게시글 조회")
	@PostMapping("/searchBoard")
	public List<BoardEntity> searchBoard(@RequestBody BoardEntity bEntity) {
		List<BoardEntity> list = bDao.searchBoard(bEntity);
		return list;
	}

//	@ApiOperation(value = "아이디로 게시글 조회", notes = "아이디로 게시글 조회")
	@PostMapping("/getBoardById")
	public BoardEntity getBoardById(@RequestBody BoardEntity bEntity) {
		BoardEntity bEnt = bDao.getBoardById(bEntity);
		return bEnt;
	}

//	@ApiOperation(value = "게시글 업데이트", notes = "구분,제목,내용,아이디가 필요")
	@PostMapping("/updateBoard")
	public int updateBoard(@RequestBody BoardEntity bEntity) {
		int result = bDao.updateBoard(bEntity);
		return result;
	}
	
//	@ApiOperation(value = "게시글 조회수 업데이트", notes = "아이디 필요")
	@PostMapping("/updateHitBoard")
	public int updateHitBoard(@RequestBody BoardEntity bEntity) {
		int result = bDao.updateHitBoard(bEntity);
		return result;
	}

//	@ApiOperation(value = "게시글 삭제", notes = "아이디 필요")
	@PostMapping("/deleteBoard")
	public int deleteBoard(@RequestBody BoardEntity bEntity) throws Exception {
		int result = bDao.deleteBoard(bEntity);
		
		String deleteTarget = "static/upload/" + bEntity.getImage_path();
		deleteS3(deleteTarget);
		
		return result;
	}

//	@ApiOperation(value = "게시글 좋아요 업데이트", notes = "아이디 필요")
	@PostMapping("/updateRecommendBoard")
	public int updateRecommendBoard(@RequestBody BoardEntity bEntity) {
		int result = bDao.updateRecommendHitBoard(bEntity);
		return result;
	}

//	@ApiOperation(value = "사진 미리보기용 api", notes = "아이디 필요")
	@PostMapping("/tempImg")
	public String tempImg(@RequestParam MultipartFile mFile) throws IllegalStateException, IOException {
		// 시간과 originalFilename으로 매핑 시켜서 src 주소를 만들어 낸다.
//		Date date = new Date();
//		StringBuilder sb = new StringBuilder();
//		BoardEntity bEnt = new BoardEntity();

		// file image 가 없을 경우
//		if (mFile.isEmpty()) {
//			sb.append("none");
//			return "none";
//		} else {
//			sb.append(date.getTime());
//			sb.append(mFile.getOriginalFilename());
//		}

//		if (!mFile.isEmpty()) {
//			File dest = new File(tempImgUrl + sb.toString());
//
//			mFile.transferTo(dest); // error는 throw해버림
//		}
		
		if (mFile.isEmpty()) {
			return "none";
		}
		
		// aws - user.home
		// windows - user.dir
		
		// 1. 경로를 지정해 껍데기를 만든다.
		String fullDirFileName = System.getProperty("user.home") + "/" + UUID.randomUUID() + mFile.getOriginalFilename();
		File tempFile = new File(fullDirFileName);
		
		// 2. 껍데기에 byte값을 밀어 넣는다.
		mFile.transferTo(tempFile);
		
		String saveDir = "static/upload" + fullDirFileName;

		// 3. 진짜 byte값이 있는 파일, 저장할 위치를 알려줌.
		putS3(tempFile, saveDir);
		
		tempFile.delete();
		return saveDir; // aws

	}
	
//	@ApiOperation(value = "게시글 등록", notes = "")
	@PostMapping("/addBoard")
	public int pushImage(@RequestPart(required = false) MultipartFile uploadFile, @RequestPart String param)
			throws IllegalStateException, IOException {
		return uploadFileAws(uploadFile, param);
		
		// 시간과 originalFilename으로 매핑 시켜서 src 주소를 만들어 낸다.
//		Date date = new Date();
//		StringBuilder sb = new StringBuilder();
//
//		ObjectMapper mapper = new ObjectMapper();
//		BoardEntity bEnt;
//		bEnt = mapper.readValue(param, BoardEntity.class);
		

		
//		try {
//			uploadFile.isEmpty();
//			sb.append(date.getTime());
//			sb.append(uploadFile.getOriginalFilename());
//
//			/**
//			 * 상대경로는 프로젝트 폴더 최초 진입점임 src보이는 위치 주의) File은 절대경로로 처리해야함
//			 */
//			File dest = new File(addBoardImgUrl + sb.toString());
//			bEnt.setImage_path(addBoardSetImgPathImgUrl + sb.toString());
//			
//			uploadFile.transferTo(dest);
//
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//
//		// db에 파일 위치랑 번호 등록
//		int result = bDao.addBoard(bEnt);
//		return 0;
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
	
	public int uploadFileAws(MultipartFile uploadFile, String param) throws IllegalStateException, IOException {
		Date date = new Date();
		StringBuilder sb = new StringBuilder();

		ObjectMapper mapper = new ObjectMapper();
		BoardEntity bEnt;
		bEnt = mapper.readValue(param, BoardEntity.class);
		
		if (uploadFile != null) {
			String dirPath = System.getProperty("user.home") + "/" + UUID.randomUUID() + uploadFile.getOriginalFilename();
			File convertFile = new File(dirPath);
			uploadFile.transferTo(convertFile);
			
			log.debug("###dirPath >>> " + dirPath);
			
			String fileName = "static/upload" + dirPath;
			putS3(convertFile, fileName);			
			bEnt.setImage_path(fileName);

			// 로컬에 저장해놓은 파일 삭제
			convertFile.delete(); 
		}
		
		int result = bDao.addBoard(bEnt);
		return result;
	}
	
    /**
     * S3로 업로드
     * @param uploadFile : 업로드할 파일
     * @param fileName : 업로드할 파일 이름
     * @return 업로드 경로
     */
    public String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(
                CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }
        
    /**
     * S3에 있는 파일 삭제
     * 영어 파일만 삭제 가능 -> 한글 이름 파일은 안됨
     */
    public void deleteS3(String filePath) throws Exception {
        try{
            String key = filePath.substring(56); // 폴더/파일.확장자

            try {
                amazonS3Client.deleteObject(bucket, key);
            } catch (AmazonServiceException e) {
                log.info(e.getErrorMessage());
            }

        } catch (Exception exception) {
            log.info(exception.getMessage());
        }
        log.info("[S3Uploader] : S3에 있는 파일 삭제");
    }
}
