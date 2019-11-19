package com.morfeus.channels.model.preference.ghome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author Prithviraj Bhosale
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleHomeResponseDataGoogleSystemIntent implements Serializable
{

  @JsonProperty("intent")
  private String intent;
  @JsonProperty("data")
  private GoogleHomeResponseSystemIntentData data;
  @JsonIgnore
  private final static long serialVersionUID = 6315216136747557131L;

  public GoogleHomeResponseDataGoogleSystemIntent() {
  }

  public GoogleHomeResponseDataGoogleSystemIntent(String intent, GoogleHomeResponseSystemIntentData data) {
    this.intent = intent;
    this.data = data;
  }

  public String getIntent() {
    return intent;
  }

  public void setIntent(String intent) {
    this.intent = intent;
  }

  public GoogleHomeResponseSystemIntentData getData() {
    return data;
  }

  public void setData(GoogleHomeResponseSystemIntentData data) {
    this.data = data;
  }

  @Override public String toString() {
    return "GoogleHomeResponseDataGoogleSystemIntent{" + "intent='" + intent + '\'' + ", data=" + data + '}';
  }
}
