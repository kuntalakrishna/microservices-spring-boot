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

@Component
public class UserService {

	@Autowired
	private MapperFacade mapperFacade;

	@Autowired
	private UserRepository userRepository;

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

	@Transactional(rollbackFor = {Exception.class})
	public UserDTO saveUserDetails(UserDTO userDTO) {
		User user = mapperFacade.map(userDTO, User.class);
		user = userRepository.save(user);
		return mapperFacade.map(user, UserDTO.class);
	}

	public void deleteUserDetails(Long userId) {
		User user = userRepository.findOne(userId);
		if(null != user) {
			userRepository.delete(userId);
		} else {
			throw new TransactionalException("User with id <" + userId + "> not found.", null);
		}
	}

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