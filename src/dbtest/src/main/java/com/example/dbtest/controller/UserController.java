package com.example.dbtest.controller;

import java.util.List;

import com.example.dbtest.entity.User;
import com.example.dbtest.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method=RequestMethod.GET, produces="application/json")
    public List<User> get() {
        List<User> users = userService.findAll();
        return users;
    }

    @RequestMapping(value="/{id}", method=RequestMethod.GET, produces="application/json")
    public User get(@PathVariable int id) {
        User user = userService.find(id);
        return user;
    }

    @RequestMapping(method=RequestMethod.POST, produces="application/json")
    public void post(@RequestBody String name) {
        userService.insert(name);
    }
}
