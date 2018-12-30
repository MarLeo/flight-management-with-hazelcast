package com.project.tchokonthe.swagger;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private Contact contact = new Contact("Martin Tchokonthe", "https://github.com/MarLeo", "marin.aurele12@gmail.com");

    private ApiInfo apiInfo(String version) {
        return new ApiInfoBuilder()
                .title("FlightReference Management API")
                .description("FlightReference Management API for testing")
                .version(version)
                .termsOfServiceUrl("Terms of Service")
                .contact(contact)
                .license("Apache License Version 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .build();
    }

    @Bean
    public Docket ticket() {
        return new Docket(SWAGGER_2)
                .groupName("v1.0/ticket/api/docs")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.project.tchokonthe.controller"))
                .paths(regex("/api/ticket/v1.0.*"))
                .build()
                .apiInfo(apiInfo("1.0"));
    }

    @Bean
    public Docket flight() {
        return new Docket(SWAGGER_2)
                .groupName("v1.0/flightReference/api/docs")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.project.tchokonthe.controller"))
                .paths(regex("/api/flightReference/v1.0.*"))
                .build()
                .apiInfo(apiInfo("1.0"));
    }

    @Bean
    public Docket flights() {
        return new Docket(SWAGGER_2)
                .groupName("v1.0/flight/api/docs")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.project.tchokonthe.controller"))
                .paths(regex("/api/flight/v1.0.*"))
                .build()
                .apiInfo(apiInfo("1.0"));
    }



}
