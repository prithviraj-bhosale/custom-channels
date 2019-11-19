package com.morfeus.channels.model.preference.ghome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * @author Prithviraj Bhosale
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleHomeResponseSimpleResponse implements Serializable
{

  @JsonProperty("textToSpeech")
  private String textToSpeech;
  @JsonProperty("displayText")
  private String displayText;
  @JsonIgnore
  private final static long serialVersionUID = -5701660811552164734L;

  public GoogleHomeResponseSimpleResponse() {
  }
  public GoogleHomeResponseSimpleResponse(String textToSpeech, String displayText) {
    this.textToSpeech = textToSpeech;
    this.displayText = displayText;
  }

  @JsonProperty("textToSpeech")
  public String getTextToSpeech() {
    return textToSpeech;
  }

  @JsonProperty("textToSpeech")
  public void setTextToSpeech(String textToSpeech) {
    this.textToSpeech = textToSpeech;
  }

  @JsonProperty("displayText")
  public String getDisplayText() {
    return displayText;
  }

  @JsonProperty("displayText")
  public void setDisplayText(String displayText) {
    this.displayText = displayText;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("textToSpeech", textToSpeech).append("displayText", displayText).toString();
  }

}
