package com.example.api;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EventApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventApplication.class, args);
    }

    @Bean // Bean 으로 등록하는 과정 , Bean 으로 등록이 되어 있어야 @Autowired 등을 사용해서 객체를 주입하고 쓸 수 있다.
    public ModelMapper modelMapper(){ // Bean 으로 등록되어 있지 않다면 @Autowired 를 쓸 수 없다.
        return new ModelMapper();
    }

}

