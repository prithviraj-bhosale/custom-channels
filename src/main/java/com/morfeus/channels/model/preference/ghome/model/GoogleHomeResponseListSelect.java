package com.morfeus.channels.model.preference.ghome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author Prithviraj Bhosale
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleHomeResponseListSelect implements Serializable {

  @JsonProperty("title")
  private String title;
  @JsonProperty("items")
  private List<GoogleHomeResponseListSelectItem> items = null;
  @JsonIgnore
  private final static long serialVersionUID = -7205079643043410917L;

  public GoogleHomeResponseListSelect() {
  }

  public GoogleHomeResponseListSelect(String title, List<GoogleHomeResponseListSelectItem> items) {
    this.title = title;
    this.items = items;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<GoogleHomeResponseListSelectItem> getItems() {
    return items;
  }

  public void setItems(List<GoogleHomeResponseListSelectItem> items) {
    this.items = items;
  }

  public void addItems(List<GoogleHomeResponseListSelectItem> items) {
    this.items.addAll(items);
  }

  @Override public String toString() {
    return "GoogleHomeResponseListSelect{" + "title='" + title + '\'' + ", items=" + items + '}';
  }
}
