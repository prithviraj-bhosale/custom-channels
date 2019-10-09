package com.morfeus.channels;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@SpringBootApplication(scanBasePackages = {"com.morfeus"})
@Cacheable
public class ActiveChannelsSpringApplication {
  public static void main(String[] args) throws IOException {
    SpringApplication.run(ActiveChannelsSpringApplication.class, args);
//    check();
  }
  @Bean RestTemplate restTemplate() {
    return new RestTemplate();
  }

//  public static void check() throws IOException {
//    ObjectMapper objectMapper = new ObjectMapper();
//    Preference preference = objectMapper.readValue(new File("/Users/admin/Downloads/NtucSpringBootApplication/src/main/resources/mostPrefred.json"), Preference.class);
//    System.out.println(preference);
//  }
}


