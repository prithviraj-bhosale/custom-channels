
package com.morfeus.channels.model.preference.ghome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author Prithviraj Bhosale
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class GoogleHomeResponse implements Serializable {

  @JsonProperty("speech")
  private String speech;
  @JsonProperty("displayText")
  private String displayText;
  @JsonProperty("payload")
  private GoogleHomeResponsePayload payload = null;
  @JsonProperty("contextOut")
  private List<String> contextOut = null;
  @JsonIgnore
  private final static long serialVersionUID = -9034558408827619975L;

  public GoogleHomeResponse() {
  }

  public GoogleHomeResponse(String speech, String displayText, GoogleHomeResponsePayload payload, List<String> contextOut) {
    this.speech = speech;
    this.displayText = displayText;
    this.payload = payload;
    this.contextOut = contextOut;
  }

  public String getSpeech() {
    return speech;
  }

  public void setSpeech(String speech) {
    this.speech = speech;
  }

  public String getDisplayText() {
    return displayText;
  }

  public void setDisplayText(String displayText) {
    this.displayText = displayText;
  }

  public GoogleHomeResponsePayload getPayload() {
    return payload;
  }

  public void setPayload(GoogleHomeResponsePayload payload) {
    this.payload = payload;
  }

  public List<String> getContextOut() {
    return contextOut;
  }

  public void setContextOut(List<String> contextOut) {
    this.contextOut = contextOut;
  }

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  @Override
  public String toString() {
    return "GoogleHomeResponse{" + "speech='" + speech + '\'' + ", displayText='" + displayText + '\'' + ", payload=" + payload
        + ", contextOut=" + contextOut + '}';
  }
}

