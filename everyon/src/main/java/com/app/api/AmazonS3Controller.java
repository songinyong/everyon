package com.app.api;

import java.util.HashMap;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.service.AwsS3Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class AmazonS3Controller {

    private final AwsS3Service awsS3Service;

    /**
     * Amazon S3에 이미지 업로드
     * @return 성공 시 200 Success와 함께 업로드 된 파일의 파일명 리스트 반환
     */
    //@ApiOperation(value = "Amazon S3에 이미지 업로드", notes = "Amazon S3에 이미지 업로드 ") @ApiParam(value="img 파일들(여러 파일 업로드 가능)", required = true) 
    @PostMapping("/image")
    public HashMap uploadImage(@RequestPart List<MultipartFile> multipartFile) {
    	HashMap result = new HashMap();
    	
    	try {
    		result.put("name", awsS3Service.uploadImage(multipartFile));
    		result.put("result", true);
    	}
    	catch (Exception e) {
    		result.put("result", false);
    	}
        return result;
    }

    /**
     * Amazon S3에 이미지 업로드 된 파일을 삭제
     * @return 성공 시 200 Success
     */
    //@ApiOperation(value = "Amazon S3에 업로드 된 파일을 삭제", notes = "Amazon S3에 업로드된 이미지 삭제")
    @DeleteMapping("/image")
    public void deleteImage(@RequestParam String fileName) {
        awsS3Service.deleteImage(fileName);
        
    }
}