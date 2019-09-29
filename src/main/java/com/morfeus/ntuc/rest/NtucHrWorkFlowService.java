package com.morfeus.ntuc.rest;


import ai.active.fulfillment.webhook.data.request.MorfeusWebhookRequest;
import ai.active.fulfillment.webhook.data.request.NlpV1;
import ai.active.fulfillment.webhook.data.response.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.morfeus.ntuc.model.preference.Prefered;
import com.morfeus.ntuc.model.preference.Preference;
import com.morfeus.ntuc.model.preference.model.Button;
import com.morfeus.ntuc.model.preference.model.CarousalTemplate;
import com.morfeus.ntuc.model.preference.model.CarouselMessage;
import com.morfeus.ntuc.model.preference.model.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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

  // Changes by Pritesh
  // final Active Channels changes


  // block card - card selection
  @PostMapping(path = "/blockcard/cards", consumes = "application/json", produces = "application/json")
  public MorfeusWebhookResponse getSelectedCard(@RequestBody(required = true) String body,
      @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    String customerId = request.getUser().getId() + "cardSelected";

    NlpV1 nlpV1 = (NlpV1) request.getNlp();
    String selectedCard = nlpV1.getData().get("payloadData").get("data").get("bank-type.bank-type").asText();
    redisTemplate.opsForValue().set(customerId, selectedCard);
    TextMessage textMessage = new TextMessage();
    textMessage.setContent("Please enter OK to proceed");
    textMessage.setType("text");
    MorfeusWebhookResponse messageWrapper = new MorfeusWebhookResponse();
    messageWrapper.setStatus(Status.SUCCESS);
    messageWrapper.setMessages(Arrays.asList(textMessage));
    return messageWrapper;
  }


  // block card - acknowledgement
  @PostMapping(path = "/blockcard/confirmation", consumes = "application/json", produces = "application/json")
  public MorfeusWebhookResponse confirmationCall(@RequestBody(required = true) String body,
      @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    String customerId = request.getUser().getId();
    System.out.println(customerId + "confirmation");
    NlpV1 nlpV1 = (NlpV1) request.getNlp();
    String confirmation = nlpV1.getData().get("payloadData").get("data").get("bank-name.bank-name").asText();
    CarouselMessage carouselMessage = new CarouselMessage();
    Content content = new Content();
    String selectedCard = redisTemplate.opsForValue().get(customerId + "cardSelected").toString();
    String base = "Your Request for " + selectedCard + " Block card has been";
    String actualTitle = "";
    if (confirmation.contains("CONFIRM")) {
      actualTitle = base + " blocked successfully.";
    } else {
      actualTitle = base + " cancelled.";
    }
    String image = "";
    if (selectedCard.contains("AXIS")) {
      image = "https://news.manikarthik.com/wp-content/uploads/Axis-Bank-Platinum-Credit-Card.png";
    } else if (selectedCard.contains("HDFC")) {
      image = "https://cards.jetprivilege.com/cards/HDFC-Jet-Privilege-World-DI-Card_final-24-10-17-011519069130907.jpg";
    } else {
      image = "https://image3.mouthshut.com/images/imagesp/925006383s.png";
    }

    content.setTitle(actualTitle);
    content.setImage(image);
    List<Content> contents = new ArrayList<>();
    contents.add(content);
    carouselMessage.setContent(contents);
    carouselMessage.setType("carousel");
    MorfeusWebhookResponse messageWrapper = new MorfeusWebhookResponse();
    messageWrapper.setMessages(Arrays.asList(carouselMessage));
    messageWrapper.setStatus(Status.SUCCESS);
    return messageWrapper;
  }


  // book ticket - source selection
  @PostMapping(path = "/bookticket/source", consumes = "application/json", produces = "application/json")
  public MorfeusWebhookResponse setSource(@RequestBody(required = true) String body,
      @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    String customerId = request.getUser().getId() + "source";

    NlpV1 nlpV1 = (NlpV1) request.getNlp();
    String selectedCard = nlpV1.getData().get("payloadData").get("data").get("source.source").asText();
    redisTemplate.opsForValue().set(customerId, selectedCard);
    TextMessage textMessage = new TextMessage();
    textMessage.setContent("Please confirm " + selectedCard + " as your source");
    textMessage.setType("text");
    MorfeusWebhookResponse messageWrapper = new MorfeusWebhookResponse();
    messageWrapper.setStatus(Status.SUCCESS);
    messageWrapper.setMessages(Arrays.asList(textMessage));
    return messageWrapper;
  }

  // book ticket - destination selection
  @PostMapping(path = "/bookticket/destination", consumes = "application/json", produces = "application/json")
  public MorfeusWebhookResponse setDestination(@RequestBody(required = true) String body,
      @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    String customerId = request.getUser().getId() + "destination";

    NlpV1 nlpV1 = (NlpV1) request.getNlp();
    String selectedCard = nlpV1.getData().get("payloadData").get("data").get("destination.destination").asText();
    redisTemplate.opsForValue().set(customerId, selectedCard);
    TextMessage textMessage = new TextMessage();
    textMessage.setContent("Please confirm " + selectedCard + " as your destination");
    textMessage.setType("text");
    MorfeusWebhookResponse messageWrapper = new MorfeusWebhookResponse();
    messageWrapper.setStatus(Status.SUCCESS);
    messageWrapper.setMessages(Arrays.asList(textMessage));
    return messageWrapper;
  }

  // book ticket - date selection
  @PostMapping(path = "/bookticket/date", consumes = "application/json", produces = "application/json")
  public MorfeusWebhookResponse setDate(@RequestBody(required = true) String body,
      @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    String customerId = request.getUser().getId() + "date";

    NlpV1 nlpV1 = (NlpV1) request.getNlp();
    String selectedCard = nlpV1.getData().get("payloadData").get("data").get("date.date").asText();
    redisTemplate.opsForValue().set(customerId, selectedCard);
    TextMessage textMessage = new TextMessage();
    textMessage.setContent("Please confirm the journey date: " + selectedCard);
    textMessage.setType("text");
    MorfeusWebhookResponse messageWrapper = new MorfeusWebhookResponse();
    messageWrapper.setStatus(Status.SUCCESS);
    messageWrapper.setMessages(Arrays.asList(textMessage));
    return messageWrapper;
  }

  // book ticket - class selection
  @PostMapping(path = "/bookticket/class", consumes = "application/json", produces = "application/json")
  public MorfeusWebhookResponse setClass(@RequestBody(required = true) String body,
      @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    String customerId = request.getUser().getId() + "class";

    NlpV1 nlpV1 = (NlpV1) request.getNlp();
    String selectedCard = nlpV1.getData().get("payloadData").get("data").get("class.class").asText();
    redisTemplate.opsForValue().set(customerId, selectedCard);
    TextMessage textMessage = new TextMessage();
    textMessage.setContent("Please confirm your ticket class: " + selectedCard + " class");
    textMessage.setType("text");
    MorfeusWebhookResponse messageWrapper = new MorfeusWebhookResponse();
    messageWrapper.setStatus(Status.SUCCESS);
    messageWrapper.setMessages(Arrays.asList(textMessage));
    return messageWrapper;
  }

  // book ticket - acknowledgement
  @PostMapping(path = "/bookticket/confirm", consumes = "application/json", produces = "application/json")
  public MorfeusWebhookResponse bookConfirmationCall(@RequestBody(required = true) String body,
      @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    String customerId = request.getUser().getId();
    System.out.println(customerId + "booking");
    NlpV1 nlpV1 = (NlpV1) request.getNlp();
    CarouselMessage carouselMessage = new CarouselMessage();
    Content content = new Content();

    String confirmation = nlpV1.getData().get("payloadData").get("data").get("confirm.confirm").asText();

    String source = redisTemplate.opsForValue().get(customerId + "source").toString();
    String destination = redisTemplate.opsForValue().get(customerId + "destination").toString();
    String date = redisTemplate.opsForValue().get(customerId + "date").toString();
    String bookingClass = redisTemplate.opsForValue().get(customerId + "class").toString();

    String base, image;
    if (StringUtils.startsWithIgnoreCase(confirmation, "y")) {
      base = "Your " + bookingClass + " class ticket for " + source + " to " + destination + " on " + date + " has been booked successfully.";
      image = "https://ukcareguide.co.uk/media/check-mark-green-tick-mark.png";
    } else {
      base = "Your " + bookingClass + " class ticket for " + source + " to " + destination + " on " + date + " was not booked.";
      image = "https://cdn.pixabay.com/photo/2012/04/12/13/15/red-29985_960_720.png";
    }

    content.setTitle(base);
    content.setImage(image);
    List<Content> contents = new ArrayList<>();
    contents.add(content);
    carouselMessage.setContent(contents);
    carouselMessage.setType("carousel");
    MorfeusWebhookResponse messageWrapper = new MorfeusWebhookResponse();
    messageWrapper.setMessages(Arrays.asList(carouselMessage));
    messageWrapper.setStatus(Status.SUCCESS);
    return messageWrapper;
  }

}
