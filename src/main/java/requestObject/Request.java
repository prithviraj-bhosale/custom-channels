package requestObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "to",
    "from",
    "message"
})
public class Request implements Serializable
{

  @JsonProperty("to")
  private To to;
  @JsonProperty("from")
  private From from;
  @JsonProperty("message")
  private Message message;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();
  private final static long serialVersionUID = -2788875158269772539L;

  @JsonProperty("to")
  public To getTo() {
    return to;
  }

  @JsonProperty("to")
  public void setTo(To to) {
    this.to = to;
  }

  @JsonProperty("from")
  public From getFrom() {
    return from;
  }

  @JsonProperty("from")
  public void setFrom(From from) {
    this.from = from;
  }

  @JsonProperty("message")
  public Message getMessage() {
    return message;
  }

  @JsonProperty("message")
  public void setMessage(Message message) {
    this.message = message;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("to", to).append("from", from).append("message", message).append("additionalProperties", additionalProperties).toString();
  }

}
