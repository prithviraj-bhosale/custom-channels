package com.morfeus.channels.rest;


import ai.active.fulfillment.webhook.data.request.MorfeusWebhookRequest;
import ai.active.fulfillment.webhook.data.request.NlpV1;
import ai.active.fulfillment.webhook.data.request.WorkflowParams;
import ai.active.fulfillment.webhook.data.response.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.morfeus.channels.model.preference.ghome.model.*;
import com.morfeus.channels.model.preference.model.CarouselMessage;
import com.morfeus.channels.model.preference.model.TextMessage;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
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
  private static final String SETINTENT = "actions.intent.OPTION";
  private static final String SYSTEM_INTENT_DATA_TYPE = "type.googleapis.com/google.actions.v2.OptionValueSpec";

  String username = null;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private RestTemplate restTemplate;

  @Autowired private ResourceLoader resourceLoader;

  @Autowired private RedisTemplate<String, String> redisTemplate;

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
    if (nlpV1.getData().get("payloadData") != null) {
      confirmation = nlpV1.getData().get("payloadData").get("data").get("bank-name.bank-name").asText();
    } else if (nlpV1.getData().get("maskedMessage") != null) {
      confirmation = nlpV1.getData().get("maskedMessage").asText();
    } else {
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
    if (workflowParameters.containsKey("bank_name_bank_name_Step_3")) {
      confirmation = workflowParameters.get("bank_name_bank_name_Step_3");
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
    if (nlpV1.getData().get("payloadData") != null) {
      selectedCard = nlpV1.getData().get("payloadData").get("data").get("source.source").asText();
    } else if (nlpV1.getData().get("maskedMessage") != null) {
      selectedCard = nlpV1.getData().get("maskedMessage").asText();
    } else {
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

  @PostMapping(path = "/blockcard/confirmation3", consumes = "application/json", produces = "application/json")
  public MorfeusWebhookResponse confirmationCall3(@RequestBody(required = true) String body,
      @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    WorkflowParams workflowParams = request.getWorkflowParams();
    Map<String, String> workflowParameters = workflowParams.getWorkflowVariables();
    Map<String, String> requestParams = workflowParams.getRequestVariables();
    String selectedCard = null, confirmation = null, cardBlockage = null;
    if (workflowParameters.containsKey("bank_type_bank_type_Step_1")) {
      selectedCard = workflowParameters.get("bank_type_bank_type_Step_1");
      System.out.println("PKS: Card identified");
    }
    if (workflowParameters.containsKey("bank_name_bank_name_Step_2")) {
      cardBlockage = workflowParameters.get("bank_name_bank_name_Step_2");
      System.out.println("PKS: Card Blockage identified");
    }
    if (workflowParameters.containsKey("destination_destination_Step_4")) {
      confirmation = workflowParameters.get("destination_destination_Step_4");
      System.out.println("PKS: Action identified");
    }
    String base = "Your Request for " + selectedCard + " Block card has been";
    String actualTitle = "";
    if (confirmation != null && confirmation.equalsIgnoreCase("confirm")) {
      base = "Your " + selectedCard + " card has been ";
      actualTitle = base + cardBlockage + "ly blocked.";
    } else {
      base = "Your Request for " + selectedCard + " Block card has been";
      actualTitle = base + " cancelled.";
    }
    String image = "";
    if (selectedCard != null && selectedCard.equalsIgnoreCase("7308")) {
      image =
          "https://images.aerlingus.com/resrc-origin/s=w340,pd2.6/o=80/https://www.aerlingus.com/media/images/content/aerclub/aer-credit-card-image1.png";
    } else if (selectedCard != null && selectedCard.equalsIgnoreCase("0000")) {
      image = "https://news.manikarthik.com/wp-content/uploads/Axis-Bank-Platinum-Credit-Card.png";
    } else if (selectedCard != null && selectedCard.equalsIgnoreCase("0001")) {
      image = "https://cards.jetprivilege.com/cards/HDFC-Jet-Privilege-World-DI-Card_final-24-10-17-011519069130907.jpg";
    } else if (selectedCard != null && selectedCard.equalsIgnoreCase("5678")) {
      image = "https://image3.mouthshut.com/images/imagesp/925006383s.png";
    }
    if (request.getRequest().getText() != null && request.getRequest().getText().equalsIgnoreCase("999999")) {
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
    } else {
      TextMessage textMessage = new TextMessage();
      textMessage.setContent("Wrong OTP detected" + " Please Reinitiate the Flow to Continue");
      textMessage.setType("text");
      MorfeusWebhookResponse messageWrapper = new MorfeusWebhookResponse();
      messageWrapper.setStatus(Status.SUCCESS);
      messageWrapper.setMessages(Arrays.asList(textMessage));
      return messageWrapper;
    }
  }

    @PostMapping(path = "/blockcard/ghome/confirmation3", consumes = "application/json", produces = "application/json")
    public GoogleHomeResponse ghomeConfirmation(@RequestBody(required = true) String body) throws Exception {
      String response = null;
      StringBuilder responseSentence = new StringBuilder();
        ObjectMapper mapper = new ObjectMapper();
      GoogleHomeResponseItem item1 = new GoogleHomeResponseItem();
      GoogleHomeResponseBasicCard basicCard = new GoogleHomeResponseBasicCard();
      List<GoogleHomeResponseItem> itemList = new ArrayList<>();
      GoogleHomeResponseSimpleResponse simpleResponse = new GoogleHomeResponseSimpleResponse();
      GoogleHomeResponseItem item = new GoogleHomeResponseItem();
      GoogleHomeResponse googleHomeResponse = new GoogleHomeResponse();
      item.setSimpleResponse(simpleResponse);
      GoogleHomeRequest request = mapper.readValue(body, GoogleHomeRequest.class);
      if (request.getOriginalDetectIntentRequest().getPayload() != null
          && request.getOriginalDetectIntentRequest().getPayload().getInputs() != null
          && request.getOriginalDetectIntentRequest().getPayload().getInputs().get(0) != null
          && request.getOriginalDetectIntentRequest().getPayload().getInputs().get(0).getIntent() != null && request
          .getOriginalDetectIntentRequest().getPayload().getInputs().get(0).getIntent().equalsIgnoreCase("actions.intent.OPTION")
          && request.getOriginalDetectIntentRequest().getPayload().getInputs().get(0).getRawInputs() != null
          && request.getOriginalDetectIntentRequest().getPayload().getInputs().get(0).getRawInputs().get(0) != null
          && request.getOriginalDetectIntentRequest().getPayload().getInputs().get(0).getArguments() != null
          && request.getOriginalDetectIntentRequest().getPayload().getInputs().get(0).getArguments().get(0).getTextValue() != null
          && request.getOriginalDetectIntentRequest().getPayload().getInputs().get(0).getArguments().get(0).getTextValue() != null
          && request.getOriginalDetectIntentRequest().getPayload().getInputs().get(0).getArguments().get(0).getName()
          .equalsIgnoreCase("OPTION")) {
        String key = request.getOriginalDetectIntentRequest().getPayload().getInputs().get(0).getArguments().get(0).getTextValue();
        if (key.contentEquals("Confirm")) {
          response = "Your card is successfully blocked";
          basicCard.setTitle("Your card is successfully blocked");
          basicCard.setSubtitle("Please save the reference id yWBW69DT for future reference.");
          GoogleHomeResponseImage image = new GoogleHomeResponseImage();
          image.setUrl("https://ukcareguide.co.uk/media/check-mark-green-tick-mark.png");
          image.setAccessibilityText("Your card is successfully blocked");
          basicCard.setImage(image);
          item1.setBasicCard(basicCard);
          basicCard.setImageDisplayOptions("CROPPED");
        } else if (key.contentEquals("Cancel")) {
          response = "The request has been cancelled, how else can I help you?";
        } else if (key.contentEquals("Deposits") || key.contentEquals("Accounts") || key.contentEquals("Loans") || key
            .contentEquals("Credit Cards")) {
         return createResponseforBalnaceEnquiry(request);
        }
      }
      simpleResponse.setTextToSpeech(response);
      itemList.add(item);
      if (item1!=null){
        itemList.add(item1);
      }
      GoogleHomeResponseGoogleRichResponse googleHomeResponseGoogleRichResponse = new GoogleHomeResponseGoogleRichResponse();
      googleHomeResponseGoogleRichResponse.setItems(itemList);
      GoogleHomeResponseDataGoogle google = new GoogleHomeResponseDataGoogle();
      google.setExpectUserResponse(true);
      google.setRichResponse(googleHomeResponseGoogleRichResponse);
      GoogleHomeResponsePayload data = new GoogleHomeResponsePayload();
      data.setGoogle(google);
      googleHomeResponse.setPayload(data);
      return googleHomeResponse;
    }


    private GoogleHomeResponse createResponseforBalnaceEnquiry(GoogleHomeRequest request){
      String key = request.getOriginalDetectIntentRequest().getPayload().getInputs().get(0).getArguments().get(0).getTextValue();
      List<GoogleHomeResponseListSelectItem> items = new ArrayList<>();
      GoogleHomeResponseSystemIntentData systemIntentData = new GoogleHomeResponseSystemIntentData();
      GoogleHomeResponseGoogleRichResponse googleHomeResponseGoogleRichResponse = new GoogleHomeResponseGoogleRichResponse();
      GoogleHomeResponseListSelect listSelect = new GoogleHomeResponseListSelect();
      GoogleHomeResponseDataGoogleSystemIntent systemIntent = new GoogleHomeResponseDataGoogleSystemIntent();
      GoogleHomeResponseItem richresponseitems = new GoogleHomeResponseItem();
      List<GoogleHomeResponseItem> itemList = new ArrayList<>();
      GoogleHomeResponseDataGoogle google = new GoogleHomeResponseDataGoogle();
      GoogleHomeResponsePayload data = new GoogleHomeResponsePayload();
      GoogleHomeResponse googleHomeResponse = new GoogleHomeResponse();

      if (key.contentEquals("Deposits")){
        GoogleHomeResponseSimpleResponse simpleResponse = new GoogleHomeResponseSimpleResponse();
        simpleResponse.setTextToSpeech("Here are the balances of your deposits");
        richresponseitems.setSimpleResponse(simpleResponse);
        itemList.add(richresponseitems);
        googleHomeResponseGoogleRichResponse.setItems(itemList);
        GoogleHomeResponseListSelectItem listSelectItem = new GoogleHomeResponseListSelectItem();
        GoogleHomeResponseListSelectItemOptionInfo optionInfo = new GoogleHomeResponseListSelectItemOptionInfo();
        listSelectItem.setTitle("Supersaver-234xxxxxxx4569");
        listSelectItem.setDescription("your deposit amount is $ 534.058");
        optionInfo.setKey("Supersaver-234xxxxxxx4569");
          listSelectItem.setOptionInfo(optionInfo);
        items.add(listSelectItem);


        GoogleHomeResponseListSelectItem listSelectItem1 = new GoogleHomeResponseListSelectItem();
        GoogleHomeResponseListSelectItemOptionInfo optionInfo1 = new GoogleHomeResponseListSelectItemOptionInfo();
        listSelectItem1.setTitle("Supersaver-234xxxxxxx4568");
        listSelectItem1.setDescription("your deposit amount is $ 400,068");
        optionInfo1.setKey("Supersaver-234xxxxxxx4568");
        listSelectItem1.setOptionInfo(optionInfo1);
        items.add(listSelectItem1);

        GoogleHomeResponseListSelectItem listSelectItem2 = new GoogleHomeResponseListSelectItem();
        GoogleHomeResponseListSelectItemOptionInfo optionInfo2 = new GoogleHomeResponseListSelectItemOptionInfo();
        listSelectItem2.setTitle("Supersaver-234xxxxxxx4567");
        listSelectItem2.setDescription("your deposit amount is $ 334,567.80");
        optionInfo2.setKey("Supersaver-234xxxxxxx4567 ");
        listSelectItem2.setOptionInfo(optionInfo2);
        items.add(listSelectItem2);

        GoogleHomeResponseListSelectItem listSelectItem3 = new GoogleHomeResponseListSelectItem();
        GoogleHomeResponseListSelectItemOptionInfo optionInfo3 = new GoogleHomeResponseListSelectItemOptionInfo();
        listSelectItem3.setTitle("Max Gainer-234xxxxxxx4566");
        listSelectItem3.setDescription("your deposit amount is $ 250,567");
        optionInfo3.setKey("Max Gainer-234xxxxxxx4566");
        listSelectItem3.setOptionInfo(optionInfo2);
        items.add(listSelectItem3 );
      }else if(key.contentEquals("Accounts")){
        GoogleHomeResponseSimpleResponse simpleResponse = new GoogleHomeResponseSimpleResponse();
        simpleResponse.setTextToSpeech("Here are the balances of your accounts");
        richresponseitems.setSimpleResponse(simpleResponse);
        itemList.add(richresponseitems);
        googleHomeResponseGoogleRichResponse.setItems(itemList);
        GoogleHomeResponseListSelectItem listSelectItem = new GoogleHomeResponseListSelectItem();
        GoogleHomeResponseListSelectItemOptionInfo optionInfo = new GoogleHomeResponseListSelectItemOptionInfo();
        listSelectItem.setTitle("Easy Access 789xxxxxxxx2016");
        listSelectItem.setDescription("your balance is $ 57,578.20");
        optionInfo.setKey("Easy Access 789xxxxxxxx2016");
        listSelectItem.setOptionInfo(optionInfo);
        items.add(listSelectItem);


        GoogleHomeResponseListSelectItem listSelectItem1 = new GoogleHomeResponseListSelectItem();
        GoogleHomeResponseListSelectItemOptionInfo optionInfo1 = new GoogleHomeResponseListSelectItemOptionInfo();
        listSelectItem1.setTitle("SavingsMax- 987xxxxxxxx1012");
        listSelectItem1.setDescription("your balance is $ 23,456.20");
        optionInfo1.setKey("SavingsMax- 987xxxxxxxx1012");
        listSelectItem1.setOptionInfo(optionInfo1);
        items.add(listSelectItem1);

        GoogleHomeResponseListSelectItem listSelectItem2 = new GoogleHomeResponseListSelectItem();
        GoogleHomeResponseListSelectItemOptionInfo optionInfo2 = new GoogleHomeResponseListSelectItemOptionInfo();
        listSelectItem2.setTitle("Traditional IRA- 100xxxxxxxx2021");
        listSelectItem2.setDescription("your balance is $ 22,000.00");
        optionInfo2.setKey("Traditional IRA- 100xxxxxxxx2021");
        listSelectItem2.setOptionInfo(optionInfo2);
        items.add(listSelectItem2);

        GoogleHomeResponseListSelectItem listSelectItem3 = new GoogleHomeResponseListSelectItem();
        GoogleHomeResponseListSelectItemOptionInfo optionInfo3 = new GoogleHomeResponseListSelectItemOptionInfo();
        listSelectItem3.setTitle("Premium- 789xxxxxxx2018");
        listSelectItem3.setDescription("your balance is $ 17,878.00");
        optionInfo3.setKey("Premium- 789xxxxxxx2018");
        listSelectItem3.setOptionInfo(optionInfo2);
        items.add(listSelectItem3 );
      }else if(key.contentEquals("Credit Cards")){
        GoogleHomeResponseSimpleResponse simpleResponse = new GoogleHomeResponseSimpleResponse();
        simpleResponse.setTextToSpeech("Here are the balances for your cards");
        richresponseitems.setSimpleResponse(simpleResponse);
        itemList.add(richresponseitems);
        googleHomeResponseGoogleRichResponse.setItems(itemList);
        GoogleHomeResponseListSelectItem listSelectItem = new GoogleHomeResponseListSelectItem();
        GoogleHomeResponseListSelectItemOptionInfo optionInfo = new GoogleHomeResponseListSelectItemOptionInfo();
        listSelectItem.setTitle("Titanium- 6987xxxxxxxx4575");
        listSelectItem.setDescription("your total outstanding amount is $25000.00 and due date for the same is 08-06-2019");
        optionInfo.setKey("Titanium- 6987xxxxxxxx4575");
        listSelectItem.setOptionInfo(optionInfo);
        items.add(listSelectItem);


        GoogleHomeResponseListSelectItem listSelectItem1 = new GoogleHomeResponseListSelectItem();
        GoogleHomeResponseListSelectItemOptionInfo optionInfo1 = new GoogleHomeResponseListSelectItemOptionInfo();
        listSelectItem1.setTitle("Platinum Edge- 5187xxxxxxxx4571");
        listSelectItem1.setDescription("your total outstanding amount is $2130.00 and due date for the same is 07-15-2019");
        optionInfo1.setKey("Platinum Edge- 5187xxxxxxxx4571");
        listSelectItem1.setOptionInfo(optionInfo1);
        items.add(listSelectItem1);

        GoogleHomeResponseListSelectItem listSelectItem2 = new GoogleHomeResponseListSelectItem();
        GoogleHomeResponseListSelectItemOptionInfo optionInfo2 = new GoogleHomeResponseListSelectItemOptionInfo();
        listSelectItem2.setTitle("Platinum Edge- 5187xxxxxxxx4572");
        listSelectItem2.setDescription("your total outstanding amount is $2130.00 and due date for the same is 07-15-2019");
        optionInfo2.setKey("Platinum Edge- 5187xxxxxxxx4572");
        listSelectItem2.setOptionInfo(optionInfo2);
        items.add(listSelectItem2);

        GoogleHomeResponseListSelectItem listSelectItem3 = new GoogleHomeResponseListSelectItem();
        GoogleHomeResponseListSelectItemOptionInfo optionInfo3 = new GoogleHomeResponseListSelectItemOptionInfo();
        listSelectItem3.setTitle("Platinum Edge- 5187xxxxxxxx4573");
        listSelectItem3.setDescription("your total outstanding amount is $2130.00 and due date for the same is 07-15-2019");
        optionInfo3.setKey("Platinum Edge- 5187xxxxxxxx4573");
        listSelectItem3.setOptionInfo(optionInfo2);
        items.add(listSelectItem3 );
      }else if(key.contentEquals("Loans")){
        GoogleHomeResponseSimpleResponse simpleResponse = new GoogleHomeResponseSimpleResponse();
        simpleResponse.setTextToSpeech("Here are the outstanding balances");
        richresponseitems.setSimpleResponse(simpleResponse);
        itemList.add(richresponseitems);
        googleHomeResponseGoogleRichResponse.setItems(itemList);
        GoogleHomeResponseListSelectItem listSelectItem = new GoogleHomeResponseListSelectItem();
        GoogleHomeResponseListSelectItemOptionInfo optionInfo = new GoogleHomeResponseListSelectItemOptionInfo();
        listSelectItem.setTitle("Getmoney - 321xxxxxxxx4571");
        listSelectItem.setDescription("your outstanding loan/Mortgage amount is $ 451,343.53");
        optionInfo.setKey("Getmoney - 321xxxxxxxx4571");
        listSelectItem.setOptionInfo(optionInfo);
        items.add(listSelectItem);


        GoogleHomeResponseListSelectItem listSelectItem1 = new GoogleHomeResponseListSelectItem();
        GoogleHomeResponseListSelectItemOptionInfo optionInfo1 = new GoogleHomeResponseListSelectItemOptionInfo();
        listSelectItem1.setTitle("Pay easy - 321xxxxxxxx4569");
        listSelectItem1.setDescription("your total outstanding amount is $345,678.00");
        optionInfo1.setKey("Pay easy - 321xxxxxxxx4569");
        listSelectItem1.setOptionInfo(optionInfo1);
        items.add(listSelectItem1);

        GoogleHomeResponseListSelectItem listSelectItem2 = new GoogleHomeResponseListSelectItem();
        GoogleHomeResponseListSelectItemOptionInfo optionInfo2 = new GoogleHomeResponseListSelectItemOptionInfo();
        listSelectItem2.setTitle("HomeSaver - 321xxxxxxxx4570");
        listSelectItem2.setDescription("your total outstanding amount is $340,089.00");
        optionInfo2.setKey("HomeSaver - 321xxxxxxxx4570");
        listSelectItem2.setOptionInfo(optionInfo2);
        items.add(listSelectItem2);

      GoogleHomeResponseListSelectItem listSelectItem3 = new GoogleHomeResponseListSelectItem();
        GoogleHomeResponseListSelectItemOptionInfo optionInfo3 = new GoogleHomeResponseListSelectItemOptionInfo();
        listSelectItem3.setTitle("HELOC - 321xxxxxxxx4568");
        listSelectItem3.setDescription("your total outstanding amount is $234,578.20");
        optionInfo3.setKey("HELOC - 321xxxxxxxx4568");
        listSelectItem3.setOptionInfo(optionInfo2);
        items.add(listSelectItem3 );
      }
      listSelect.setItems(items);
      systemIntentData.setListSelect(listSelect);
      systemIntentData.setType(SYSTEM_INTENT_DATA_TYPE);
      systemIntent.setIntent(SETINTENT);
      systemIntent.setData(systemIntentData);
      googleHomeResponseGoogleRichResponse.setItems(itemList);
      google.setExpectUserResponse(true);
      google.setRichResponse(googleHomeResponseGoogleRichResponse);
      google.setSystemIntent(systemIntent);
      data.setGoogle(google);
      googleHomeResponse.setPayload(data);
      return googleHomeResponse;
    }

}
