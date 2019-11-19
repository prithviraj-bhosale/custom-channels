package com.morfeus.channels.model.preference.ghome.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author Pritesh Soni
 * This class is only used as a component of GoogleHomeOriginalRequestData Class
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleHomeOriginalRequestDataInput implements Serializable {
  @JsonProperty("rawInputs")
  private List<GoogleHomeOriginalRequestDataInputRaw> rawInputs;
  @JsonProperty("arguments")
  private List<GoogleHomeRequestArgument> arguments = null;
  @JsonProperty("intent")
  private String intent;

  public GoogleHomeOriginalRequestDataInput() {
  }

  public GoogleHomeOriginalRequestDataInput(List<GoogleHomeOriginalRequestDataInputRaw> rawInputs, List<GoogleHomeRequestArgument> arguments,
      String intent) {
    this.rawInputs = rawInputs;
    this.arguments = arguments;
    this.intent = intent;
  }

  public List<GoogleHomeOriginalRequestDataInputRaw> getRawInputs() {
    return rawInputs;
  }

  public void setRawInputs(List<GoogleHomeOriginalRequestDataInputRaw> rawInputs) {
    this.rawInputs = rawInputs;
  }

  public List<GoogleHomeRequestArgument> getArguments() {
    return arguments;
  }

  public void setArguments(List<GoogleHomeRequestArgument> arguments) {
    this.arguments = arguments;
  }

  public String getIntent() {
    return intent;
  }

  public void setIntent(String intent) {
    this.intent = intent;
  }

  @Override public String toString() {
    return "GoogleHomeOriginalRequestDataInput{" + "rawInputs=" + rawInputs + ", arguments=" + arguments + ", intent='" + intent + '\''
        + '}';
  }
}
