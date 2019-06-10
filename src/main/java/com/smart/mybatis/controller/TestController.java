package com.smart.mybatis.controller;

import com.github.pagehelper.PageInfo;
import com.smart.mybatis.pojo.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class TestController {
    /**
     * 查询student分页
     */
    @GetMapping(value = "/page")
    public PageInfo<Student> page() {
        PageInfo<Student> baseVo = new PageInfo<>();

        return baseVo;
    }
}