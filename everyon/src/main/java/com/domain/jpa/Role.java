package com.domain.jpa;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

	USER("NORMAL_USER", "일반사용자");
	
	private final String key;
	private final String title;
}
