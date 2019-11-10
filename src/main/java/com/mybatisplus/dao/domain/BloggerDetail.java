package com.mybatisplus.dao.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "blogger_detail")
public class BloggerDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Integer id;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "blogger_id")
    private String bloggerId;

    /**
     * 博主名称
     */
    private String blogger;

    /**
     * 抖音账号
     */
    @Column(name = "dy_account")
    private String dyAccount;

    /**
     * 地区
     */
    private String location;

    private String sex;

    private Integer age;

    /**
     * 7天最新作品数
     */
    @Column(name = "new_work7")
    private Integer newWork7;

    /**
     * 7天粉丝增量
     */
    @Column(name = "fan_add7")
    private Integer fanAdd7;

    /**
     * 7的点赞增量
     */
    @Column(name = "suppport_add7")
    private Integer suppportAdd7;

    /**
     * 7天的评论增量
     */
    @Column(name = "commnet_add7")
    private Integer commnetAdd7;

    /**
     * 7天的分享增量
     */
    @Column(name = "share_add7")
    private Integer shareAdd7;

    @Column(name = "fan_add30")
    private Integer fanAdd30;

    @Column(name = "new_work30")
    private Integer newWork30;

    @Column(name = "suppport_add30")
    private Integer suppportAdd30;

    @Column(name = "commnet_add30")
    private Integer commnetAdd30;

    @Column(name = "share_add30")
    private Integer shareAdd30;

    /**
     * 分类
     */
    private String classify;

    /**
     * 简介
     */
    private String profile;

    /**
     * 排行榜
     */
    private String rank;

    /**
     * 粉丝总数
     */
    private Integer fans;

    /**
     * 作品总数
     */
    private Integer works;

    /**
     * 总点赞
     */
    private Integer supports;

    /**
     * 平均点赞
     */
    @Column(name = "avg_supports")
    private Integer avgSupports;

    /**
     * 集均评论
     */
    @Column(name = "avg_comments")
    private Integer avgComments;

    /**
     * 集均分享
     */
    @Column(name = "avg_shares")
    private Integer avgShares;

    /**
     * 男比例
     */
    @Column(name = "man_ratio")
    private String manRatio;

    /**
     * 女比例
     */
    @Column(name = "woman_ratio")
    private String womanRatio;

    private String location1;

    @Column(name = "location1_ratio")
    private String location1Ratio;

    private String location2;

    @Column(name = "location2_ratio")
    private String location2Ratio;

    private String location3;

    @Column(name = "location3_ratio")
    private String location3Ratio;

    private String location4;

    @Column(name = "location4_ratio")
    private String location4Ratio;

    private String location5;

    @Column(name = "location5_ratio")
    private String location5Ratio;

    private String city1;

    @Column(name = "city1_ratio")
    private String city1Ratio;

    private String city2;

    @Column(name = "city2_ratio")
    private String city2Ratio;

    private String city3;

    @Column(name = "city3_ratio")
    private String city3Ratio;

    private String city4;

    @Column(name = "city4_ratio")
    private String city4Ratio;

    private String city5;

    @Column(name = "city5_ratio")
    private String city5Ratio;

    private String zodiac1;

    @Column(name = "zodiac1_ratio")
    private String zodiac1Ratio;

    private String zodiac2;

    @Column(name = "zodiac2_ratio")
    private String zodiac2Ratio;

    private String zodiac3;

    @Column(name = "zodiac3_ratio")
    private String zodiac3Ratio;

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
     * 获取博主名称
     *
     * @return blogger - 博主名称
     */
    public String getBlogger() {
        return blogger;
    }

    /**
     * 设置博主名称
     *
     * @param blogger 博主名称
     */
    public void setBlogger(String blogger) {
        this.blogger = blogger;
    }

    /**
     * 获取抖音账号
     *
     * @return dy_account - 抖音账号
     */
    public String getDyAccount() {
        return dyAccount;
    }

    /**
     * 设置抖音账号
     *
     * @param dyAccount 抖音账号
     */
    public void setDyAccount(String dyAccount) {
        this.dyAccount = dyAccount;
    }

    /**
     * 获取地区
     *
     * @return location - 地区
     */
    public String getLocation() {
        return location;
    }

    /**
     * 设置地区
     *
     * @param location 地区
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * @param sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * @return age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * @param age
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * 获取7天最新作品数
     *
     * @return new_work7 - 7天最新作品数
     */
    public Integer getNewWork7() {
        return newWork7;
    }

    /**
     * 设置7天最新作品数
     *
     * @param newWork7 7天最新作品数
     */
    public void setNewWork7(Integer newWork7) {
        this.newWork7 = newWork7;
    }

    /**
     * 获取7天粉丝增量
     *
     * @return fan_add7 - 7天粉丝增量
     */
    public Integer getFanAdd7() {
        return fanAdd7;
    }

    /**
     * 设置7天粉丝增量
     *
     * @param fanAdd7 7天粉丝增量
     */
    public void setFanAdd7(Integer fanAdd7) {
        this.fanAdd7 = fanAdd7;
    }

    /**
     * 获取7的点赞增量
     *
     * @return suppport_add7 - 7的点赞增量
     */
    public Integer getSuppportAdd7() {
        return suppportAdd7;
    }

    /**
     * 设置7的点赞增量
     *
     * @param suppportAdd7 7的点赞增量
     */
    public void setSuppportAdd7(Integer suppportAdd7) {
        this.suppportAdd7 = suppportAdd7;
    }

    /**
     * 获取7天的评论增量
     *
     * @return commnet_add7 - 7天的评论增量
     */
    public Integer getCommnetAdd7() {
        return commnetAdd7;
    }

    /**
     * 设置7天的评论增量
     *
     * @param commnetAdd7 7天的评论增量
     */
    public void setCommnetAdd7(Integer commnetAdd7) {
        this.commnetAdd7 = commnetAdd7;
    }

    /**
     * 获取7天的分享增量
     *
     * @return share_add7 - 7天的分享增量
     */
    public Integer getShareAdd7() {
        return shareAdd7;
    }

    /**
     * 设置7天的分享增量
     *
     * @param shareAdd7 7天的分享增量
     */
    public void setShareAdd7(Integer shareAdd7) {
        this.shareAdd7 = shareAdd7;
    }

    /**
     * @return fan_add30
     */
    public Integer getFanAdd30() {
        return fanAdd30;
    }

    /**
     * @param fanAdd30
     */
    public void setFanAdd30(Integer fanAdd30) {
        this.fanAdd30 = fanAdd30;
    }

    /**
     * @return new_work30
     */
    public Integer getNewWork30() {
        return newWork30;
    }

    /**
     * @param newWork30
     */
    public void setNewWork30(Integer newWork30) {
        this.newWork30 = newWork30;
    }

    /**
     * @return suppport_add30
     */
    public Integer getSuppportAdd30() {
        return suppportAdd30;
    }

    /**
     * @param suppportAdd30
     */
    public void setSuppportAdd30(Integer suppportAdd30) {
        this.suppportAdd30 = suppportAdd30;
    }

    /**
     * @return commnet_add30
     */
    public Integer getCommnetAdd30() {
        return commnetAdd30;
    }

    /**
     * @param commnetAdd30
     */
    public void setCommnetAdd30(Integer commnetAdd30) {
        this.commnetAdd30 = commnetAdd30;
    }

    /**
     * @return share_add30
     */
    public Integer getShareAdd30() {
        return shareAdd30;
    }

    /**
     * @param shareAdd30
     */
    public void setShareAdd30(Integer shareAdd30) {
        this.shareAdd30 = shareAdd30;
    }

    /**
     * 获取分类
     *
     * @return classify - 分类
     */
    public String getClassify() {
        return classify;
    }

    /**
     * 设置分类
     *
     * @param classify 分类
     */
    public void setClassify(String classify) {
        this.classify = classify;
    }

    /**
     * 获取简介
     *
     * @return profile - 简介
     */
    public String getProfile() {
        return profile;
    }

    /**
     * 设置简介
     *
     * @param profile 简介
     */
    public void setProfile(String profile) {
        this.profile = profile;
    }

    /**
     * 获取排行榜
     *
     * @return rank - 排行榜
     */
    public String getRank() {
        return rank;
    }

    /**
     * 设置排行榜
     *
     * @param rank 排行榜
     */
    public void setRank(String rank) {
        this.rank = rank;
    }

    /**
     * 获取粉丝总数
     *
     * @return fans - 粉丝总数
     */
    public Integer getFans() {
        return fans;
    }

    /**
     * 设置粉丝总数
     *
     * @param fans 粉丝总数
     */
    public void setFans(Integer fans) {
        this.fans = fans;
    }

    /**
     * 获取作品总数
     *
     * @return works - 作品总数
     */
    public Integer getWorks() {
        return works;
    }

    /**
     * 设置作品总数
     *
     * @param works 作品总数
     */
    public void setWorks(Integer works) {
        this.works = works;
    }

    /**
     * 获取总点赞
     *
     * @return supports - 总点赞
     */
    public Integer getSupports() {
        return supports;
    }

    /**
     * 设置总点赞
     *
     * @param supports 总点赞
     */
    public void setSupports(Integer supports) {
        this.supports = supports;
    }

    /**
     * 获取平均点赞
     *
     * @return avg_supports - 平均点赞
     */
    public Integer getAvgSupports() {
        return avgSupports;
    }

    /**
     * 设置平均点赞
     *
     * @param avgSupports 平均点赞
     */
    public void setAvgSupports(Integer avgSupports) {
        this.avgSupports = avgSupports;
    }

    /**
     * 获取集均评论
     *
     * @return avg_comments - 集均评论
     */
    public Integer getAvgComments() {
        return avgComments;
    }

    /**
     * 设置集均评论
     *
     * @param avgComments 集均评论
     */
    public void setAvgComments(Integer avgComments) {
        this.avgComments = avgComments;
    }

    /**
     * 获取集均分享
     *
     * @return avg_shares - 集均分享
     */
    public Integer getAvgShares() {
        return avgShares;
    }

    /**
     * 设置集均分享
     *
     * @param avgShares 集均分享
     */
    public void setAvgShares(Integer avgShares) {
        this.avgShares = avgShares;
    }

    /**
     * 获取男比例
     *
     * @return man_ratio - 男比例
     */
    public String getManRatio() {
        return manRatio;
    }

    /**
     * 设置男比例
     *
     * @param manRatio 男比例
     */
    public void setManRatio(String manRatio) {
        this.manRatio = manRatio;
    }

    /**
     * 获取女比例
     *
     * @return woman_ratio - 女比例
     */
    public String getWomanRatio() {
        return womanRatio;
    }

    /**
     * 设置女比例
     *
     * @param womanRatio 女比例
     */
    public void setWomanRatio(String womanRatio) {
        this.womanRatio = womanRatio;
    }

    /**
     * @return location1
     */
    public String getLocation1() {
        return location1;
    }

    /**
     * @param location1
     */
    public void setLocation1(String location1) {
        this.location1 = location1;
    }

    /**
     * @return location1_ratio
     */
    public String getLocation1Ratio() {
        return location1Ratio;
    }

    /**
     * @param location1Ratio
     */
    public void setLocation1Ratio(String location1Ratio) {
        this.location1Ratio = location1Ratio;
    }

    /**
     * @return location2
     */
    public String getLocation2() {
        return location2;
    }

    /**
     * @param location2
     */
    public void setLocation2(String location2) {
        this.location2 = location2;
    }

    /**
     * @return location2_ratio
     */
    public String getLocation2Ratio() {
        return location2Ratio;
    }

    /**
     * @param location2Ratio
     */
    public void setLocation2Ratio(String location2Ratio) {
        this.location2Ratio = location2Ratio;
    }

    /**
     * @return location3
     */
    public String getLocation3() {
        return location3;
    }

    /**
     * @param location3
     */
    public void setLocation3(String location3) {
        this.location3 = location3;
    }

    /**
     * @return location3_ratio
     */
    public String getLocation3Ratio() {
        return location3Ratio;
    }

    /**
     * @param location3Ratio
     */
    public void setLocation3Ratio(String location3Ratio) {
        this.location3Ratio = location3Ratio;
    }

    /**
     * @return location4
     */
    public String getLocation4() {
        return location4;
    }

    /**
     * @param location4
     */
    public void setLocation4(String location4) {
        this.location4 = location4;
    }

    /**
     * @return location4_ratio
     */
    public String getLocation4Ratio() {
        return location4Ratio;
    }

    /**
     * @param location4Ratio
     */
    public void setLocation4Ratio(String location4Ratio) {
        this.location4Ratio = location4Ratio;
    }

    /**
     * @return location5
     */
    public String getLocation5() {
        return location5;
    }

    /**
     * @param location5
     */
    public void setLocation5(String location5) {
        this.location5 = location5;
    }

    /**
     * @return location5_ratio
     */
    public String getLocation5Ratio() {
        return location5Ratio;
    }

    /**
     * @param location5Ratio
     */
    public void setLocation5Ratio(String location5Ratio) {
        this.location5Ratio = location5Ratio;
    }

    /**
     * @return city1
     */
    public String getCity1() {
        return city1;
    }

    /**
     * @param city1
     */
    public void setCity1(String city1) {
        this.city1 = city1;
    }

    /**
     * @return city1_ratio
     */
    public String getCity1Ratio() {
        return city1Ratio;
    }

    /**
     * @param city1Ratio
     */
    public void setCity1Ratio(String city1Ratio) {
        this.city1Ratio = city1Ratio;
    }

    /**
     * @return city2
     */
    public String getCity2() {
        return city2;
    }

    /**
     * @param city2
     */
    public void setCity2(String city2) {
        this.city2 = city2;
    }

    /**
     * @return city2_ratio
     */
    public String getCity2Ratio() {
        return city2Ratio;
    }

    /**
     * @param city2Ratio
     */
    public void setCity2Ratio(String city2Ratio) {
        this.city2Ratio = city2Ratio;
    }

    /**
     * @return city3
     */
    public String getCity3() {
        return city3;
    }

    /**
     * @param city3
     */
    public void setCity3(String city3) {
        this.city3 = city3;
    }

    /**
     * @return city3_ratio
     */
    public String getCity3Ratio() {
        return city3Ratio;
    }

    /**
     * @param city3Ratio
     */
    public void setCity3Ratio(String city3Ratio) {
        this.city3Ratio = city3Ratio;
    }

    /**
     * @return city4
     */
    public String getCity4() {
        return city4;
    }

    /**
     * @param city4
     */
    public void setCity4(String city4) {
        this.city4 = city4;
    }

    /**
     * @return city4_ratio
     */
    public String getCity4Ratio() {
        return city4Ratio;
    }

    /**
     * @param city4Ratio
     */
    public void setCity4Ratio(String city4Ratio) {
        this.city4Ratio = city4Ratio;
    }

    /**
     * @return city5
     */
    public String getCity5() {
        return city5;
    }

    /**
     * @param city5
     */
    public void setCity5(String city5) {
        this.city5 = city5;
    }

    /**
     * @return city5_ratio
     */
    public String getCity5Ratio() {
        return city5Ratio;
    }

    /**
     * @param city5Ratio
     */
    public void setCity5Ratio(String city5Ratio) {
        this.city5Ratio = city5Ratio;
    }

    /**
     * @return zodiac1
     */
    public String getZodiac1() {
        return zodiac1;
    }

    /**
     * @param zodiac1
     */
    public void setZodiac1(String zodiac1) {
        this.zodiac1 = zodiac1;
    }

    /**
     * @return zodiac1_ratio
     */
    public String getZodiac1Ratio() {
        return zodiac1Ratio;
    }

    /**
     * @param zodiac1Ratio
     */
    public void setZodiac1Ratio(String zodiac1Ratio) {
        this.zodiac1Ratio = zodiac1Ratio;
    }

    /**
     * @return zodiac2
     */
    public String getZodiac2() {
        return zodiac2;
    }

    /**
     * @param zodiac2
     */
    public void setZodiac2(String zodiac2) {
        this.zodiac2 = zodiac2;
    }

    /**
     * @return zodiac2_ratio
     */
    public String getZodiac2Ratio() {
        return zodiac2Ratio;
    }

    /**
     * @param zodiac2Ratio
     */
    public void setZodiac2Ratio(String zodiac2Ratio) {
        this.zodiac2Ratio = zodiac2Ratio;
    }

    /**
     * @return zodiac3
     */
    public String getZodiac3() {
        return zodiac3;
    }

    /**
     * @param zodiac3
     */
    public void setZodiac3(String zodiac3) {
        this.zodiac3 = zodiac3;
    }

    /**
     * @return zodiac3_ratio
     */
    public String getZodiac3Ratio() {
        return zodiac3Ratio;
    }

    /**
     * @param zodiac3Ratio
     */
    public void setZodiac3Ratio(String zodiac3Ratio) {
        this.zodiac3Ratio = zodiac3Ratio;
    }
}