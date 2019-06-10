package com.smart.mybatis.controller;

import com.github.pagehelper.PageInfo;
import com.smart.mybatis.model.Student;
import com.smart.mybatis.model.Teacher;
import com.smart.mybatis.pojo.BaseVo;
import com.smart.mybatis.service.StudentService;
import com.smart.mybatis.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@SuppressWarnings("all")
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    StudentService studentService;
    @Autowired
    TeacherService teacherService;

    /**
     * 查询student分页
     */
    @GetMapping(value = "/page")
    public PageInfo<Student> page() {
        PageInfo<Student> baseVo = new PageInfo<>();

        return baseVo;
    }

    /**
     * 新增学生
     */
    @GetMapping(value = "/add")
    public BaseVo<Student> add(Student student) {
        BaseVo<Student> baseVo = new BaseVo<>();
        student.setDates();
        studentService.insert(student);
        return baseVo;
    }

    /**
     * 更新学生
     *
     * @param id:学生id
     * @param name:姓名
     */
    @GetMapping(value = "/update")
    public BaseVo<Student> update(Student student) {
        BaseVo<Student> baseVo = new BaseVo<>();
        if (null == student.getId()) {
            baseVo.setCode(BaseVo.CODE_ERR);
            return baseVo;
        }
        student.setModifyDate(new Date());
        studentService.update(student);
        return baseVo;
    }

    /**
     * 新增老师
     *
     * @param name:姓名
     */
    @GetMapping(value = "/teacher/add")
    public BaseVo<Teacher> teacherAdd(Teacher teacher) {
        BaseVo<Teacher> baseVo = new BaseVo<>();
        teacher.setDates();
        teacherService.insert(teacher);
        return baseVo;
    }

    /**
     * 查询学生集合
     */
    @GetMapping(value = "/list")
    public BaseVo<List<Student>> list() {
        BaseVo<List<Student>> baseVo = new BaseVo<>();
        baseVo.setContent(studentService.list(new Student(), Student.class));
        return baseVo;
    }

    /**
     * 查询老师集合
     */
    @GetMapping(value = "/teacher/list")
    public BaseVo<List<Teacher>> teacherList() {
        BaseVo<List<Teacher>> baseVo = new BaseVo<>();
        baseVo.setContent(teacherService.list(new Teacher(), Teacher.class));
        return baseVo;
    }
}