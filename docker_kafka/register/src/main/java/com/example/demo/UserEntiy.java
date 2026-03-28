package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UserEntiy {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    

    private String name;
    private String password;
   
    private String role = "USER";

    public UserEntiy() {}

    public String getName() { return name; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
  

    public void setName(String name) { this.name = name; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
}

