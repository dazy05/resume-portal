package com.example.resumeportal;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.resumeportal.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	Optional<User> findByUserName(String userName);
	

}
