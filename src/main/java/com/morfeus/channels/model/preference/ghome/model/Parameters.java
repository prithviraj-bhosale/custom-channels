package com.morfeus.channels.model.preference.ghome.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
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
    "banking_product_type",
    "banking_product_type.original",
    "banking_product_account_number",
    "banking_product_account_number.original",
    "banking_product_account_type",
    "banking_product_account_type.original"
})
public class Parameters implements Serializable
{

  @JsonProperty("banking_product_type")
  private List<String> bankingProductType = null;
  @JsonProperty("banking_product_type.original")
  private List<String> bankingProductTypeOriginal = null;
  @JsonProperty("banking_product_account_number")
  private String bankingProductAccountNumber;
  @JsonProperty("banking_product_account_number.original")
  private String bankingProductAccountNumberOriginal;
  @JsonProperty("banking_product_account_type")
  private String bankingProductAccountType;
  @JsonProperty("banking_product_account_type.original")
  private String bankingProductAccountTypeOriginal;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();
  private final static long serialVersionUID = 7761260266745071939L;

  /**
   * No args constructor for use in serialization
   *
   */
  public Parameters() {
  }

  /**
   *
   * @param bankingProductAccountNumber
   * @param bankingProductAccountType
   * @param bankingProductType
   * @param bankingProductAccountTypeOriginal
   * @param bankingProductAccountNumberOriginal
   * @param bankingProductTypeOriginal
   */
  public Parameters(List<String> bankingProductType, List<String> bankingProductTypeOriginal, String bankingProductAccountNumber, String bankingProductAccountNumberOriginal, String bankingProductAccountType, String bankingProductAccountTypeOriginal) {
    super();
    this.bankingProductType = bankingProductType;
    this.bankingProductTypeOriginal = bankingProductTypeOriginal;
    this.bankingProductAccountNumber = bankingProductAccountNumber;
    this.bankingProductAccountNumberOriginal = bankingProductAccountNumberOriginal;
    this.bankingProductAccountType = bankingProductAccountType;
    this.bankingProductAccountTypeOriginal = bankingProductAccountTypeOriginal;
  }

  @JsonProperty("banking_product_type")
  public List<String> getBankingProductType() {
    return bankingProductType;
  }

  @JsonProperty("banking_product_type")
  public void setBankingProductType(List<String> bankingProductType) {
    this.bankingProductType = bankingProductType;
  }

  @JsonProperty("banking_product_type.original")
  public List<String> getBankingProductTypeOriginal() {
    return bankingProductTypeOriginal;
  }

  @JsonProperty("banking_product_type.original")
  public void setBankingProductTypeOriginal(List<String> bankingProductTypeOriginal) {
    this.bankingProductTypeOriginal = bankingProductTypeOriginal;
  }

  @JsonProperty("banking_product_account_number")
  public String getBankingProductAccountNumber() {
    return bankingProductAccountNumber;
  }

  @JsonProperty("banking_product_account_number")
  public void setBankingProductAccountNumber(String bankingProductAccountNumber) {
    this.bankingProductAccountNumber = bankingProductAccountNumber;
  }

  @JsonProperty("banking_product_account_number.original")
  public String getBankingProductAccountNumberOriginal() {
    return bankingProductAccountNumberOriginal;
  }

  @JsonProperty("banking_product_account_number.original")
  public void setBankingProductAccountNumberOriginal(String bankingProductAccountNumberOriginal) {
    this.bankingProductAccountNumberOriginal = bankingProductAccountNumberOriginal;
  }

  @JsonProperty("banking_product_account_type")
  public String getBankingProductAccountType() {
    return bankingProductAccountType;
  }

  @JsonProperty("banking_product_account_type")
  public void setBankingProductAccountType(String bankingProductAccountType) {
    this.bankingProductAccountType = bankingProductAccountType;
  }

  @JsonProperty("banking_product_account_type.original")
  public String getBankingProductAccountTypeOriginal() {
    return bankingProductAccountTypeOriginal;
  }

  @JsonProperty("banking_product_account_type.original")
  public void setBankingProductAccountTypeOriginal(String bankingProductAccountTypeOriginal) {
    this.bankingProductAccountTypeOriginal = bankingProductAccountTypeOriginal;
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
    return new ToStringBuilder(this).append("bankingProductType", bankingProductType).append("bankingProductTypeOriginal", bankingProductTypeOriginal).append("bankingProductAccountNumber", bankingProductAccountNumber).append("bankingProductAccountNumberOriginal", bankingProductAccountNumberOriginal).append("bankingProductAccountType", bankingProductAccountType).append("bankingProductAccountTypeOriginal", bankingProductAccountTypeOriginal).append("additionalProperties", additionalProperties).toString();
  }

}
