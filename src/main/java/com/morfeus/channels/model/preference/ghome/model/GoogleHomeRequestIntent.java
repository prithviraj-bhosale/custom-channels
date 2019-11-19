
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
public class GoogleHomeRequestIntent implements Serializable
{

  @JsonProperty("name")
  private String name;
  @JsonProperty("displayName")
  private String displayName;
  private final static long serialVersionUID = -5022433374366149333L;

  /**
   * No args constructor for use in serialization
   *
   */
  public GoogleHomeRequestIntent() {
  }

  /**
   *
   * @param name
   * @param displayName
   */
  public GoogleHomeRequestIntent(String name, String displayName) {
    super();
    this.name = name;
    this.displayName = displayName;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty("displayName")
  public String getDisplayName() {
    return displayName;
  }

  @JsonProperty("displayName")
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("name", name).append("displayName", displayName).toString();
  }

}
