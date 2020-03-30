package club.banyuan.studyweb;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("club.banyuan.studyweb.dao")
public class StudywebApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudywebApplication.class, args);
    }

}
