
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
public class GoogleHomeRequestOriginalDetectIntentRequest implements Serializable
{

  @JsonProperty("source")
  private String source;
  @JsonProperty("version")
  private String version;
  @JsonProperty("payload")
  private GoogleHomeRequestPayload payload;
  private final static long serialVersionUID = -3053062244915664161L;

  /**
   * No args constructor for use in serialization
   *
   */
  public GoogleHomeRequestOriginalDetectIntentRequest() {
  }

  /**
   *
   * @param source
   * @param payload
   * @param version
   */
  public GoogleHomeRequestOriginalDetectIntentRequest(String source, String version, GoogleHomeRequestPayload payload) {
    super();
    this.source = source;
    this.version = version;
    this.payload = payload;
  }

  @JsonProperty("source")
  public String getSource() {
    return source;
  }

  @JsonProperty("source")
  public void setSource(String source) {
    this.source = source;
  }

  @JsonProperty("version")
  public String getVersion() {
    return version;
  }

  @JsonProperty("version")
  public void setVersion(String version) {
    this.version = version;
  }

  @JsonProperty("payload")
  public GoogleHomeRequestPayload getPayload() {
    return payload;
  }

  @JsonProperty("payload")
  public void setPayload(GoogleHomeRequestPayload payload) {
    this.payload = payload;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("source", source).append("version", version).append("payload", payload).toString();
  }

}
