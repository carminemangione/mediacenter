package com.mangione.mediacenter.model;

import java.io.Serializable;

public class MovieLinks implements Serializable{
    private final String self;
    private final String alternate;

    public MovieLinks(String self, String alternate) {
        this.self = self;
        this.alternate = alternate;
    }

    public String getSelf() {
        return self;
    }

    public String getAlternate() {
        return alternate;
    }
}
