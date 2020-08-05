package com.books.dao;

import com.books.entities.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email,Long> {
    Email findByName(String name);
}
