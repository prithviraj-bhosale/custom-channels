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
public class GoogleHomeOriginalRequestDataSurfaceCapability implements Serializable {

  @JsonProperty("name") private String name;

  public GoogleHomeOriginalRequestDataSurfaceCapability() {
  }

  public GoogleHomeOriginalRequestDataSurfaceCapability(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override public String toString() {
    return "GoogleHomeOriginalRequestDataSurfaceCapability{" + "name='" + name + '\'' + '}';
  }
}
