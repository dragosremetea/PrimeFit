package com.primefit.tool.repository;

import com.primefit.tool.model.Training;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Integer> {

    Optional<Training> findByName(String name);

    Optional<Training> findById(@NotNull Integer id);
}
