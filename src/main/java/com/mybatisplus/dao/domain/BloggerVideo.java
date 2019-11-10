package com.mybatisplus.dao.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "blogger_video")
public class BloggerVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Integer id;

    private String title;

    @Column(name = "hot_word")
    private String hotWord;

    @Column(name = "publish_date")
    private String publishDate;

    private Integer supports;

    private Integer shares;

    private Integer comments;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "blogger_id")
    private String bloggerId;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "video_id")
    private String videoId;

    /**
     * 0隐藏或者删除了，1有效
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
     * @return publish_date
     */
    public String getPublishDate() {
        return publishDate;
    }

    /**
     * @param publishDate
     */
    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    /**
     * @return supports
     */
    public Integer getSupports() {
        return supports;
    }

    /**
     * @param supports
     */
    public void setSupports(Integer supports) {
        this.supports = supports;
    }

    /**
     * @return shares
     */
    public Integer getShares() {
        return shares;
    }

    /**
     * @param shares
     */
    public void setShares(Integer shares) {
        this.shares = shares;
    }

    /**
     * @return comments
     */
    public Integer getComments() {
        return comments;
    }

    /**
     * @param comments
     */
    public void setComments(Integer comments) {
        this.comments = comments;
    }

    /**
     * @return video_url
     */
    public String getVideoUrl() {
        return videoUrl;
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
     * @return video_id
     */
    public String getVideoId() {
        return videoId;
    }

    /**
     * @param videoId
     */
    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    /**
     * 获取0隐藏或者删除了，1有效
     *
     * @return status - 0隐藏或者删除了，1有效
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置0隐藏或者删除了，1有效
     *
     * @param status 0隐藏或者删除了，1有效
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}