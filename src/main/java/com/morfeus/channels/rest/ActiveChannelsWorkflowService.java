package com.morfeus.channels.rest;


import ai.active.fulfillment.webhook.data.request.MorfeusWebhookRequest;
import ai.active.fulfillment.webhook.data.request.NlpV1;
import ai.active.fulfillment.webhook.data.request.WorkflowParams;
import ai.active.fulfillment.webhook.data.response.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.morfeus.channels.model.preference.model.CarouselMessage;
import com.morfeus.channels.model.preference.model.TextMessage;
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
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
public class ActiveChannelsWorkflowService {
    String username = null;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RestTemplate restTemplate;

  @Autowired
  private ResourceLoader resourceLoader;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  // Changes by Pritesh
  // final Active Channels changes


  // block card - card selection
  @PostMapping(path = "/blockcard/cards", consumes = "application/json", produces = "application/json")
  public MorfeusWebhookResponse getSelectedCard(@RequestBody(required = true) String body,
      @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    String customerId = request.getUser().getId() + "cardSelected";
    String selectedCard = null;
    NlpV1 nlpV1 = (NlpV1) request.getNlp();
    if (nlpV1.getData().get("payloadData") != null) {
      selectedCard = nlpV1.getData().get("payloadData").get("data").get("bank-type.bank-type").asText();
    } else if (nlpV1.getData().get("maskedMessage") != null) {
      selectedCard = nlpV1.getData().get("maskedMessage").asText();
    } else {
      selectedCard = request.getRequest().getText();
    }
    customerId += request.getBot().getChannelId();
    System.out.println("Redis key for card selection -> " + customerId);
    System.out.println("Redis value for card selection -> " + selectedCard);
    redisTemplate.opsForValue().set(customerId, selectedCard, 5, TimeUnit.MINUTES);
    TextMessage textMessage = new TextMessage();
    textMessage.setContent("Please enter OK to proceed");
    textMessage.setType("text");
    MorfeusWebhookResponse messageWrapper = new MorfeusWebhookResponse();
    messageWrapper.setStatus(Status.SUCCESS);
    messageWrapper.setMessages(Arrays.asList(textMessage));
    return messageWrapper;
  }


  // block card - acknowledgement - redis impl
  @PostMapping(path = "/blockcard/confirmation", consumes = "application/json", produces = "application/json")
  public MorfeusWebhookResponse confirmationCall(@RequestBody(required = true) String body,
      @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    String customerId = request.getUser().getId();
    System.out.println(customerId + "confirmation");
    String confirmation = null;
    NlpV1 nlpV1 = (NlpV1) request.getNlp();
    if (nlpV1.getData().get("payloadData")!=null){
       confirmation = nlpV1.getData().get("payloadData").get("data").get("bank-name.bank-name").asText();
    }else if (nlpV1.getData().get("maskedMessage")!=null){
      confirmation =nlpV1.getData().get("maskedMessage").asText();
    }else{
      confirmation = request.getRequest().getText();
    }
    CarouselMessage carouselMessage = new CarouselMessage();
    Content content = new Content();
    customerId = customerId + "cardSelected" + request.getBot().getChannelId();
    System.out.println("Redis key for card selection -> " + customerId);
    String selectedCard = redisTemplate.opsForValue().get(customerId + "cardSelected" + request.getBot().getChannelId()).toString();
    String base = "Your Request for " + selectedCard + " Block card has been";
    String actualTitle = "";
    if (confirmation.contains("CONFIRM") || confirmation.equalsIgnoreCase("confirm")) {
      actualTitle = base + " blocked successfully.";
    } else {
      actualTitle = base + " cancelled.";
    }
    String image = "";
    if (selectedCard.contains("AXIS") || selectedCard.equalsIgnoreCase("axis")) {
      image = "https://news.manikarthik.com/wp-content/uploads/Axis-Bank-Platinum-Credit-Card.png";
    } else if (selectedCard.contains("HDFC") || selectedCard.equalsIgnoreCase("hdfc")) {
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

  // block card - acknowledgement - non-redis workflow impl
  @PostMapping(path = "/blockcard/confirmation2", consumes = "application/json", produces = "application/json")
  public MorfeusWebhookResponse confirmationCall2(@RequestBody(required = true) String body,
      @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    WorkflowParams workflowParams = request.getWorkflowParams();
    Map<String, String> workflowParameters = workflowParams.getWorkflowVariables();
    Map<String, String> requestParams = workflowParams.getRequestVariables();
    String selectedCard = null, confirmation = null;
    if (workflowParameters.containsKey("bank_type_bank_type_Step_1")) {
      selectedCard = workflowParameters.get("bank_type_bank_type_Step_1");
      System.out.println("PKS: Card identified -> " + selectedCard);
    }
    if (requestParams.containsKey("bank_name_bank_name")) {
      confirmation = workflowParameters.get("bank_name_bank_name");
      System.out.println("PKS: Action identified -> " + confirmation);
    }
    String base = "Your Request for " + selectedCard + " Block card has been";
    String actualTitle = "";
    if ((confirmation != null) && (confirmation.equalsIgnoreCase("confirm") || confirmation.equalsIgnoreCase("CONFIRM"))) {
      base = "Your " + selectedCard + " card has been";
      actualTitle = base + " blocked successfully.";
    } else {
      base = "Your Request for " + selectedCard + " Block card has been";
      actualTitle = base + " cancelled.";
    }
    String image = "";
    if (selectedCard != null && selectedCard.equalsIgnoreCase("axis")) {
      image = "https://news.manikarthik.com/wp-content/uploads/Axis-Bank-Platinum-Credit-Card.png";
    } else if (selectedCard != null && selectedCard.equalsIgnoreCase("hdfc")) {
      image = "https://cards.jetprivilege.com/cards/HDFC-Jet-Privilege-World-DI-Card_final-24-10-17-011519069130907.jpg";
    } else {
      image = "https://image3.mouthshut.com/images/imagesp/925006383s.png";
    }
    Content content = new Content();
    content.setTitle(actualTitle);
    content.setImage(image);
    List<Content> contents = new ArrayList<>();
    contents.add(content);
    CarouselMessage carouselMessage = new CarouselMessage();
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
    String selectedCard = null;
    if (nlpV1.getData().get("payloadData")!=null){
    selectedCard = nlpV1.getData().get("payloadData").get("data").get("source.source").asText();
    }else if(nlpV1.getData().get("maskedMessage")!=null){
      selectedCard= nlpV1.getData().get("maskedMessage").asText();
    }else{
      selectedCard = request.getRequest().getText();
    }
    redisTemplate.opsForValue().set(customerId, selectedCard, 5, TimeUnit.MINUTES);
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
    String selectedCard = null;
    NlpV1 nlpV1 = (NlpV1) request.getNlp();
    if (nlpV1.getData().get("payloadData") != null) {
      selectedCard = nlpV1.getData().get("payloadData").get("data").get("destination.destination").asText();
    } else if (nlpV1.getData().get("maskedMessage") != null) {
      selectedCard = nlpV1.getData().get("maskedMessage").asText();
    } else {
      selectedCard = request.getRequest().getText();
    }
    redisTemplate.opsForValue().set(customerId, selectedCard, 5, TimeUnit.MINUTES);
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
    String selectedCard;
    NlpV1 nlpV1 = (NlpV1) request.getNlp();
    if (nlpV1.getData().get("payloadData") != null) {
      selectedCard = nlpV1.getData().get("payloadData").get("data").get("date.date").asText();
    } else if (nlpV1.getData().get("maskedMessage") != null) {
      selectedCard = nlpV1.getData().get("maskedMessage").asText();
    } else {
      selectedCard = request.getRequest().getText();
    }
    redisTemplate.opsForValue().set(customerId, selectedCard, 5, TimeUnit.MINUTES);
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
    String selectedCard = null;
    NlpV1 nlpV1 = (NlpV1) request.getNlp();
    if (nlpV1.getData().get("payloadData") != null) {
      selectedCard = nlpV1.getData().get("payloadData").get("data").get("class.class").asText();
    } else if (nlpV1.getData().get("maskedMessage") != null) {
      selectedCard = nlpV1.getData().get("maskedMessage").asText();
    } else {
      selectedCard = request.getRequest().getText();
    }
    redisTemplate.opsForValue().set(customerId, selectedCard, 5, TimeUnit.MINUTES);
    TextMessage textMessage = new TextMessage();
    textMessage.setContent("Please confirm your ticket class: " + selectedCard + " class");
    textMessage.setType("text");
    MorfeusWebhookResponse messageWrapper = new MorfeusWebhookResponse();
    messageWrapper.setStatus(Status.SUCCESS);
    messageWrapper.setMessages(Arrays.asList(textMessage));
    return messageWrapper;
  }

  // book ticket - acknowledgement redis impl
  @PostMapping(path = "/bookticket/confirm", consumes = "application/json", produces = "application/json")
  public MorfeusWebhookResponse bookConfirmationCall(@RequestBody(required = true) String body,
      @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    String customerId = request.getUser().getId();
    System.out.println(customerId + "booking");
    NlpV1 nlpV1 = (NlpV1) request.getNlp();
    CarouselMessage carouselMessage = new CarouselMessage();
    Content content = new Content();
    String confirmation = null;
    if (nlpV1.getData().get("payloadData") != null) {
      confirmation = nlpV1.getData().get("payloadData").get("data").get("confirm.confirm").asText();
    } else if (nlpV1.getData().get("maskedMessage") != null) {
      confirmation = nlpV1.getData().get("maskedMessage").asText();
    } else {
      confirmation = request.getRequest().getText();
    }
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


  // book ticket - acknowledgement non-redis impl
  @PostMapping(path = "/bookticket/confirm2", consumes = "application/json", produces = "application/json")
  public MorfeusWebhookResponse bookConfirmationCall2(@RequestBody(required = true) String body,
      @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    String customerId = request.getUser().getId();
    System.out.println(customerId + "booking");
    CarouselMessage carouselMessage = new CarouselMessage();
    Content content = new Content();

    WorkflowParams workflowParams = request.getWorkflowParams();
    Map<String, String> workflowParameters = workflowParams.getWorkflowVariables();
    Map<String, String> requestParams = workflowParams.getRequestVariables();
    String source = null, destination = null, date = null, classs = null, confirmation = null;

    if (workflowParameters.containsKey("source_source_Step_1")) {
      source = workflowParameters.get("source_source_Step_1");
      System.out.println("PKS: Source identified");
    }
    if (workflowParameters.containsKey("destination_destination_Step_3")) {
      destination = workflowParameters.get("destination_destination_Step_3");
      System.out.println("PKS: Destination identified");
    }
    if (workflowParameters.containsKey("date_date_Step_5")) {
      date = workflowParameters.get("date_date_Step_5");
      System.out.println("PKS: Date identified");
    }
    if (workflowParameters.containsKey("class_class_Step_8")) {
      classs = workflowParameters.get("class_class_Step_8");
      System.out.println("PKS: Class identified");
    }
    if (workflowParameters.containsKey("confirm_confirm_Step_10")) {
      confirmation = workflowParameters.get("confirm_confirm_Step_10");
      System.out.println("PKS: Confirmation identified");
    }

    String base, image;
    if (StringUtils.startsWithIgnoreCase(confirmation, "y")) {
      base = "Your " + classs + " class ticket for " + source + " to " + destination + " on " + date + " has been booked successfully.";
      image = "https://ukcareguide.co.uk/media/check-mark-green-tick-mark.png";
    } else {
      base = "Your " + classs + " class ticket for " + source + " to " + destination + " on " + date + " was not booked.";
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
