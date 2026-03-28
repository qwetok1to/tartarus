/* 
package com.example.demo;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;


@org.springframework.stereotype.Controller
public class Controller {
    

    @Autowired
    Servise servise;

    @QueryMapping
    public DTO user(@Argument Long id) {
        return servise.getUserById(id);
    }

    @QueryMapping
    public List<DTO> users() {
        return servise.listUsers();
    }

    @MutationMapping
    public AuthResponse login(@Argument String username, @Argument String password) {
        return new AuthResponse(servise.loginUser(username, password));
    }

    @MutationMapping
    public AuthResponse register(@Argument String username, @Argument String password) {
        return new AuthResponse(servise.registerUser(username, password));
    }
  

}




*/