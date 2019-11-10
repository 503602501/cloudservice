package com.mybatisplus.dao.domain;

import javax.persistence.*;

@Table(name = "config_access")
public class ConfigAccess {
    private String type;

    private Integer limit;

    /**
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return limit
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * @param limit
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}