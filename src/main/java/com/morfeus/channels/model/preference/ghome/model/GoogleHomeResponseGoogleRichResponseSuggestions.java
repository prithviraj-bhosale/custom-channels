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
public class GoogleHomeResponseGoogleRichResponseSuggestions implements Serializable {
  @JsonProperty("title")
  private String Title;

  public GoogleHomeResponseGoogleRichResponseSuggestions() {
  }

  public GoogleHomeResponseGoogleRichResponseSuggestions(String title) {
    Title = title;
  }

  public String getTitle() {
    return Title;
  }

  public void setTitle(String title) {
    Title = title;
  }

  @Override public String toString() {
    return "GoogleHomeResponseGoogleRichResponseSuggestions{" + "Title='" + Title + '\'' + '}';
  }
}
