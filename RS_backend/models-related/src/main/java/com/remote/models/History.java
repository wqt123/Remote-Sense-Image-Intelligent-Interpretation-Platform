package com.remote.models;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "history", indexes = {
        @Index(name = "history_isRemove_IDX", columnList = "isRemove")
})
public class History {
    @Id
    @Column(name = "hisId", nullable = false, length = 20)
    private String id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(name = "type", nullable = false, length = 20)
    private String type;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "originName1", length = 100)
    private String originName1;

    @Column(name = "originName2", length = 100)
    private String originName2;

    @Column(name = "resultName", length = 100)
    private String resultName;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "size", length = 20)
    private String size;

    @Column(name = "createTime")
    private Instant createTime;

    @Column(name = "isRemove", nullable = false)
    private Boolean isRemove = false;

    public Boolean getIsRemove() {
        return isRemove;
    }

    public void setIsRemove(Boolean isRemove) {
        this.isRemove = isRemove;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResultName() {
        return resultName;
    }

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    public String getOriginName2() {
        return originName2;
    }

    public void setOriginName2(String originName2) {
        this.originName2 = originName2;
    }

    public String getOriginName1() {
        return originName1;
    }

    public void setOriginName1(String originName1) {
        this.originName1 = originName1;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}