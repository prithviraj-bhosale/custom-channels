package com.morfeus.ntuc.rest;


import ai.active.fulfillment.webhook.data.request.MorfeusWebhookRequest;
import ai.active.fulfillment.webhook.data.response.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.morfeus.ntuc.model.preference.Prefered;
import com.morfeus.ntuc.model.preference.Preference;
import com.morfeus.ntuc.model.preference.model.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.morfeus.ntuc.model.preference.model.*;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

@RestController
public class NtucHrWorkFlowService {
    String username = null;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RestTemplate restTemplate;

  @Autowired
  private ResourceLoader resourceLoader;

  @Autowired
  private RedisTemplate redisTemplate;

  @PostMapping(path = "/ntuc/username/", consumes = "application/json", produces = "application/json")
  public MorfeusWebhookResponse getUserNameFromEntities(@RequestBody(required = true) String body,
                                                        @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    System.out.println(objectMapper.writeValueAsString(request ));
    Gson gson = new Gson();
    String username1 = request.getRequest().getText();
    System.out.println("Username : " + username1);
    String customerID = request.getUser().getCustomerId();
    System.out.println("CustomerID: "+ customerID);
    redisTemplate.opsForValue().set(customerID,username1);
    System.out.println("Stored in redis: "+ redisTemplate.opsForValue().get(customerID));
    String botUserNameResp = "Nice to know you, " + username1;
    return getTextMessageOnBot(botUserNameResp);

  }

  @PostMapping(path = "/ntuc/thankyounote/", consumes = "application/json", produces = "application/json")
  public MorfeusWebhookResponse getUserNameFromEntitiesForThankyou(@RequestBody(required = true) String body,
                                                                   @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    System.out.println("Thankyou  : " + objectMapper.writeValueAsString(request));
    String customerID = request.getUser().getCustomerId();
    String username = redisTemplate.opsForValue().get(customerID).toString();
    System.out.println("Username : " + username);
    String botUserNameResp = "Okay, thank you for sharing," + username + "!";
    TextMessage textMessage = new TextMessage();
    textMessage.setContent(botUserNameResp);
    textMessage.setType("text");
    TextMessage textMessage1= new TextMessage();
    textMessage1.setContent("How do stay motivated?");
    textMessage1.setType("text");
    MorfeusWebhookResponse messageWrapper = new MorfeusWebhookResponse();
    messageWrapper.setMessages(Arrays.asList(textMessage, textMessage1));
    messageWrapper.setStatus(Status.SUCCESS);
    System.out.println("Exiting getTextMessageOnBot: "+ objectMapper.writeValueAsString(messageWrapper));
    return messageWrapper;

  }

  public MorfeusWebhookResponse getTextMessageOnBot(String messageOnBot) throws JsonProcessingException {
    String message = "Have you been or is unable to fulfil any of your financial obligations, whether in Singapore or elsewhere (i.e. Judgment Debt, Debt Repayment Scheme, Bankrupt, others)?";
    TextMessage textMessage = new TextMessage();
    textMessage.setContent(messageOnBot);
    textMessage.setType("text");
    QuickReply quickReply = new QuickReply();
    quickReply.setTitle("Yes");
    quickReply.setPayload("{\"data\":{\"sys_confirmation\": \"yes\"}, \"intent\": \"txn-mainsection\"}");
    quickReply.setType("postback");
    QuickReply quickReply1 = new QuickReply();
    quickReply1.setTitle("No");
    quickReply1.setType("postback");
    quickReply1.setPayload("{\"data\":{\"sys_confirmation\": \"no\"}, \"intent\": \"txn-mainsection\"}");
    TextMessage textMessage1 = new TextMessage();
    textMessage1.setContent(message);
    textMessage1.setType("text");
    textMessage1.setQuickReplies(Arrays.asList(quickReply, quickReply1));
    MorfeusWebhookResponse messageWrapper = new MorfeusWebhookResponse();
    messageWrapper.setMessages(Arrays.asList(textMessage, textMessage1));
    messageWrapper.setStatus(Status.SUCCESS);
    System.out.println("Exiting getTextMessageOnBot: "+ objectMapper.writeValueAsString(messageWrapper));
    return messageWrapper;
  }


@PostMapping(path = "/ntuc/mostpreferred/", consumes = "application/json", produces = "application/json")
public MorfeusWebhookResponse getMostPreferredTemplate(@RequestBody(required = true) String body,
  @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
  MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
  System.out.println("Username : " + objectMapper.writeValueAsString(body));
  InputStream inputStream=this.getClass().getClassLoader().getResourceAsStream("mostPrefred.json");
//  InputStream inputStream = new FileInputStream("/Users/savinaysai/Desktop/Repo/ntuc/springBootApplication/NtucSpringBootApplication/src/main/resources/mostPrefred.json");
  Preference preference = objectMapper.readValue(inputStream, Preference.class);
  inputStream.close();
  System.out.println("preference: "+ preference);
  List<Button>  buttons = new ArrayList<>();
  for(Prefered prefered : preference.getPreferedList()) {
    Button button = new Button();
    button.setIntent("txn-mainsection");
    button.setText(prefered.getField());
    button.setPayload(prefered.getPayload());
    button.setType("text");
    buttons.add(button);
  }
  CarousalTemplate carousalTemplate = new CarousalTemplate();
  carousalTemplate.setButton(buttons);
  String payload = objectMapper.writeValueAsString(carousalTemplate);
  System.out.println("templatePayload: "+ objectMapper.writeValueAsString(carousalTemplate));
  MorfeusWebhookResponse messageWrapper = new MorfeusWebhookResponse();
  Map<String,Object> payloadMap = new HashMap<>();
  payloadMap.put("payload",payload);
  DynamicMessage dynamicMessage = new DynamicMessage();
  dynamicMessage.setContent(new CarouselMessage());
  dynamicMessage.setId("Preferred_Carousal");
  dynamicMessage.setType("dynamic");
  messageWrapper.setExtraData(payloadMap);
  messageWrapper.setStatus(Status.SUCCESS);
  messageWrapper.setMessages(Arrays.asList(dynamicMessage));
  return messageWrapper;
}

  @PostMapping(path = "/ntuc/second/", consumes = "application/json", produces = "application/json")
  public MorfeusWebhookResponse getSecondTemplate(@RequestBody(required = true) String body,
    @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    System.out.println("Username : " + objectMapper.writeValueAsString(body));
    //    InputStream inputStream = resourceLoader.getResource("classpath:mostPrefered/mostPrefred.json").getInputStream();
    InputStream inputStream = new FileInputStream("/Users/savinaysai/Desktop/Repo/ntuc/NtucSpringBootApplication/src/main/resources/mostPrefred.json");
    Preference preference = objectMapper.readValue(inputStream, Preference.class);
    inputStream.close();
    System.out.println("preference: "+ preference);
    List<Button>  buttons = new ArrayList<>();
    for(Prefered prefered : preference.getPreferedList()) {
      Button button = new Button();
      button.setIntent("txn-mainsection");
      button.setText(prefered.getField());
      button.setPayload(prefered.getPayload());
      button.setType("text");
      buttons.add(button);
    }
    CarousalTemplate carousalTemplate = new CarousalTemplate();
    carousalTemplate.setButton(buttons);
    carousalTemplate.setTitle("Second");
    String payload = objectMapper.writeValueAsString(carousalTemplate);
    System.out.println("templatePayload: "+ objectMapper.writeValueAsString(carousalTemplate));
    MorfeusWebhookResponse messageWrapper = new MorfeusWebhookResponse();
    Map<String,Object> payloadMap = new HashMap<>();
    payloadMap.put("payload",payload);
    DynamicMessage dynamicMessage = new DynamicMessage();
    dynamicMessage.setContent(new CarouselMessage());
    dynamicMessage.setId("Preferred_Carousal");
    dynamicMessage.setType("dynamic");
    messageWrapper.setExtraData(payloadMap);
    messageWrapper.setStatus(Status.SUCCESS);
    messageWrapper.setMessages(Arrays.asList(dynamicMessage));
    return messageWrapper;
  }

  @PostMapping(path = "/ntuc/uploadresume/", consumes = "application/json", produces = "application/json")
  public MorfeusWebhookResponse uploadResumeInScenario(@RequestBody(required = true) String body,
    @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    System.out.println("Upload Resume : " + objectMapper.writeValueAsString(body));
    MorfeusWebhookResponse messageWrapper = new MorfeusWebhookResponse();
    DynamicMessage dynamicMessage = new DynamicMessage();
    dynamicMessage.setType("dynamic");
    dynamicMessage.setContent(new CarouselMessage());
    dynamicMessage.setId("upload_resume");
    messageWrapper.setStatus(Status.SUCCESS);
    messageWrapper.setMessages(Arrays.asList(dynamicMessage));
    return messageWrapper;
  }

  @PostMapping(path = "/channels/ticket/confirmation", consumes = "application/json", produces = "application/json")
  public MorfeusWebhookResponse getResposne(@RequestBody(required = true) String body,
      @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    System.out.println("Username : " + objectMapper.writeValueAsString(body));
    InputStream inputStream=this.getClass().getClassLoader().getResourceAsStream("mostPrefred.json");
    Preference preference = objectMapper.readValue(inputStream, Preference.class);
    inputStream.close();
    System.out.println("preference: "+ preference);
    List<Button>  buttons = new ArrayList<>();
    for(Prefered prefered : preference.getPreferedList()) {
      Button button = new Button();
      button.setIntent("txn-mainsection");
      button.setText(prefered.getField());
      button.setPayload(prefered.getPayload());
      button.setType("text");
      buttons.add(button);
    }
    CarousalTemplate carousalTemplate = new CarousalTemplate();
    carousalTemplate.setButton(buttons);
    String payload = objectMapper.writeValueAsString(carousalTemplate);
    System.out.println("templatePayload: "+ objectMapper.writeValueAsString(carousalTemplate));
    MorfeusWebhookResponse messageWrapper = new MorfeusWebhookResponse();
    Map<String,Object> payloadMap = new HashMap<>();
    payloadMap.put("payload",payload);
    DynamicMessage dynamicMessage = new DynamicMessage();
    dynamicMessage.setContent(new CarouselMessage());
    dynamicMessage.setId("Preferred_Carousal");
    dynamicMessage.setType("dynamic");
    messageWrapper.setExtraData(payloadMap);
    messageWrapper.setStatus(Status.SUCCESS);
    messageWrapper.setMessages(Arrays.asList(dynamicMessage));
    return messageWrapper;
  }

}
