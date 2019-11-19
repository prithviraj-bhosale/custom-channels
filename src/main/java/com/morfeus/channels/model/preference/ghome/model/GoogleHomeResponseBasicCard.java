package com.morfeus.channels.model.preference.ghome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * @author Prithviraj Bhosale
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleHomeResponseBasicCard implements Serializable
{

  @JsonProperty("title")
  private String title;
  @JsonProperty("subtitle")
  private String subtitle;
  @JsonProperty("formattedText")
  private String formattedText;
  @JsonProperty("image")
  private GoogleHomeResponseImage image;
  @JsonProperty("buttons")
  private List<GoogleHomeResponseButton> buttons = null;
  @JsonProperty("imageDisplayOptions")
  private String imageDisplayOptions;
  @JsonIgnore
  private final static long serialVersionUID = -8654805487912497878L;


  public GoogleHomeResponseBasicCard() {
  }


  public GoogleHomeResponseBasicCard(String title, String subtitle, String formattedText, GoogleHomeResponseImage image,
      List<GoogleHomeResponseButton> buttons, String imageDisplayOptions) {
    this.title = title;
    this.subtitle = subtitle;
    this.formattedText = formattedText;
    this.image = image;
    this.buttons = buttons;
    this.imageDisplayOptions = imageDisplayOptions;
  }

  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  @JsonProperty("title")
  public void setTitle(String title) {
    this.title = title;
  }

  @JsonProperty("subtitle")
  public String getSubtitle() {
    return subtitle;
  }

  @JsonProperty("subtitle")
  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }

  @JsonProperty("formattedText")
  public String getFormattedText() {
    return formattedText;
  }

  @JsonProperty("formattedText")
  public void setFormattedText(String formattedText) {
    this.formattedText = formattedText;
  }

  @JsonProperty("imageDisplayOptions")
  public String getImageDisplayOptions() {
    return imageDisplayOptions;
  }

  @JsonProperty("imageDisplayOptions")
  public void setImageDisplayOptions(String imageDisplayOptions) {
    this.imageDisplayOptions = imageDisplayOptions;
  }

  public GoogleHomeResponseImage getImage() {
    return image;
  }

  public void setImage(GoogleHomeResponseImage image) {
    this.image = image;
  }

  public List<GoogleHomeResponseButton> getButtons() {
    return buttons;
  }

  public void setButtons(List<GoogleHomeResponseButton> buttons) {
    this.buttons = buttons;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("title", title).append("subtitle", subtitle).append("formattedText", formattedText).append("image", image).append("buttons", buttons).append("imageDisplayOptions", imageDisplayOptions).toString();
  }

}
