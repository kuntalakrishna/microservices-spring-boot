package com.dev.ops.micro.service.user.orika.mapper.mappings;

import java.util.HashSet;
import java.util.Set;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapperFacadeFactoryBean implements FactoryBean<MapperFacade> {

	private Set<MappingConfigurer> configurers;

	public MapperFacadeFactoryBean() {
		super();
		this.configurers = new HashSet<MappingConfigurer>();
	}

	@Autowired(required = true)
	public MapperFacadeFactoryBean(Set<MappingConfigurer> configurers) {
		super();
		this.configurers = configurers;
	}

	@Override
	public MapperFacade getObject() throws Exception {
		MapperFactory mapperFactory = new DefaultMapperFactory.Builder().useBuiltinConverters(true).build();

		for(MappingConfigurer configurer : this.configurers) {
			configurer.configure(mapperFactory);
		}
		return mapperFactory.getMapperFacade();
	}

	@Override
	public Class<?> getObjectType() {
		return MapperFacade.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}