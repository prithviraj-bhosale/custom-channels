
package com.morfeus.channels.model.preference.ghome.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author Prithviraj Bhosale
 * This class is used to get request from google home.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleHomeRequestPayload implements Serializable
{

  @JsonProperty("isInSandbox")
  private boolean isInSandbox;
  @JsonProperty("surface")
  private GoogleHomeOriginalRequestDataSurface surface;
  @JsonProperty("inputs")
  private List<GoogleHomeOriginalRequestDataInput> inputs = null;
  @JsonProperty("user")
  private GoogleHomeRequestUser user;
  @JsonProperty("conversation")
  private GoogleHomeRequestConversation conversation;
  @JsonProperty("availableSurfaces")
  private List<GoogleHomeOriginalRequestDataSurface> availableSurfaces = null;
  private final static long serialVersionUID = 4233424551759985158L;

  public GoogleHomeRequestPayload() {
  }

  public GoogleHomeRequestPayload(boolean isInSandbox, GoogleHomeOriginalRequestDataSurface surface, List<GoogleHomeOriginalRequestDataInput> inputs,
      GoogleHomeRequestUser user, GoogleHomeRequestConversation conversation, List<GoogleHomeOriginalRequestDataSurface> availableSurfaces) {
    this.isInSandbox = isInSandbox;
    this.surface = surface;
    this.inputs = inputs;
    this.user = user;
    this.conversation = conversation;
    this.availableSurfaces = availableSurfaces;
  }

  public boolean isInSandbox() {
    return isInSandbox;
  }

  public void setInSandbox(boolean inSandbox) {
    isInSandbox = inSandbox;
  }

  public GoogleHomeOriginalRequestDataSurface getSurface() {
    return surface;
  }

  public void setSurface(GoogleHomeOriginalRequestDataSurface surface) {
    this.surface = surface;
  }

  public List<GoogleHomeOriginalRequestDataInput> getInputs() {
    return inputs;
  }

  public void setInputs(List<GoogleHomeOriginalRequestDataInput> inputs) {
    this.inputs = inputs;
  }

  public GoogleHomeRequestUser getUser() {
    return user;
  }

  public void setUser(GoogleHomeRequestUser user) {
    this.user = user;
  }

  public GoogleHomeRequestConversation getConversation() {
    return conversation;
  }

  public void setConversation(GoogleHomeRequestConversation conversation) {
    this.conversation = conversation;
  }

  public List<GoogleHomeOriginalRequestDataSurface> getAvailableSurfaces() {
    return availableSurfaces;
  }

  public void setAvailableSurfaces(List<GoogleHomeOriginalRequestDataSurface> availableSurfaces) {
    this.availableSurfaces = availableSurfaces;
  }

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  @Override
  public String toString() {
    return "GoogleHomeRequestPayload{" + "isInSandbox=" + isInSandbox + ", surface=" + surface + ", inputs=" + inputs + ", user=" + user + ", conversation="
        + conversation + ", availableSurfaces=" + availableSurfaces + '}';
  }
}
