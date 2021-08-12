package com.ganzux.person.rest.persistance.repository;


import com.ganzux.person.rest.persistance.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

  Employee getEmployeeByUserId(String userId);

  List<Employee> findAllByManagerIsNull();

}
