package com.example.demo;

import java.util.Optional;

public interface  UserRep extends org.springframework.data.repository.CrudRepository<UserEntiy,Long> {
  
    
    boolean existsByName(String name);
    UserEntiy findByName(String name);
}
