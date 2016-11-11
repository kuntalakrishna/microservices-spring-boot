package com.dev.ops.micro.service.user.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.dev.ops.micro.service.user.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
	List<User> fetchAllUsers();
}
