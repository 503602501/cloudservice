package com.mybatisplus.dao.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "word_task")
public class WordTask extends Task{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Integer id;

    private String keyword;

    private String account;

    /**
     * 采集任务时间
     */
    @Column(name = "task_time")
    private Date taskTime;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

    /**
     * 0 未处理，1已经处理
     */
    private Integer status;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @param keyword
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

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
     * 获取采集任务时间
     *
     * @return task_time - 采集任务时间
     */
    public Date getTaskTime() {
        return taskTime;
    }

    /**
     * 设置采集任务时间
     *
     * @param taskTime 采集任务时间
     */
    public void setTaskTime(Date taskTime) {
        this.taskTime = taskTime;
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
     * 获取0 未处理，1已经处理
     *
     * @return status - 0 未处理，1已经处理
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置0 未处理，1已经处理
     *
     * @param status 0 未处理，1已经处理
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}