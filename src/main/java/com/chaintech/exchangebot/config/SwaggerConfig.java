package com.chaintech.exchangebot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration("swaggerConfigProperties")
@EnableSwagger2
public class SwaggerConfig {

	@Value("${swagger.enabled}")
	private String enabled = "false";

	@Value("${swagger.title}")
	private String title;

	@Value("${swagger.description}")
	private String description;

	@Value("${swagger.useDefaultResponseMessages}")
	private String useDefaultResponseMessages;

	@Value("${swagger.enableUrlTemplating}")
	private String enableUrlTemplating;

	@Value("${swagger.deepLinking}")
	private String deepLinking;

	@Value("${swagger.defaultModelsExpandDepth}")
	private String defaultModelsExpandDepth;

	@Value("${swagger.defaultModelExpandDepth}")
	private String defaultModelExpandDepth;

	@Value("${swagger.displayOperationId}")
	private String displayOperationId;

	@Value("${swagger.displayRequestDuration}")
	private String displayRequestDuration;

	@Value("${swagger.filter}")
	private String filter;

	@Value("${swagger.maxDisplayedTags}")
	private String maxDisplayedTags;

	@Value("${swagger.showExtensions}")
	private String showExtensions;

	@Bean
	public Docket api() {

		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.chaintech.exchangebot.controller"))
				.paths(PathSelectors.any())
				.build();
	}

	@Bean
	UiConfiguration uiConfig() {

		return UiConfigurationBuilder
				.builder()
				.deepLinking(Boolean.valueOf(this.deepLinking))
				.displayOperationId(Boolean.valueOf(this.displayOperationId))
				.defaultModelsExpandDepth(Integer.valueOf(this.defaultModelsExpandDepth))
				.defaultModelExpandDepth(Integer.valueOf(this.defaultModelExpandDepth))
				.defaultModelRendering(ModelRendering.EXAMPLE)
				.displayRequestDuration(Boolean.valueOf(this.displayRequestDuration))
				.docExpansion(DocExpansion.NONE)
				.filter(Boolean.valueOf(this.filter))
				.maxDisplayedTags(Integer.valueOf(this.maxDisplayedTags))
				.operationsSorter(OperationsSorter.ALPHA)
				.showExtensions(Boolean.valueOf(this.showExtensions))
				.tagsSorter(TagsSorter.ALPHA)
				.supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
				.validatorUrl(null)
				.build();
	}

	@Bean
	ApiInfo apiInfo() {

		return new ApiInfoBuilder().title(this.title).description(this.description).build();
	}

}