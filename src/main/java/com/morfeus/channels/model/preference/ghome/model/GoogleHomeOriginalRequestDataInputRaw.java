package com.morfeus.channels.model.preference.ghome.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author Pritesh Soni
 * This class is only used as a component of GoogleHomeOriginalRequestDataInput Class. 
 * It contains the user's input.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleHomeOriginalRequestDataInputRaw implements Serializable {
  @JsonProperty("query")
  private String query;

  @JsonProperty("inputType")
  private String inputType;

  public GoogleHomeOriginalRequestDataInputRaw() {
  }

  public GoogleHomeOriginalRequestDataInputRaw(String query, String inputType) {
    this.query = query;
    this.inputType = inputType;
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public String getInputType() {
    return inputType;
  }

  public void setInputType(String inputType) {
    this.inputType = inputType;
  }

  @Override
  public String toString() {
    return "GoogleHomeOriginalRequestDataInputRaw{" + "query='" + query + '\'' + ", inputType='" + inputType + '\'' + '}';
  }
}
