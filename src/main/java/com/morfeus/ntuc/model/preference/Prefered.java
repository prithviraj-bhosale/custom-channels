package com.morfeus.ntuc.model.preference;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Prefered {

  @JsonProperty("field")
  private String field;

  @JsonProperty("payload")
  private String payload;

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }

  @Override public String toString() {
    return "Prefered{" + "field='" + field + '\'' + ", payload='" + payload + '\'' + '}';
  }
}
