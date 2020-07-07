package com.jocata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jocata.entity.User;

public interface UserRepository extends  JpaRepository<User, Integer> {

	User findByUser(String username);

}
