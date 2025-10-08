package com.sairajtravels.site.repository;

import com.sairajtravels.site.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {}
