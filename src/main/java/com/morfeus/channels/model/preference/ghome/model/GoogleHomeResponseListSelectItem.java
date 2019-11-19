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
public class GoogleHomeResponseListSelectItem implements Serializable
{
  @JsonProperty("optionInfo")
  private GoogleHomeResponseListSelectItemOptionInfo optionInfo;
  @JsonProperty("description")
  private String description;
  @JsonProperty("image")
  private GoogleHomeResponseImage image;
  @JsonProperty("title")
  private String title;
  @JsonIgnore
  private final static long serialVersionUID = -2930039775493735418L;

  public GoogleHomeResponseListSelectItem() {
  }

  public GoogleHomeResponseListSelectItem(GoogleHomeResponseListSelectItemOptionInfo optionInfo, String description,
      GoogleHomeResponseImage image, String title) {
    this.optionInfo = optionInfo;
    this.description = description;
    this.image = image;
    this.title = title;
  }

  public GoogleHomeResponseListSelectItemOptionInfo getOptionInfo() {
    return optionInfo;
  }

  public void setOptionInfo(GoogleHomeResponseListSelectItemOptionInfo optionInfo) {
    this.optionInfo = optionInfo;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public GoogleHomeResponseImage getImage() {
    return image;
  }

  public void setImage(GoogleHomeResponseImage image) {
    this.image = image;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }
}
