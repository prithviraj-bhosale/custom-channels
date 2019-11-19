
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleHomeRequestOutputContext implements Serializable
{

  @JsonProperty("name")
  private String name;
  private final static long serialVersionUID = 6519966703139740563L;

  /**
   * No args constructor for use in serialization
   *
   */
  public GoogleHomeRequestOutputContext() {
  }

  /**
   *
   * @param name
   */
  public GoogleHomeRequestOutputContext(String name) {
    super();
    this.name = name;
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
    return new ToStringBuilder(this).append("name", name).toString();
  }

}
