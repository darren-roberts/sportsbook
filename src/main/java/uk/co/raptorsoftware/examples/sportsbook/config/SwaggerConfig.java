package uk.co.raptorsoftware.examples.sportsbook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket postsApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("public-api").apiInfo(apiInfo()).select()
				.paths(PathSelectors.any())
				.apis(RequestHandlerSelectors.basePackage("uk.co.raptorsoftware.examples.sportsbook")).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Scorecard APIs").description(
				"A set iof API's for creating a sports event and recording scores against a scorescard for each event.")
				.termsOfServiceUrl("http://javainuse.com").license("JavaInUse License")
				.licenseUrl("darren.roberts@raptorsoftware.co.uk").version("1.0").build();
	}

}
