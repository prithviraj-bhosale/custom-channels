package com.morfeus.channels.model.preference.ghome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author Prithviraj Bhosale
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleHomeResponseSystemIntentData implements Serializable
{

  @JsonProperty("@type")
  private String type;
  @JsonProperty("listSelect")
  private GoogleHomeResponseListSelect listSelect = null;
  @JsonProperty("carouselSelect")
  private GoogleHomeResponseCarouselSelect carouselSelect = null;
  @JsonIgnore
  private final static long serialVersionUID = -6769038248959693999L;

  public GoogleHomeResponseSystemIntentData() {
  }

  public GoogleHomeResponseSystemIntentData(String type, GoogleHomeResponseListSelect listSelect, GoogleHomeResponseCarouselSelect carouselSelect) {
    this.type = type;
    this.listSelect = listSelect;
    this.carouselSelect = carouselSelect;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public GoogleHomeResponseListSelect getListSelect() {
    return listSelect;
  }

  public void setListSelect(GoogleHomeResponseListSelect listSelect) {
    this.listSelect = listSelect;
  }

  public GoogleHomeResponseCarouselSelect getCarouselSelect() {
    return carouselSelect;
  }

  public void setCarouselSelect(GoogleHomeResponseCarouselSelect carouselSelect) {
    this.carouselSelect = carouselSelect;
  }

  @Override public String toString() {
    return "GoogleHomeResponseSystemIntentData{" + "type='" + type + '\'' + ", listSelect=" + listSelect + ", carouselSelect=" + carouselSelect + '}';
  }
}
