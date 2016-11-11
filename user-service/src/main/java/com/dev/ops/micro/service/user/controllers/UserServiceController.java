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

@Controller
@RequestMapping("/user")
public class UserServiceController {

	@Autowired
	private UserService userService;

	private static final Logger LOGGER = LogManager.getLogger(UserServiceController.class);

	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public UserDTO getUserDetails(@PathVariable Long userId) {
		LOGGER.info("Get User details for:" + userId);
		return userService.getUserDetails(userId);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON)
	@ResponseBody
	public UserDTO saveUserDetails(@RequestBody @Valid UserDTO userDTO) {
		UserDTO userDetails = userService.saveUserDetails(userDTO);
		return userDetails;
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteUserDetails(@PathVariable Long userId) {
		userService.deleteUserDetails(userId);
		LOGGER.info("The deleted User id: " + userId);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public List<UserDTO> getAllUserDetails() {
		return userService.getAllUserDetails();
	}
}