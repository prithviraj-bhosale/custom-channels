package com.morfeus.channels.model.preference.ghome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author Prithviraj Bhosale
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleHomeResponseButton implements Serializable {

  @JsonProperty("title")
  private String title;
  @JsonProperty("openUrlAction")
  private GoogleHomeResponseOpenUrlAction googleHomeResponseOpenUrlAction;
  @JsonIgnore
  private final static long serialVersionUID = -3727234508162650081L;

  public GoogleHomeResponseButton() {
  }

  public GoogleHomeResponseButton(String title, GoogleHomeResponseOpenUrlAction googleHomeResponseOpenUrlAction) {
    this.title = title;
    this.googleHomeResponseOpenUrlAction = googleHomeResponseOpenUrlAction;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public GoogleHomeResponseOpenUrlAction getGoogleHomeResponseOpenUrlAction() {
    return googleHomeResponseOpenUrlAction;
  }

  public void setGoogleHomeResponseOpenUrlAction(GoogleHomeResponseOpenUrlAction googleHomeResponseOpenUrlAction) {
    this.googleHomeResponseOpenUrlAction = googleHomeResponseOpenUrlAction;
  }

  @Override
  public String toString() {
    return "GoogleHomeResponseButton{" + "title='" + title + '\'' + ", googleHomeResponseOpenUrlAction=" + googleHomeResponseOpenUrlAction
        + '}';
  }
}
