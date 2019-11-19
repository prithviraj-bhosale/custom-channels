package com.morfeus.channels.model.preference.ghome.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author Pritesh Soni
 * This class is only used as a component of GoogleHomeResponse class
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleHomeResponsePayload implements Serializable {
  private GoogleHomeResponseDataGoogle google;

  public GoogleHomeResponsePayload() {
  }

  public GoogleHomeResponsePayload(GoogleHomeResponseDataGoogle google) {
    this.google = google;
  }

  public GoogleHomeResponseDataGoogle getGoogle() {
    return google;
  }

  public void setGoogle(GoogleHomeResponseDataGoogle google) {
    this.google = google;
  }

}
