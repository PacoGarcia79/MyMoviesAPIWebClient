package com.mymoviesapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateClient {
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
        
    @Bean
    HttpEntity<?> httpEntity() {
    	HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(
				"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyYmE3NzJkNmQ0NzdlZmZkNTQyN2I4YmQxOTMwMThkMCIsInN1YiI6IjYzMTVhYzhmMGMxMjU1MDA3YjhmZDgxMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.mEmubDDIITjWk36DdeAb7LKwYnTcVIC86YQRYuWkLI4");
		return new HttpEntity(headers);
    }
}
