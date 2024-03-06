
package com.newlecture.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newlecture.web.dao.BoardDao;
import com.newlecture.web.entity.BoardEntity;
import com.newlecture.web.service.S3UploadService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
//@Api(tags = {"API 정보를 제공하는 Controller"})
public class BoardController {
	
	// aws config start
//    @Autowired
//    private AmazonS3Client amazonS3Client;
    
	@Autowired
	S3UploadService s3Svc;
    
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
		
//		String deleteTarget = "static/upload/" + bEntity.getImage_path();
//		s3Svc.deleteImage(deleteTarget);
		
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
		if (mFile.isEmpty()) {
			return "none";
		}
		
		return s3Svc.saveFile(mFile); // aws
	}
	
//	@ApiOperation(value = "게시글 등록", notes = "")
	@PostMapping("/addBoard")
	public int pushImage(@RequestPart(required = false) MultipartFile uploadFile, @RequestPart String param)
			throws IllegalStateException, IOException {
		return uploadFileS3V2(uploadFile, param);
		
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

//	@GetMapping(value = "/display")
//	public ResponseEntity<Resource> display(@Param("filename") String filename) throws IOException {
//		String path = "./upload/";
//
//		Resource resource = new FileSystemResource(path + filename);
//
//		HttpHeaders header = new HttpHeaders();
//		Path filePath = null;
//
//		filePath = Paths.get(path + filename);
//		header.add("Content-Type", Files.probeContentType(filePath));
//
//		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
//	}

	public int uploadFileS3V2(MultipartFile uploadFile, String param) throws IllegalStateException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		BoardEntity bEnt;
		bEnt = mapper.readValue(param, BoardEntity.class);
		
		if (uploadFile != null) {
//			String dirPath = System.getProperty("user.home") + "/" + UUID.randomUUID() + uploadFile.getOriginalFilename();
//			File convertFile = new File(dirPath);
//			uploadFile.transferTo(convertFile);
			
//			log.debug("###dirPath >>> " + dirPath);
			
//			String fileName = "static/upload" + dirPath;
//			putS3(convertFile, fileName);			

			String fileURL = s3Svc.saveFile(uploadFile);
			bEnt.setImage_path(fileURL);
		}
		
		int result = bDao.addBoard(bEnt);
		return result;
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// 삭제된 방법
//	
//	public int uploadFileAws(MultipartFile uploadFile, String param) throws IllegalStateException, IOException {
//		Date date = new Date();
//		StringBuilder sb = new StringBuilder();
//
//		ObjectMapper mapper = new ObjectMapper();
//		BoardEntity bEnt;
//		bEnt = mapper.readValue(param, BoardEntity.class);
//		
//		if (uploadFile != null) {
//			String dirPath = System.getProperty("user.home") + "/" + UUID.randomUUID() + uploadFile.getOriginalFilename();
//			File convertFile = new File(dirPath);
//			uploadFile.transferTo(convertFile);
//			
//			log.debug("###dirPath >>> " + dirPath);
//			
//			String fileName = "static/upload" + dirPath;
//			putS3(convertFile, fileName);			
//			bEnt.setImage_path(fileName);
//
//			// 로컬에 저장해놓은 파일 삭제
//			convertFile.delete(); 
//		}
//		
//		int result = bDao.addBoard(bEnt);
//		return result;
//	}
	
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
        
    /**
     * S3에 있는 파일 삭제
     * 영어 파일만 삭제 가능 -> 한글 이름 파일은 안됨
     */
//    public void deleteS3(String filePath) throws Exception {
//    	
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
    
//	 public static SecretEntity getSecret() throws JsonMappingException, JsonProcessingException {
//			
//	     String secretName = "/secret/tom";
//	     Region region = Region.of("us-east-2");
//	
//	     // Create a Secrets Manager client
//	     SecretsManagerClient client = SecretsManagerClient.builder()
//	             .region(region)
//	             .build();
//	
//	     GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
//	             .secretId(secretName)
//	             .build();
//	
//	     GetSecretValueResponse getSecretValueResponse;
//	
//	     try {
//	         getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
//	     } catch (Exception e) {
//	         // For a list of exceptions thrown, see
//	         // https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html
//	         throw e;
//	     }
//	
//	     String secret = getSecretValueResponse.secretString();
//	     ObjectMapper mapper = new ObjectMapper();
//	     SecretEntity sEnt = new SecretEntity();
//	     sEnt = mapper.readValue(secret, SecretEntity.class);
//		
//	     return sEnt;
//	 }
}
