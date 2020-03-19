package com.morfeus.channels.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import requestObject.Request;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class StandAloneApplication {

  String username = null;
  @Autowired private ObjectMapper objectMapper;


  @Autowired private RedisTemplate<String, String> redisTemplate;

  @Autowired private RedisConnectionFactory jedisConnectionFactory;


  /*@PostMapping(path = "/morfeus/whatsapp", consumes = "application/json",produces = "application/json")
  public void acceptRequest( @RequestBody(required = true) String body,HttpServletResponse httpServletResponse) throws Exception {
    String url=null;
    Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    Request req= objectMapper.readValue(body,Request.class);
    LOGGER.log(Level.INFO, "after request ");
    LOGGER.log(Level.INFO, "after request ");
   // String mobileNumber=req.getTo().getNumber();
    try{
      LOGGER.log(Level.INFO, "in try block");
     // url=redisTemplate.opsForValue().get(mobileNumber);
      if(!url.isEmpty()) {
        LOGGER.log(Level.INFO, "in if block");
        postRequest(body, url);
      }else {
        LOGGER.log(Level.INFO, "in else block");
        //mobileNumber = "918147953938";
        url = "https://router.triniti.ai/fb-flow/ml9zupdo6k/morfeus/v1/channels/177wn22408692407/message";
        postRequest(body , url);
      }
    }
    catch(Exception e)
    {
      LOGGER.log(Level.SEVERE, e.toString());
    }
    LOGGER.log(Level.INFO, "END OF ACCEPTREQUEST FUNCTION");
  }

  private void postRequest(String body,String urlToRedirect) throws IOException {
    OkHttpClient client = new OkHttpClient();
    Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    Response response=null;
    try {
      MediaType mediaType = MediaType.parse("application/json");
      com.squareup.okhttp.RequestBody requestBody = com.squareup.okhttp.RequestBody.create(mediaType, body);
      com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder().url(urlToRedirect).method("POST", requestBody).addHeader("Content-Type", "application/json").build();
      response = client.newCall(request).execute();
    }
    catch (IOException e)
    {
      System.out.println(e);
      LOGGER.log(Level.SEVERE, (Supplier<String>) e);
    }
    finally{
      if(response!=null && response.isSuccessful())
      {
        response.body().close();
        LOGGER.log(Level.INFO,"redirected to url---> "+urlToRedirect);
      }
      else if(response==null)
        LOGGER.log(Level.INFO,"Failed to post! ");
    }
    LOGGER.log(Level.INFO, "END OF POSTREQUEST FUNCTION");
  }

  @RequestMapping(path = "/morfeus/whatsapp/manage", consumes = "application/json",produces = "application/json")
  private void addNumber(@RequestBody(required = true) String body,HttpServletResponse httpServletResponse,
      @RequestParam(value = "number",required = false) String number)
  {
    String[] keyValue=body.split(":");
    Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
   // String mobileNumber = keyValue[0].trim().replaceAll("\\{", "").replaceAll("\"", "");
   // JsonObject jsonResponse = new com.google.gson.JsonParser().parse(body).getAsJsonObject();
    String newUrl = "https://router.triniti.ai/fb-flow/ml9zupdo6k/morfeus/v1/channels/177wn22408692407/message";
     String mobileNumber = "918147953938";
    //String url = keyValue[1].trim().replaceAll("\\}", "").replaceAll("\"", "");
    try{
      if(number != null && number.isEmpty()==false) {
        number=number.replaceAll("\"","");
        boolean result=redisTemplate.delete(number);
        if(result==false)
          LOGGER.log(Level.INFO,number+" NUMBER NOT PRESENT IN REDIS");
        else LOGGER.log(Level.INFO,number+" NUMBER RECORD DELETED");
      }
      else
      {
        if (!Objects.nonNull(redisTemplate.opsForValue().get(mobileNumber))) {
          redisTemplate.opsForValue().set(mobileNumber, newUrl);
          LOGGER.log(Level.INFO,"ADDED "+ mobileNumber);
        }
        else{
          redisTemplate.opsForValue().set(mobileNumber, newUrl);
          LOGGER.log(Level.INFO,"UPDATED "+ mobileNumber);
        }
      }
    }
    catch(Exception e)
    {
      LOGGER.log(Level.SEVERE, (Supplier<String>) e);
    }
  }
}*/
}
