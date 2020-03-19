package com.morfeus.channels;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication(scanBasePackages = {"com.morfeus"})
@Cacheable
public class ActiveChannelsSpringApplication {
  public static void main(String[] args) throws IOException, URISyntaxException {
    SpringApplication.run(ActiveChannelsSpringApplication.class, args);
    getConnection();
  }
  @Bean RestTemplate restTemplate() {
    return new RestTemplate();
  }

//  public static void check() throws IOException {
//    ObjectMapper objectMapper = new ObjectMapper();
//    Preference preference = objectMapper.readValue(new File("/Users/admin/Downloads/NtucSpringBootApplication/src/main/resources/mostPrefred.json"), Preference.class);
//    System.out.println(preference);
//  }
private static Jedis getConnection() throws URISyntaxException {
  URI redisURI = new URI("redis://h:p54ad225041ca21d44ec49548733619a2ccc7c69174d0066fe5e1f0aa1fd58bc9@ec2-54-165-98-171.compute-1.amazonaws.com:20489");
  Jedis jedis = new Jedis(redisURI);
  return jedis;
}
}


