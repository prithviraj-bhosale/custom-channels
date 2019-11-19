
package com.morfeus.channels.model.preference.ghome.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.io.Serializable;

/**
 * @author Prithviraj Bhosale
 * This class is used to get request from google home.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleHomeRequestUser implements Serializable
{

  @JsonProperty("lastSeen")
  private String lastSeen;
  @JsonProperty("locale")
  private String locale;
  @JsonProperty("userId")
  private String userId;
  @JsonProperty("userStorage")
  private String userStorage;
  @JsonProperty("accessToken")
  private String accessToken;

  private final static long serialVersionUID = -7289421318216034341L;

  public GoogleHomeRequestUser() {
  }

  public GoogleHomeRequestUser(String lastSeen, String locale, String userId, String userStorage, String accessToken) {
    this.lastSeen = lastSeen;
    this.locale = locale;
    this.userId = userId;
    this.userStorage = userStorage;
    this.accessToken = accessToken;
  }

  public String getLastSeen() {
    return lastSeen;
  }

  public void setLastSeen(String lastSeen) {
    this.lastSeen = lastSeen;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserStorage() {
    return userStorage;
  }

  public void setUserStorage(String userStorage) {
    this.userStorage = userStorage;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }
}
