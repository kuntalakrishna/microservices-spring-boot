package com.dev.ops.micro.service.user.orika.mapper.mappings;

import ma.glasnost.orika.MapperFactory;

import org.springframework.stereotype.Component;

import com.dev.ops.micro.service.user.domain.UserDTO;
import com.dev.ops.micro.service.user.entities.User;

@Component
public class UserMappingConfigurer implements MappingConfigurer {

	@Override
	public void configure(MapperFactory factory) {
		factory.classMap(UserDTO.class, User.class).byDefault().register();
	}
}