package org.xavier.star.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
@ConditionalOnExpression("${dev.enable:true}")//当enable为true时才选择加载该配置类
public class Swagger2Config {

    private static final String BASE_PACKAGE1 = "org.xavier.star.controller";
    private static final String GROUP_NAME1 = "controller";
    private static final String BASE_PACKAGE2 = "org.xavier.star.entity";
    private static final String GROUP_NAME2 = "entity";
    private static final String BASE_PACKAGE3 = "org.xavier.star.dto";
    private static final String GROUP_NAME3 = "dto";
    private static final String TITLE = "star API Documentation";
    private static final String DESCRIPTION = "star Api";

    @Bean
    public Docket createControllerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(GROUP_NAME1)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE1))//设定扫描范围
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(this.getParameterList());

    }

    @Bean
    public Docket createEntityApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(GROUP_NAME2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE2))//设定扫描范围
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(this.getParameterList());
    }

    @Bean
    public Docket createDtoApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(GROUP_NAME3)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE3))//设定扫描范围
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(this.getParameterList());

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(TITLE)
                .description(DESCRIPTION)
                .version("1.0")
                .build();
    }

    private List<Parameter> getParameterList() {
        ParameterBuilder clientIdTicket = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        clientIdTicket.name("session").description("会话Id")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false).build(); //设置false，表示session参数 非必填,可传可不传！
        pars.add(clientIdTicket.build());
        return pars;
    }

}
