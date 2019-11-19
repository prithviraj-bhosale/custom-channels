package com.morfeus.channels.model.preference.ghome.model;

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
public class GoogleHomeOriginalRequestDataSurface implements Serializable {

  @JsonProperty("capabilities")
  private List<GoogleHomeOriginalRequestDataSurfaceCapability> capabilities = null;

  public GoogleHomeOriginalRequestDataSurface() {
  }

  public GoogleHomeOriginalRequestDataSurface(List<GoogleHomeOriginalRequestDataSurfaceCapability> capabilities) {
    this.capabilities = capabilities;
  }

  public List<GoogleHomeOriginalRequestDataSurfaceCapability> getCapabilities() {
    return capabilities;
  }

  public void setCapabilities(List<GoogleHomeOriginalRequestDataSurfaceCapability> capabilities) {
    this.capabilities = capabilities;
  }

  @Override public String toString() {
    return "GoogleHomeOriginalRequestDataSurface{" + "capabilities=" + capabilities + '}';
  }
}
