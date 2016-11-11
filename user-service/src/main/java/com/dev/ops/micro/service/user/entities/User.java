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

//TODO: Design this class according to your requirements.
@Entity
@Table(name = "system_user")
@NamedQueries({@NamedQuery(name = "User.fetchAllUsers", query = "SELECT user FROM User user")})
public class User {

	@Id
	@SequenceGenerator(name = "USER_ID_SEQUENCE_GENERATOR", sequenceName = "USER_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ID_SEQUENCE_GENERATOR")
	@Column(name = "user_id")
	private Long userId;

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