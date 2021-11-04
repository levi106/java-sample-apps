package com.example.dbtest.service;

import java.util.List;

import com.example.dbtest.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public Boolean insert(String name) {
        int ret = jdbcTemplate.update("INSERT INTO [dbo].[User] (Name) VALUES (?)", name);
        return ret == 1;
    }

    public User find(int id) {
        return jdbcTemplate.queryForObject("SELECT ID, Name FROM [dbo].[User] WHERE ID = ?",
            (rs, rowNum) -> new User(
                rs.getInt("ID"),
                rs.getString("Name")
            ), id);
    }

    public List<User> findAll() {
        return jdbcTemplate.query("SELECT ID, Name FROM [dbo].[User]",
            (rs, rowNum) -> new User(
                rs.getInt("ID"),
                rs.getString("Name")));
    }
}
