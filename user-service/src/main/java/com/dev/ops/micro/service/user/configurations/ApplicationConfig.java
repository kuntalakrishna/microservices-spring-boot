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

@Configuration
@EnableSwagger2
public class ApplicationConfig {

	@Value("${swagger.title}")
	private String title;

	@Value("${swagger.description}")
	private String description;

	@Value("${swagger.version}")
	private String version;

	@Value("${swagger.termsOfServiceUrl}")
	private String termsOfServiceUrl;

	@Value("${swagger.contact.name}")
	private String contactName;

	@Value("${swagger.contact.url}")
	private String contactURL;

	@Value("${swagger.contact.email}")
	private String contactEmail;

	@Value("${swagger.license}")
	private String license;

	@Value("${swagger.licenseUrl}")
	private String licenseURL;

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build().apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfo(title, description, version, termsOfServiceUrl, new Contact(contactName, contactURL, contactEmail), license, licenseURL);
	}
}