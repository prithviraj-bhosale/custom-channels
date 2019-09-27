package com.morfeus.ntuc.model.preference.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

// TODO: Auto-generated Javadoc


/**
 * The Class ListTemplate.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class    ListTemplate {
    /**
     * The sub title.
     */
    @JsonProperty("subTitle")
    private String subTitle;


    /**
     * The title.
     */
    @JsonProperty("title")
    private String title;

    /**
     * No args constructor for use in serialization.
     */
    public ListTemplate() {
    }

    /**
     * Instantiates a new list template.
     *
     * @param subTitle the sub title
     */
    public ListTemplate(String subTitle) {
        super();
        this.subTitle = subTitle;
    }


    /**
     * Gets the sub title.
     *
     * @return the sub title
     */
    @JsonProperty("subTitle")
    public String getSubTitle() {
        return subTitle;
    }

    /**
     * Sets the sub title.
     *
     * @param subTitle the new sub title
     */
    @JsonProperty("subTitle")
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title the new title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }





    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}








