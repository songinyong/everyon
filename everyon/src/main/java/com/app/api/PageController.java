package com.app.api;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;


import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PageController {

	 //모임들 목록
	 @GetMapping("/")
	 public String main() {
	    	return "index.html";
	 }
}
