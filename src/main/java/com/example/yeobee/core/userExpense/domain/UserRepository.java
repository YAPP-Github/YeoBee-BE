package com.example.yeobee.core.userExpense.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserExpense, Long> {
}
