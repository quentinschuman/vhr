package org.javaboy.vhr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

@SpringBootApplication
@EnableCaching
@MapperScan(basePackages = "org.javaboy.vhr.mapper")
@EnableScheduling
public class VhrApplication {

    public static void main(String[] args) {
        SpringApplication.run(VhrApplication.class, args);
    }

    @Bean
    public Docket docket(Environment environment) {
        // 启动Swagger的环境 开发和测试
        Profiles of = Profiles.of("develop", "test");
        boolean b = environment.acceptsProfiles(of);
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .groupName("group1")
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 文档标题
                .title("vhr项目接口文档")
                // 文档基本描述
                .description("基本的一些接口说明")
                // 联系人信息
                .contact(new Contact("xxxx",
                        "https://blog.csdn.net",
                        "xxxx@qq.com"))
                // 组织链接
                .termsOfServiceUrl("http://terms.service.url/组织链接")
                // 版本
                .version("1.0")
                // 许可
                .license("Apache 2.0 许可")
                // 许可链接
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                // 拓展
                .extensions(new ArrayList<>())
                .build();
    }
}