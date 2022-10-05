package com.huseyinari.mockitotestexample.repository;

import com.huseyinari.mockitotestexample.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE ID = 1")
    public Optional<User> getFirstUser();
}
