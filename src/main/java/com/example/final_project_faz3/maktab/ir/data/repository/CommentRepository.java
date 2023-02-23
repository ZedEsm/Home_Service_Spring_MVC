package com.example.final_project_faz3.maktab.ir.data.repository;

import com.example.final_project_faz3.maktab.ir.data.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}