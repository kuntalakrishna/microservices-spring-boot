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
package com.dev.ops.micro.service.user.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The class Application Config will contain the beans definition for spring's IOC.
 */
@Configuration
@EnableSwagger2
public class ApplicationConfig {

	/** The title for the spring boot service to be displayed on swagger UI. */
	@Value("${swagger.title}")
	private String title;

	/** The description of the spring boot service. */
	@Value("${swagger.description}")
	private String description;

	/** The version of the service. */
	@Value("${swagger.version}")
	private String version;

	/** The terms of service url for the service if applicable. */
	@Value("${swagger.termsOfServiceUrl}")
	private String termsOfServiceUrl;

	/** The contact name for the service. */
	@Value("${swagger.contact.name}")
	private String contactName;

	/** The contact url for the service. */
	@Value("${swagger.contact.url}")
	private String contactURL;

	/** The contact email for the service. */
	@Value("${swagger.contact.email}")
	private String contactEmail;

	/** The license for the service if applicable. */
	@Value("${swagger.license}")
	private String license;

	/** The license url for the service if applicable. */
	@Value("${swagger.licenseUrl}")
	private String licenseURL;

	/**
	 * This method will return the Docket API object to swagger which will in turn display the information on the swagger UI.
	 *
	 * Refer the URL http://{ip-address or host-name}:{service.port}/{server.contextPath}/swagger-ui.html
	 *
	 * service.port and server.contextPath are provided in application.properties.
	 * If these properties are not defined in the application.properties then the URL for swagger will be
	 * http://{ip-address or host-name}:8080/swagger-ui.html
	 *
	 * @return the docket
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build().apiInfo(apiInfo());
	}

	/**
	 * This method will return the API info object to swagger which will in turn display the information on the swagger UI.
	 *
	 * @return the API information
	 */
	private ApiInfo apiInfo() {
		return new ApiInfo(title, description, version, termsOfServiceUrl, new Contact(contactName, contactURL, contactEmail), license, licenseURL);
	}
}