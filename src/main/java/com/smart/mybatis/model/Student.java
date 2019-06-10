package com.smart.mybatis.model;

import com.smart.mybatis.annotation.Column;
import com.smart.mybatis.annotation.OneToOne;
import com.smart.mybatis.annotation.Table;
import com.smart.mybatis.pojo.BasePojo;

@Table("tb_student")
public class Student extends BasePojo {
    /**
     * 姓名
     */
    @Column(value = "name", columnDefinition = "VARCHAR(20)")
    private String name;
    /**
     * 老师id
     */
    @Column(value = "teacher_id", columnDefinition = "BIGINT(10)")
    private Long teacherId;

    /**
     * 所属老师
     */
    @OneToOne(value = "teacher_id")
    private Teacher teacher;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
}
