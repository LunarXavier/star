package org.xavier.star;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = {"org.xavier.star"})
@EnableCaching
@EnableScheduling
@EnableTransactionManagement
@MapperScan("org.xavier.star.mapper")
public class StarApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarApplication.class, args);
    }

}
