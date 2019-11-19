
package com.morfeus.channels.model.preference.ghome.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * @author Prithviraj Bhosale
 * This class is used to get request from google home.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleHomeRequestConversation implements Serializable
{

  @JsonProperty("conversationId")
  private String conversationId;
  @JsonProperty("type")
  private String type;
  @JsonProperty("conversationToken")
  private String conversationToken;
  private final static long serialVersionUID = -4859218005393884268L;

  /**
   * No args constructor for use in serialization
   *
   */
  public GoogleHomeRequestConversation() {
  }

  /**
   *
   * @param conversationToken
   * @param conversationId
   * @param type
   */
  public GoogleHomeRequestConversation(String conversationId, String type, String conversationToken) {
    super();
    this.conversationId = conversationId;
    this.type = type;
    this.conversationToken = conversationToken;
  }

  @JsonProperty("conversationId")
  public String getConversationId() {
    return conversationId;
  }

  @JsonProperty("conversationId")
  public void setConversationId(String conversationId) {
    this.conversationId = conversationId;
  }

  @JsonProperty("type")
  public String getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(String type) {
    this.type = type;
  }

  @JsonProperty("conversationToken")
  public String getConversationToken() {
    return conversationToken;
  }

  @JsonProperty("conversationToken")
  public void setConversationToken(String conversationToken) {
    this.conversationToken = conversationToken;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("conversationId", conversationId).append("type", type).append("conversationToken", conversationToken).toString();
  }

}
