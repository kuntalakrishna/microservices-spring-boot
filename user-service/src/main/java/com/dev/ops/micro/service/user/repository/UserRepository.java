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
package com.dev.ops.micro.service.user.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.dev.ops.micro.service.user.entities.User;

/**
 * The interface User Repository acts as a DAO for User entity.
 */
public interface UserRepository extends CrudRepository<User, Long> {

	/**
	 * Fetch all users.
	 *
	 * @return the list
	 */
	List<User> fetchAllUsers();
}
