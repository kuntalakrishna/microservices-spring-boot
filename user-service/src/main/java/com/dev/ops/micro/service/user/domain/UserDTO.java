package com.dev.ops.micro.service.user.domain;

import javax.validation.constraints.NotNull;

//TODO: Design this class according to your requirements.
public class UserDTO {

	private Long userId;

	@NotNull(message = "{NotNull.userDTO.userName}")
	private String userName;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}