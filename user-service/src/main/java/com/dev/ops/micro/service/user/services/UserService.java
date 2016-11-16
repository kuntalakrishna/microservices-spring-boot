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
package com.dev.ops.micro.service.user.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.TransactionalException;

import ma.glasnost.orika.MapperFacade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dev.ops.micro.service.user.domain.UserDTO;
import com.dev.ops.micro.service.user.entities.User;
import com.dev.ops.micro.service.user.repository.UserRepository;

/**
 * The class User Service.
 */
@Component
public class UserService {

	/** The mapper facade which will help in converting the objects from one form to the other forms. */
	@Autowired
	private MapperFacade mapperFacade;

	/** The user repository will act as a DAO for User entity. */
	@Autowired
	private UserRepository userRepository;

	/**
	 * Gets the user details from the persistence store.
	 *
	 * @param joinPoint the contains the information related to method signature and the join point
	 * @return the user details
	 */
	@Transactional(rollbackFor = {Exception.class})
	public UserDTO getUserDetails(Long userId) {
		User user = userRepository.findOne(userId);
		UserDTO userDetails = null;
		if(null != user) {
			userDetails = mapperFacade.map(user, UserDTO.class);
		} else {
			throw new TransactionalException("User with id <" + userId + "> not found.", null);
		}
		return userDetails;
	}

	/**
	 * Save user details.
	 *
	 * @param userDTO the object which needs to be saved to the persistence store
	 * @return the saved user details
	 */
	@Transactional(rollbackFor = {Exception.class})
	public UserDTO saveUserDetails(UserDTO userDTO) {
		User user = mapperFacade.map(userDTO, User.class);
		user = userRepository.save(user);
		return mapperFacade.map(user, UserDTO.class);
	}

	/**
	 * Delete the user from persistence store.
	 *
	 * @param userId the user id for which the details needs to be deleted
	 */
	public void deleteUserDetails(Long userId) {
		User user = userRepository.findOne(userId);
		if(null != user) {
			userRepository.delete(userId);
		} else {
			throw new TransactionalException("User with id <" + userId + "> not found.", null);
		}
	}

	/**
	 * Get all the user details which are available in the persistence store.
	 *
	 * @return the list of all the user details available
	 */
	@Transactional(rollbackFor = {Exception.class})
	public List<UserDTO> getAllUserDetails() {
		List<UserDTO> userDTOs = new ArrayList<UserDTO>();
		List<User> users = userRepository.fetchAllUsers();
		for(User user : users) {
			UserDTO userDTO = mapperFacade.map(user, UserDTO.class);
			userDTOs.add(userDTO);
		}
		return userDTOs;
	}
}