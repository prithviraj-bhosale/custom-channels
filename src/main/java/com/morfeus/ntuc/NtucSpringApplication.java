package com.morfeus.ntuc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.morfeus.ntuc.model.preference.Preference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication(scanBasePackages = {"com.morfeus"})
@Cacheable
public class NtucSpringApplication {
  public static void main(String[] args) throws IOException {
    SpringApplication.run(NtucSpringApplication.class, args);
    check();
  }
  @Bean RestTemplate restTemplate() {
    return new RestTemplate();
  }

  public static void check() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    Preference preference = objectMapper.readValue(new File("/Users/admin/Downloads/NtucSpringBootApplication/src/main/resources/mostPrefred.json"), Preference.class);
    System.out.println(preference);
  }
}


