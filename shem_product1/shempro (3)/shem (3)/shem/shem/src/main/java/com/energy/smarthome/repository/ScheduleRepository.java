package com.energy.smarthome.repository;

import com.energy.smarthome.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // Used by SchedulerService to find tasks that are waiting to run.
    List<Schedule> findByStatus(String status);
}