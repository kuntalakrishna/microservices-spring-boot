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
package com.dev.ops.micro.service.user.controllers;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dev.ops.micro.service.user.domain.UserDTO;
import com.dev.ops.micro.service.user.services.UserService;

/**
 * The class User Service Controller exposes the rest services for CRUD operations related to User.
 */
@Controller
@RequestMapping("/user")
public class UserServiceController {

	/** Autowired the user service. */
	@Autowired
	private UserService userService;

	/** The LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger(UserServiceController.class);

	/**
	 * REST API to get the user details.
	 *
	 * @param userId the user id for which the details to be fetched
	 * @return the user details
	 */
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public UserDTO getUserDetails(@PathVariable Long userId) {
		LOGGER.info("Get User details for:" + userId);
		return userService.getUserDetails(userId);
	}

	/**
	 * REST API to save user details.
	 *
	 * @param userDTO holds the JSON object which needs to be saved to the persistence store
	 * @return the saved user details
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON)
	@ResponseBody
	public UserDTO saveUserDetails(@RequestBody @Valid UserDTO userDTO) {
		UserDTO userDetails = userService.saveUserDetails(userDTO);
		return userDetails;
	}

	/**
	 * REST API to delete the user from persistence store.
	 *
	 * @param userId the user id for which the details needs to be deleted
	 */
	@RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteUserDetails(@PathVariable Long userId) {
		userService.deleteUserDetails(userId);
		LOGGER.info("The deleted User id: " + userId);
	}

	/**
	 * REST API to get all the user details which are available in the persistence store.
	 *
	 * @return the list of all the user details available
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public List<UserDTO> getAllUserDetails() {
		return userService.getAllUserDetails();
	}
}