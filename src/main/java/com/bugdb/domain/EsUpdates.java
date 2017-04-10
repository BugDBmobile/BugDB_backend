package com.bugdb.domain;

import java.io.Serializable;
import java.sql.Timestamp;


import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * Created by jingwei on 2017/4/4.
 */
@Document(indexName = "updates", type = "updates", shards = 1, replicas = 0, refreshInterval = "-1")
public class EsUpdates implements Serializable {
    @Id
    private Integer id;
    @Field(format= DateFormat.date_hour_minute_second, pattern = "dd-MM-yyyy hh:mm:ss")
    private Timestamp time;
    private Integer userId;
    private Integer bugId;
    private String comments;
    private String chg;

    public EsUpdates() {
    }
    public EsUpdates(Updates updates){
        BeanUtils.copyProperties(updates, this);
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Timestamp getTime() {
        return time;
    }
    public void setTime(Timestamp time) {
        this.time = time;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getBugId() {
        return bugId;
    }
    public void setBugId(Integer bugId) {
        this.bugId = bugId;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public String getChg() {
        return chg;
    }
    public void setChg(String chg) {
        this.chg = chg;
    }
}
