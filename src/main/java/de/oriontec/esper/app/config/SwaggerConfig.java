package de.oriontec.esper.app.config;


import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Bean
  public Docket productApi() {
//    List<SecurityScheme> schemeList = new ArrayList<>();
//    schemeList.add(new BasicAuth("basicAuth"));
    return new Docket(DocumentationType.SWAGGER_2).select()
        .apis(RequestHandlerSelectors.basePackage("de.oriontec.esper.app")).paths(regex("/.*")).build()
        .pathMapping("/").apiInfo(metaData());

  }


  private ApiInfo metaData() {
    ApiInfo apiInfo = new ApiInfo("Stream Analysis with Esper", "Sample Stream Analytics application using Esper SDK", "1.0", "Terms of service",
        new Contact("Kemal Atik", "", "k.atik@oriontec.de"), "Apache License Version 2.0",
        "https://www.apache.org/licenses/LICENSE-2.0");
    return apiInfo;
  }
}
