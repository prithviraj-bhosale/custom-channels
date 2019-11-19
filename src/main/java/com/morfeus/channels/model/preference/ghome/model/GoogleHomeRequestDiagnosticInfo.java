
package com.morfeus.channels.model.preference.ghome.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleHomeRequestDiagnosticInfo implements Serializable
{

  private final static long serialVersionUID = 5701653291543187576L;

  @Override
  public String toString() {
    return new ToStringBuilder(this).toString();
  }

}
