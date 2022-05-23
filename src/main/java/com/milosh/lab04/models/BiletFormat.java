package com.milosh.lab04.models;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
public class BiletFormat {
    @NotBlank
    private String language;
    @NotBlank
    private String runningTime;
    public BiletFormat(){}

    public BiletFormat(String language, String runningTime) {
        this.language = language;
        this.runningTime = runningTime;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(String runningTime) {
        this.runningTime = runningTime;
    }
}
