package com.mybatisplus.dao.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "hot_video")
public class HotVideo extends Task{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Integer id;

    private String title;
    
    private String account;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "blogger_id")
    private String bloggerId;

    private String cycle;

    private String time;

    @Column(name = "hot_word")
    private String hotWord;

    private String blogger;

    /**
     * 点赞
     */
    private String supports;

    /**
     * 评论数
     */
    private String comments;
    
    private Integer status;

    /**
     * 博主链接
     */
    @Column(name = "blogger_url")
    private String bloggerUrl;
    
    /**
     * 任务id
     */
    @Column(name = "task_id")
    private Integer taskId;

    @Column(name = "create_date")
    private Date createDate;

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

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
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return video_url
     */
    public String getVideoUrl() {
        return videoUrl;
    }
    

    public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
     * @param videoUrl
     */
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    /**
     * @return blogger_id
     */
    public String getBloggerId() {
        return bloggerId;
    }

    /**
     * @param bloggerId
     */
    public void setBloggerId(String bloggerId) {
        this.bloggerId = bloggerId;
    }

    /**
     * @return cycle
     */
    public String getCycle() {
        return cycle;
    }

    /**
     * @param cycle
     */
    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

     

    public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	/**
     * @return hot_word
     */
    public String getHotWord() {
        return hotWord;
    }

    /**
     * @param hotWord
     */
    public void setHotWord(String hotWord) {
        this.hotWord = hotWord;
    }

    /**
     * @return blogger
     */
    public String getBlogger() {
        return blogger;
    }

    /**
     * @param blogger
     */
    public void setBlogger(String blogger) {
        this.blogger = blogger;
    }

      

    public String getSupports() {
		return supports;
	}

	public void setSupports(String supports) {
		this.supports = supports;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
     * 获取博主链接
     *
     * @return blogger_url - 博主链接
     */
    public String getBloggerUrl() {
        return bloggerUrl;
    }

    /**
     * 设置博主链接
     *
     * @param bloggerUrl 博主链接
     */
    public void setBloggerUrl(String bloggerUrl) {
        this.bloggerUrl = bloggerUrl;
    }

    /**
     * @return create_date
     */
    public Date getCreateDate(){
        return createDate;
    }

    /**
     * @param createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}