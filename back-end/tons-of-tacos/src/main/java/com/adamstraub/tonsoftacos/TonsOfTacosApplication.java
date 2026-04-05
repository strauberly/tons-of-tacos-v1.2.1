package com.adamstraub.tonsoftacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication
@EnableScheduling
public class TonsOfTacosApplication {
	public static void main (String[]args){
		SpringApplication.run(TonsOfTacosApplication.class, args);
	}
}




