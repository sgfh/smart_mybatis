package com.smart.mybatis.pojo;

import com.smart.mybatis.annotation.OneToOne;
import com.smart.mybatis.annotation.Table;

@Table("tb_student")
public class Student extends BasePojo {
    /**
     * 姓名
     */
    private String name;
    /**
     * 老师id
     */
    private Long teacher_id;

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

    public Long getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(Long teacher_id) {
        this.teacher_id = teacher_id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
