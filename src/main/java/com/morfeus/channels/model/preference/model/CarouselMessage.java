package com.morfeus.channels.model.preference.model;

import ai.active.fulfillment.webhook.data.response.AbstractMessage;
import ai.active.fulfillment.webhook.data.response.Content;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarouselMessage extends AbstractMessage implements Serializable {
  private static final long serialVersionUID = 8125746124348212739L;
  protected static final String TYPE = "carousel";
  @JsonProperty("content")
  private List<Content> content = null;

  @JsonProperty("type")
  private String type;

  public CarouselMessage(List<Content> content, String type) {
    this.content = content;
    this.type = type;
  }

  @Override public String getType() {
    return type;
  }

  @Override public void setType(String type) {
    this.type = type;
  }

  public CarouselMessage() {
  }

  @JsonProperty("content")
  public List<Content> getContent() {
    return this.content;
  }

  @JsonProperty("content")
  public void setContent(List<Content> content) {
    this.content = content;
  }
}