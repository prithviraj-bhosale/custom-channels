package com.morfeus.channels.model.preference.ghome.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author Pritesh Soni
 * This class is used to get request from google home.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleHomeRequest implements Serializable {
  @JsonProperty("responseId")
  private String responseId;
  @JsonProperty("queryResult")
  private GoogleHomeRequestQueryResult queryResult;
  @JsonProperty("originalDetectIntentRequest")
  private GoogleHomeRequestOriginalDetectIntentRequest originalDetectIntentRequest;
  @JsonProperty("session")
  private String session;
  private final static long serialVersionUID = 8278960174437310743L;

  public GoogleHomeRequest() {
  }

  public GoogleHomeRequest(String responseId, GoogleHomeRequestQueryResult queryResult, GoogleHomeRequestOriginalDetectIntentRequest originalDetectIntentRequest,
      String session) {
    this.responseId = responseId;
    this.queryResult = queryResult;
    this.originalDetectIntentRequest = originalDetectIntentRequest;
    this.session = session;
  }

  public String getResponseId() {
    return responseId;
  }

  public void setResponseId(String responseId) {
    this.responseId = responseId;
  }

  public GoogleHomeRequestQueryResult getQueryResult() {
    return queryResult;
  }

  public void setQueryResult(GoogleHomeRequestQueryResult queryResult) {
    this.queryResult = queryResult;
  }

  public GoogleHomeRequestOriginalDetectIntentRequest getOriginalDetectIntentRequest() {
    return originalDetectIntentRequest;
  }

  public void setOriginalDetectIntentRequest(GoogleHomeRequestOriginalDetectIntentRequest originalDetectIntentRequest) {
    this.originalDetectIntentRequest = originalDetectIntentRequest;
  }

  public String getSession() {
    return session;
  }

  public void setSession(String session) {
    this.session = session;
  }

  @Override public String toString() {
    return "GoogleHomeRequest{" + "responseId='" + responseId + '\'' + ", queryResult=" + queryResult + ", originalDetectIntentRequest="
        + originalDetectIntentRequest + ", session='" + session + '\'' + '}';
  }
}
