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
public class GoogleHomeResponseItem implements Serializable {

  @JsonProperty("simpleResponse")
  private GoogleHomeResponseSimpleResponse simpleResponse;
  @JsonProperty("basicCard")
  private GoogleHomeResponseBasicCard basicCard;
  @JsonIgnore
  private final static long serialVersionUID = 6069675533180138671L;

  public GoogleHomeResponseItem() {
  }

  public GoogleHomeResponseItem(GoogleHomeResponseSimpleResponse simpleResponse, GoogleHomeResponseBasicCard basicCard) {
    this.simpleResponse = simpleResponse;
    this.basicCard = basicCard;
  }

  @JsonProperty("simpleResponse")
  public GoogleHomeResponseSimpleResponse getSimpleResponse() {
    return simpleResponse;
  }

  @JsonProperty("simpleResponse")
  public void setSimpleResponse(GoogleHomeResponseSimpleResponse simpleResponse) {
    this.simpleResponse = simpleResponse;
  }

  public GoogleHomeResponseBasicCard getBasicCard() {
    return basicCard;
  }

  public void setBasicCard(GoogleHomeResponseBasicCard basicCard) {
    this.basicCard = basicCard;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("simpleResponse", simpleResponse).toString();
  }

}
