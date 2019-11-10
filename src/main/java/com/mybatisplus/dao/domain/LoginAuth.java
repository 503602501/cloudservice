package com.mybatisplus.dao.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "login_auth")
public class LoginAuth {
    @Id
    private String account;

    private String password;

    private String cookie;
    
    private Integer delay;
    
    @Column(name="limit_count")
    private Integer limitCount;
    
    /**
     * 已经访问的次数
     */
    @Transient
    private int visits;
    
    /**
     * 最后的访问时间
     */
    @Column(name = "last_datetime")
    private Date lastDatetime;
    
    @Column(name = "origin_cookie")
    private String originCookie;

    private String ua;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

	@Transient
	private Long lastTime;
	
	
	public Integer getVisits() {
		return visits;
	}

	public void setVisits(Integer visits) {
		this.visits = visits;
	}

	public Integer getDelay() {
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public Integer getLimitCount() {
		return limitCount;
	}

	public void setLimitCount(Integer limitCount) {
		this.limitCount = limitCount;
	}

	public Date getLastDatetime() {
		return lastDatetime;
	}

	public void setLastDatetime(Date lastDatetime) {
		this.lastDatetime = lastDatetime;
	}

	public Long getLastTime() {
		return lastTime;
	}

	public void setLastTime(Long lastTime) {
		this.lastTime = lastTime;
	}

	public String getOriginCookie() {
		return originCookie;
	}

	public void setOriginCookie(String originCookie) {
		this.originCookie = originCookie;
	}

	/**
     * 备注，用户存储是否触反采集的一些信息
     */
    private String remark;

    /**
     * 0未初始化，1运行状态，2过期状态，3异常状态
     */
    private Integer status;

    /**
     * 过期时间
     */
    private Date expires;

    private String content;

	/**
     * @return account
     */
    public String getAccount() {
        return account;
    }

    /**
     * @param account
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

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
     * @return ua
     */
    public String getUa() {
        return ua;
    }

    /**
     * @param ua
     */
    public void setUa(String ua) {
        this.ua = ua;
    }

    /**
     * @return create_date
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return update_date
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * 获取备注，用户存储是否触反采集的一些信息
     *
     * @return remark - 备注，用户存储是否触反采集的一些信息
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注，用户存储是否触反采集的一些信息
     *
     * @param remark 备注，用户存储是否触反采集的一些信息
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取0未初始化，1运行状态，2过期状态，3异常状态
     *
     * @return status - 0未初始化，1运行状态，2过期状态，3异常状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置0未初始化，1运行状态，2过期状态，3异常状态
     *
     * @param status 0未初始化，1运行状态，2过期状态，3异常状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取过期时间
     *
     * @return expires - 过期时间
     */
    public Date getExpires() {
        return expires;
    }

    /**
     * 设置过期时间
     *
     * @param expires 过期时间
     */
    public void setExpires(Date expires) {
        this.expires = expires;
    }

    /**
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }
}