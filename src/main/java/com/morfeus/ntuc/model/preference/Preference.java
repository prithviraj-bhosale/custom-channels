package com.morfeus.ntuc.model.preference;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Preference {
  @JsonProperty("prefered")
  private List<Prefered> preferedList;

  public List<Prefered> getPreferedList() {
    return preferedList;
  }

  public void setPreferedList(List<Prefered> preferedList) {
    this.preferedList = preferedList;
  }

  @Override public String toString() {
    return "Preference{" + "preferedList=" + preferedList + '}';
  }
}
