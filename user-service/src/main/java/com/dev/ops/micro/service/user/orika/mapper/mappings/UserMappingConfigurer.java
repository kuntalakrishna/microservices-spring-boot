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
package com.dev.ops.micro.service.user.orika.mapper.mappings;

import ma.glasnost.orika.MapperFactory;

import org.springframework.stereotype.Component;

import com.dev.ops.micro.service.user.domain.UserDTO;
import com.dev.ops.micro.service.user.entities.User;

/**
 * The class User Mapping Configurer is responsible for converting
 * UserDTO to User object using orika mapper configuration.
 */
@Component
public class UserMappingConfigurer implements MappingConfigurer {

	/* (non-Javadoc)
	 * @see com.dev.ops.micro.service.user.orika.mapper.mappings.MappingConfigurer#configure(ma.glasnost.orika.MapperFactory)
	 */
	@Override
	public void configure(MapperFactory factory) {
		factory.classMap(UserDTO.class, User.class).byDefault().register();
	}
}