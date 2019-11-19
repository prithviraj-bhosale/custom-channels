
package com.morfeus.channels.model.preference.ghome.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * @author Prithviraj Bhosale
 * This class is used to get request from google home.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleHomeRequestQueryResult implements Serializable
{

  @JsonProperty("queryText")
  private String queryText;
  @JsonProperty("action")
  private String action;
  @JsonProperty("parameters")
  private GoogleHomeRequestParameters parameters;
  @JsonProperty("allRequiredParamsPresent")
  private boolean allRequiredParamsPresent;
  @JsonProperty("outputContexts")
  private List<GoogleHomeRequestOutputContext> outputContexts = null;
  @JsonProperty("intent")
  private GoogleHomeRequestIntent intent;
  @JsonProperty("intentDetectionConfidence")
  private int intentDetectionConfidence;
  @JsonProperty("diagnosticInfo")
  private GoogleHomeRequestDiagnosticInfo diagnosticInfo;
  @JsonProperty("languageCode")
  private String languageCode;
  private final static long serialVersionUID = 2873200252137903678L;

  /**
   * No args constructor for use in serialization
   *
   */
  public GoogleHomeRequestQueryResult() {
  }

  /**
   *
   * @param languageCode
   * @param intentDetectionConfidence
   * @param outputContexts
   * @param allRequiredParamsPresent
   * @param action
   * @param parameters
   * @param queryText
   * @param diagnosticInfo
   * @param intent
   */
  public GoogleHomeRequestQueryResult(String queryText, String action, GoogleHomeRequestParameters parameters, boolean allRequiredParamsPresent, List<GoogleHomeRequestOutputContext> outputContexts, GoogleHomeRequestIntent intent, int intentDetectionConfidence, GoogleHomeRequestDiagnosticInfo diagnosticInfo, String languageCode) {
    super();
    this.queryText = queryText;
    this.action = action;
    this.parameters = parameters;
    this.allRequiredParamsPresent = allRequiredParamsPresent;
    this.outputContexts = outputContexts;
    this.intent = intent;
    this.intentDetectionConfidence = intentDetectionConfidence;
    this.diagnosticInfo = diagnosticInfo;
    this.languageCode = languageCode;
  }

  @JsonProperty("queryText")
  public String getQueryText() {
    return queryText;
  }

  @JsonProperty("queryText")
  public void setQueryText(String queryText) {
    this.queryText = queryText;
  }

  @JsonProperty("action")
  public String getAction() {
    return action;
  }

  @JsonProperty("action")
  public void setAction(String action) {
    this.action = action;
  }

  @JsonProperty("parameters")
  public GoogleHomeRequestParameters getParameters() {
    return parameters;
  }

  @JsonProperty("parameters")
  public void setParameters(GoogleHomeRequestParameters parameters) {
    this.parameters = parameters;
  }

  @JsonProperty("allRequiredParamsPresent")
  public boolean isAllRequiredParamsPresent() {
    return allRequiredParamsPresent;
  }

  @JsonProperty("allRequiredParamsPresent")
  public void setAllRequiredParamsPresent(boolean allRequiredParamsPresent) {
    this.allRequiredParamsPresent = allRequiredParamsPresent;
  }

  @JsonProperty("outputContexts")
  public List<GoogleHomeRequestOutputContext> getOutputContexts() {
    return outputContexts;
  }

  @JsonProperty("outputContexts")
  public void setOutputContexts(List<GoogleHomeRequestOutputContext> outputContexts) {
    this.outputContexts = outputContexts;
  }

  @JsonProperty("intent")
  public GoogleHomeRequestIntent getIntent() {
    return intent;
  }

  @JsonProperty("intent")
  public void setIntent(GoogleHomeRequestIntent intent) {
    this.intent = intent;
  }

  @JsonProperty("intentDetectionConfidence")
  public int getIntentDetectionConfidence() {
    return intentDetectionConfidence;
  }

  @JsonProperty("intentDetectionConfidence")
  public void setIntentDetectionConfidence(int intentDetectionConfidence) {
    this.intentDetectionConfidence = intentDetectionConfidence;
  }

  @JsonProperty("diagnosticInfo")
  public GoogleHomeRequestDiagnosticInfo getDiagnosticInfo() {
    return diagnosticInfo;
  }

  @JsonProperty("diagnosticInfo")
  public void setDiagnosticInfo(GoogleHomeRequestDiagnosticInfo diagnosticInfo) {
    this.diagnosticInfo = diagnosticInfo;
  }

  @JsonProperty("languageCode")
  public String getLanguageCode() {
    return languageCode;
  }

  @JsonProperty("languageCode")
  public void setLanguageCode(String languageCode) {
    this.languageCode = languageCode;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("queryText", queryText).append("action", action).append("parameters", parameters).append("allRequiredParamsPresent", allRequiredParamsPresent).append("outputContexts", outputContexts).append("intent", intent).append("intentDetectionConfidence", intentDetectionConfidence).append("diagnosticInfo", diagnosticInfo).append("languageCode", languageCode).toString();
  }

}
