
package io.fourfinanceit.util;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ComponentScan(basePackages="io.fourfinanceit.rest")
public class ApplicationSwaggerConfig {

   @Bean
   public Docket customDocket(){
      return new Docket(DocumentationType.SWAGGER_2)
    		  .select()
              .apis(RequestHandlerSelectors.any())
              .paths(PathSelectors.any())
              .build()
              .securitySchemes(Lists.newArrayList(apiKey()))
              .apiInfo(getApiInfo());
   }

   private ApiKey apiKey() {
        return new ApiKey("apiKey", "Authorization", "header");
    }

   private ApiInfo getApiInfo() {
	   return new ApiInfo(
		"REST fourfinanceit backend Api Documentation. Test task",
		"This is REST API documentation of the Spring fourfinanceit backend. " +
            "/adminapi/ is accessible only from ADMIN_ROLE, /userapi/ is accesible only from USER_ROLE"  +
            "When calling the APIs use admin/aaa for ADMIN_ROLE and user/aaa for USER_ROLE, insert authentication token using 'Authorize' button " +
            " also you can register new user with USER_ROLE using authapi/register. ",
		"1.0",
		"fourfinanceit backend terms of service",
		new Contact(
				"Alexander Larin",
				"https://github.com/",
				"larinego@gmail.com"),
		"Apache 2.0",
		"http://www.apache.org/licenses/LICENSE-2.0");
   }


}
