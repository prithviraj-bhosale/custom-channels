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
public class GoogleHomeResponseGoogleRichResponse implements Serializable
{

  @JsonProperty("items")
  private List<GoogleHomeResponseItem> items = null;
  @JsonIgnore
  private final static long serialVersionUID = -2054240003415270442L;
  @JsonProperty("suggestions")
  private List<GoogleHomeResponseGoogleRichResponseSuggestions>suggestions = null;

  public GoogleHomeResponseGoogleRichResponse() {
  }

  public GoogleHomeResponseGoogleRichResponse(List<GoogleHomeResponseItem> items, List<GoogleHomeResponseGoogleRichResponseSuggestions> suggestions) {
    this.items = items;
    this.suggestions = suggestions;
  }

  @JsonProperty("items")
  public List<GoogleHomeResponseItem> getItems() {
    return items;
  }

  @JsonProperty("items")
  public void setItems(List<GoogleHomeResponseItem> items) {
    this.items = items;
  }

  public void addItem(GoogleHomeResponseItem item) {
    this.items.add(item);
  }

  public void addItems(List<GoogleHomeResponseItem> items) {
    this.items.addAll(items);
  }

  public List<GoogleHomeResponseGoogleRichResponseSuggestions> getSuggestions() {
    return suggestions;
  }

  public void setSuggestions(List<GoogleHomeResponseGoogleRichResponseSuggestions> suggestions) {
    this.suggestions = suggestions;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("items", items).toString();
  }

}
