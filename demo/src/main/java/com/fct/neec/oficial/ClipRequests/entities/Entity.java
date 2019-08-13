package com.fct.neec.oficial.ClipRequests.entities;

import java.io.Serializable;

public abstract class Entity implements Serializable {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
