package com.morfeus.channels.model.preference.ghome.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * @author Prithviraj Bhosale
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleHomeResponseImage implements Serializable
{

  @JsonProperty("url")
  private String url;
  @JsonProperty("accessibilityText")
  private String accessibilityText;
  private final static long serialVersionUID = -8315713563032393564L;

  public GoogleHomeResponseImage() {
  }

  public GoogleHomeResponseImage(String url, String accessibilityText) {
    super();
    this.url = url;
    this.accessibilityText = accessibilityText;
  }

  @JsonProperty("url")
  public String getUrl() {
    return url;
  }

  @JsonProperty("url")
  public void setUrl(String url) {
    this.url = url;
  }

  @JsonProperty("accessibilityText")
  public String getAccessibilityText() {
    return accessibilityText;
  }

  @JsonProperty("accessibilityText")
  public void setAccessibilityText(String accessibilityText) {
    this.accessibilityText = accessibilityText;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("url", url).append("accessibilityText", accessibilityText).toString();
  }

}
