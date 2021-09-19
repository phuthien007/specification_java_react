package com.example.testjson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableSwagger2
public class TestJsonApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestJsonApplication.class, args);
    }



}
