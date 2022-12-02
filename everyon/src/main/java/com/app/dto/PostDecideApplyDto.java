package com.app.dto;

import com.domain.jpa.ApplyLog;

import lombok.Getter;

@Getter
public class PostDecideApplyDto {
    private Long applyId;
    private boolean appr;
    private String refusalDec;
    
    

    public PostDecideApplyDto(Long applyId, boolean appr,String refusalDec ) {
    	this.applyId = applyId;
    	this.appr = appr;
    	this.refusalDec = refusalDec;
    }
    

}
