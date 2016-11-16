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
package com.dev.ops.micro.service.user.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The class User is only a placeholder. Please design this class according to your requirements.
 * This entity will refer to system_user table from the persistence store.
 * Also contains named query to fetch all the User entities.
 */
//TODO: Design this class according to your requirements.
@Entity
@Table(name = "system_user")
@NamedQueries({@NamedQuery(name = "User.fetchAllUsers", query = "SELECT user FROM User user")})
public class User {

	/** The user id PK is attached with the sequence to auto generate id's for new user entities. */
	@Id
	@SequenceGenerator(name = "USER_ID_SEQUENCE_GENERATOR", sequenceName = "USER_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ID_SEQUENCE_GENERATOR")
	@Column(name = "user_id")
	private Long userId;

	/** The user name field. */
	@Column(name = "user_name")
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