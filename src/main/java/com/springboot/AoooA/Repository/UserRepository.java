package com.springboot.AoooA.Repository;

import com.springboot.AoooA.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
   User findByUserEmail(String email);
   
   User findByUserNameAndUserTelAndUserEmail(String userName, String userTel, String userEmail);
}