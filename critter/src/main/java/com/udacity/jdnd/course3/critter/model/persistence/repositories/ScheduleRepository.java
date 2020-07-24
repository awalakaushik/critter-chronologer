package com.udacity.jdnd.course3.critter.model.persistence.repositories;

import com.udacity.jdnd.course3.critter.model.persistence.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
