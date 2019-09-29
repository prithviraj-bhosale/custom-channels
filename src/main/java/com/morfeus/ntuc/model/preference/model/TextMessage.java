package com.morfeus.ntuc.model.preference.model;

import ai.active.fulfillment.webhook.data.response.AbstractMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TextMessage extends AbstractMessage implements Serializable {
  private static final long serialVersionUID = -319802185150685079L;
  protected static final String TYPE = "text";
  @JsonProperty("content")
  private String content;

  @Override public String getType() {
    return type;
  }

  @Override public void setType(String type) {
    this.type = type;
  }

  @JsonProperty("type")
  private String type;

  public TextMessage() {
  }

  public TextMessage(String content, String type) {
    this.content = content;
    this.type = type;
  }

  @JsonProperty("content")
  public String getContent() {
    return this.content;
  }

  @JsonProperty("content")
  public void setContent(String content) {
    this.content = content;
  }
}