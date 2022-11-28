package com.domain.jpa;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.app.dto.PutMeetDto;
import com.app.dto.UpdateMyPageDto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="users")
@NoArgsConstructor
public class CustomUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String platform;
    @Column
    private String uid;
    @Column
    private String nickname;
    @Column
    private String auth;
    
    @Column
    private String image;

    @Column
    private String introduce;
    
    @Builder
    public CustomUser(String platform, String uid) {
    	
    	String hashCode = String.valueOf(uid.hashCode());
    	if(hashCode.length() > 10)
    		hashCode = hashCode.substring(0, 9);
    	
    	this.nickname = String.valueOf(hashCode);
    	this.auth = "USER" ;
    	this.platform = platform ;
    	this.uid = uid;
    }
    
	public void updateInfo(UpdateMyPageDto updateMyPageDto) {
		
		if(updateMyPageDto.getImage() != null)
		    this.image = updateMyPageDto.getImage();
		if(updateMyPageDto.getIntroduce() != null)
			this.introduce = updateMyPageDto.getIntroduce();
		if(updateMyPageDto.getNickname() != null)
			this.nickname = updateMyPageDto.getNickname();
		
	}


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return nickname;
    }
    
    
    public void setImage(String image) {
    	this.image = image ;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return false;
    }
}