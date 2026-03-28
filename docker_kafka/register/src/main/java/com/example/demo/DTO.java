package com.example.demo;

public class DTO {

    private String name;
    private String password;
 

   public DTO(String name, String password) {// конструктор
   
        this.name = name;
        this.password = password;
     
    }

  
    public String getName() { return name; }
    public String getPassword() { return password; }

}