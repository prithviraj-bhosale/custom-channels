package com.morfeus.channels.model.preference.ghome.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author Prithviraj Bhosale
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleHomeResponseListSelectItemOptionInfo implements Serializable
{

  @JsonProperty("key")
  private String key;

  public GoogleHomeResponseListSelectItemOptionInfo() {
  }

  public GoogleHomeResponseListSelectItemOptionInfo(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  @Override public String toString() {
    return "GoogleHomeResponseListSelectItemOptionInfo{" + "key='" + key + '\'' + '}';
  }
}
