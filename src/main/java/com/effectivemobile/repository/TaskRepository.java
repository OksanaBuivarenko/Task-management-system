package com.effectivemobile.repository;

import com.effectivemobile.entity.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    boolean existsByTitle(String title);

    List<Task> findAllByPerformerId(Long id, Pageable pageable);
}
