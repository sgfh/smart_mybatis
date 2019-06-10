package com.smart.mybatis.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.smart.mybatis.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasePojo implements Serializable {
    /**
     * id
     */
    @GeneratedValue
    @Id(value = "id",columnDefinition = "BIGINT(20) NOT NULL")
    private Long id;

    /**
     * 创建时间
     */
    @Column(value = "create_date",columnDefinition = "DATETIME")
    private Date createDate;

    /**
     * 操作时间
     */
    @Column(value = "modify_date",columnDefinition = "DATETIME")
    private Date modifyDate;


    /**
     * 筛选日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date chooseDate;

    /**
     * 筛选时间--开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;

    /**
     * 筛选时间--结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;


    /**
     * 筛选时间--开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 筛选时间--结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }



    /**
     * 是否可见
     */
    @JsonIgnore
    @Column(value = "is_show",columnDefinition = "TINYINT(1) DEFAULT 1",isNull = true)
    private Boolean isShow;


    // 用来升降序
    @SortValue
    private String sortValue;

    // 需要排序的字段
    @SortParam
    private String sortParam;

    /**
     * 注入sql
     */
    @Sql
    @JsonIgnore
    private String sql;

    /**
     * 模糊查询字段
     */
    @JsonIgnore
    private String vague;

    /**
     * 精确查询字段
     */
    @JsonIgnore
    private String content;



    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean show) {
        isShow = show;
    }

    public String getVague() {
        return vague;
    }

    public void setVague(String vague) {
        this.vague = vague;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getChooseDate() {
        return chooseDate;
    }

    public void setChooseDate(Date chooseDate) {
        this.chooseDate = chooseDate;
    }

    public String getSortValue() {
        return sortValue;
    }

    public void setSortValue(String sortValue) {
        this.sortValue = sortValue;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }




    /**
     * 升降序
     */
    public enum SortType {
        asc, desc;
    }


    public void setDates() {
        setCreateDate(new Date());
        setModifyDate(new Date());
    }


    public String getSortParam() {
        return sortParam;
    }

    public void setSortParam(String sortParam) {
        this.sortParam = sortParam;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }


    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    @Override
    public String toString() {
        return "BasePojo{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", chooseDate=" + chooseDate +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", isShow=" + isShow +
                ", sortParam='" + sortParam + '\'' +
                ", sql='" + sql + '\'' +
                ", vague='" + vague + '\'' +
                ", content='" + content + '\'' +
                ", subjectid="  +
                '}';
    }
    public static void main(String[] args){
        String str="";
        System.out.println(str);
    }

}
