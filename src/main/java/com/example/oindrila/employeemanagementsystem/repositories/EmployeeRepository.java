package com.example.oindrila.employeemanagementsystem.repositories;

import com.example.oindrila.employeemanagementsystem.entities.employee.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long>, QueryByExampleExecutor<EmployeeEntity> {
}
