
package com.morfeus.channels.model.preference.ghome.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * @author Prithviraj Bhosale
 * This class is used to get request from google home.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleHomeRequestArgument implements Serializable
{

  @JsonProperty("rawText")
  private String rawText;
  @JsonProperty("textValue")
  private String textValue;
  @JsonProperty("name")
  private String name;
  private final static long serialVersionUID = 5840147824580933919L;

  /**
   * No args constructor for use in serialization
   *
   */
  public GoogleHomeRequestArgument() {
  }

  /**
   *
   * @param rawText
   * @param name
   * @param textValue
   */
  public GoogleHomeRequestArgument(String rawText, String textValue, String name) {
    super();
    this.rawText = rawText;
    this.textValue = textValue;
    this.name = name;
  }

  @JsonProperty("rawText")
  public String getRawText() {
    return rawText;
  }

  @JsonProperty("rawText")
  public void setRawText(String rawText) {
    this.rawText = rawText;
  }

  @JsonProperty("textValue")
  public String getTextValue() {
    return textValue;
  }

  @JsonProperty("textValue")
  public void setTextValue(String textValue) {
    this.textValue = textValue;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("rawText", rawText).append("textValue", textValue).append("name", name).toString();
  }

}
