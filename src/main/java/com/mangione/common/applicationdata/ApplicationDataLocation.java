package com.mangione.common.applicationdata;

public class ApplicationDataLocation {
    private final String applicationDataLocation;

    public ApplicationDataLocation(String applicationName) {
        applicationDataLocation = System.getProperty("user.home") + "/Library/Application Support/" + applicationName;
    }

    public String getApplicationDataLocation() {
        return applicationDataLocation;
    }
}
