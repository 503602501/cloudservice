package com.mybatisplus.dao.domain;

import javax.persistence.*;

@Table(name = "access_record")
public class AccessRecord {
    @Id
    private String cookie;

    /**
     * 业务类型
     */
    @Id
    private String type;

    private String acount;

    private Integer visit;

    /**
     * 总限制的访问次数
     */
    private Integer limit;

    /**
     * 延迟访问策略
     */
    private String delay;

    /**
     * @return cookie
     */
    public String getCookie() {
        return cookie;
    }

    /**
     * @param cookie
     */
    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    /**
     * 获取业务类型
     *
     * @return type - 业务类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置业务类型
     *
     * @param type 业务类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return acount
     */
    public String getAcount() {
        return acount;
    }

    /**
     * @param acount
     */
    public void setAcount(String acount) {
        this.acount = acount;
    }

    /**
     * @return visit
     */
    public Integer getVisit() {
        return visit;
    }

    /**
     * @param visit
     */
    public void setVisit(Integer visit) {
        this.visit = visit;
    }

    /**
     * 获取总限制的访问次数
     *
     * @return limit - 总限制的访问次数
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * 设置总限制的访问次数
     *
     * @param limit 总限制的访问次数
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * 获取延迟访问策略
     *
     * @return delay - 延迟访问策略
     */
    public String getDelay() {
        return delay;
    }

    /**
     * 设置延迟访问策略
     *
     * @param delay 延迟访问策略
     */
    public void setDelay(String delay) {
        this.delay = delay;
    }
}