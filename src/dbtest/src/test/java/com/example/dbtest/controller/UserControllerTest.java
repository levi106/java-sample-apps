package com.example.dbtest.controller;

import java.util.Arrays;
import java.util.List;

import com.example.dbtest.entity.User;
import com.example.dbtest.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    public void getReturnsAllUsers() throws Exception {
        List<User> users = Arrays.asList(
            new User(1, "user1"),
            new User(2, "user2")
        );
        when(userService.findAll()).thenReturn(users);
        mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].name", is("user1")))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].name", is("user2")));
    }

    @Test
    public void getId1ReturnsUser1() throws Exception {
        User user = new User(1, "user1");
        when(userService.find(1)).thenReturn(user);
        mockMvc.perform(get("/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("user1")));
    }

    @Test
    public void postValidUserReturnsSuccess() throws Exception {
        when(userService.insert("user10")).thenReturn(true);
        mockMvc.perform(post("/users")
            .contentType(MediaType.TEXT_PLAIN)
            .content("user10"))
            .andExpect(status().isOk());
    }
}
