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

import java.util.HashSet;
import java.util.Set;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The class MapperFacadeFactoryBean will load all the Orika mappers available
 * on the classpath according to @ComponentScan packages provided at the time of service start up.
 */
@Component
public class MapperFacadeFactoryBean implements FactoryBean<MapperFacade> {

	/** The set of all the orika mapping configurers loaded. */
	private Set<MappingConfigurer> configurers;

	/**
	 * Instantiates a new mapper facade factory bean with empty MappingConfigurers.
	 */
	public MapperFacadeFactoryBean() {
		super();
		this.configurers = new HashSet<MappingConfigurer>();
	}

	/**
	 * Instantiates a new mapper facade factory bean with all the MappingConfigurers available on the classpath.
	 *
	 * @param configurers the configurers
	 */
	@Autowired(required = true)
	public MapperFacadeFactoryBean(Set<MappingConfigurer> configurers) {
		super();
		this.configurers = configurers;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public MapperFacade getObject() throws Exception {
		MapperFactory mapperFactory = new DefaultMapperFactory.Builder().useBuiltinConverters(true).build();

		for(MappingConfigurer configurer : this.configurers) {
			configurer.configure(mapperFactory);
		}
		return mapperFactory.getMapperFacade();
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<?> getObjectType() {
		return MapperFacade.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}
}