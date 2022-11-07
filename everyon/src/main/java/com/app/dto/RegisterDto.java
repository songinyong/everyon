package com.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class RegisterDto {

	//@JsonProperty("uid")
	private String uid ;
	//@JsonProperty("platform")
	private String platform ;
	
	public RegisterDto(String uid, String platform) {
		this.uid = uid ;
		this.platform = platform ;
	}
}
