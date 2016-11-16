/*
 *
 * Copyright (C) 2016 Krishna Kuntala <kuntala.krishna@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.dev.ops.micro.service.user.domain;

import javax.validation.constraints.NotNull;

/**
 * The class User DTO is only a placeholder. Please design this class according to your requirements.
 */
//TODO: Design this class according to your requirements.
public class UserDTO {

	/** The user id which represents the primary key of its corresponding entity. */
	private Long userId;

	/** The user name field with the validation of NotNull. In case of validation error, the message would be picked up from Validation.properties file */
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