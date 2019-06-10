package com.smart.mybatis.pojo;

import com.smart.mybatis.annotation.Column;
import com.smart.mybatis.annotation.OneToMany;
import com.smart.mybatis.annotation.Table;

import java.util.List;

@Table("tb_teacher")
public class Teacher extends BasePojo {
    /**
     * 姓名
     */
    @Column(value = "name", columnDefinition = "VARCHAR(20)")
    private String name;

    /**
     * 学生集合
     */
    @OneToMany(value = "teacher_id")
    private List<Student> student;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudent() {
        return student;
    }

    public void setStudent(List<Student> student) {
        this.student = student;
    }
}
