package com.mymoviesapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MyMoviesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyMoviesApiApplication.class, args);
	}

}
