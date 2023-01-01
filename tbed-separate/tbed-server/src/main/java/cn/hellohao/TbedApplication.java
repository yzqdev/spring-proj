package cn.hellohao;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.unit.DataSize;

@SpringBootApplication
@EnableScheduling
//@ServletComponentScan
//@EnableTransactionManagement(proxyTargetClass = true)
public class TbedApplication {

public static void main(String[] args) {
    SpringApplication.run(TbedApplication.class, args);
    }

}

