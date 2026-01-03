package com.example.demo.repository;

import com.example.demo.domain.StampBoard;
import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StampBoardRepository extends JpaRepository<StampBoard, Long> {
    List<StampBoard> findByUser(User user);
}


