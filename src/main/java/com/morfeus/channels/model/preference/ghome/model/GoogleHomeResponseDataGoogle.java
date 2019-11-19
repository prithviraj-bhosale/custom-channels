package com.morfeus.channels.model.preference.ghome.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author Pritesh Soni
 * This class is only used as a component of GoogleHomeResponseData class
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleHomeResponseDataGoogle implements Serializable {
  @JsonProperty("expectUserResponse")
  private boolean expectUserResponse;

  @JsonProperty("richResponse")
  private GoogleHomeResponseGoogleRichResponse richResponse = null;

  @JsonProperty("systemIntent")
  private GoogleHomeResponseDataGoogleSystemIntent systemIntent = null;

  @JsonProperty("userStorage")
  String userStorage = null;

  public GoogleHomeResponseDataGoogle() {
  }

  public GoogleHomeResponseDataGoogle(boolean expectUserResponse, GoogleHomeResponseGoogleRichResponse richResponse,
      GoogleHomeResponseDataGoogleSystemIntent systemIntent, String userStorage) {
    this.expectUserResponse = expectUserResponse;
    this.richResponse = richResponse;
    this.systemIntent = systemIntent;
    this.userStorage = userStorage;
  }

  public boolean isExpectUserResponse() {
    return expectUserResponse;
  }

  public void setExpectUserResponse(boolean expectUserResponse) {
    this.expectUserResponse = expectUserResponse;
  }

  public GoogleHomeResponseGoogleRichResponse getRichResponse() {
    return richResponse;
  }

  public void setRichResponse(GoogleHomeResponseGoogleRichResponse richResponse) {
    this.richResponse = richResponse;
  }

  public GoogleHomeResponseDataGoogleSystemIntent getSystemIntent() {
    return systemIntent;
  }

  public void setSystemIntent(GoogleHomeResponseDataGoogleSystemIntent systemIntent) {
    this.systemIntent = systemIntent;
  }

  public String getUserStorage() {
    return userStorage;
  }

  public void setUserStorage(String userStorage) {
    this.userStorage = userStorage;
  }
}
