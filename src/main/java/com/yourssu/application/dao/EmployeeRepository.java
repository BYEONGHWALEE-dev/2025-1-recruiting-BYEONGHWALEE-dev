package com.yourssu.application.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

public interface EmployeeRepository extends JpaRepository<User, Integer> {


}
