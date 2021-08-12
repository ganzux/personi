package com.ganzux.person.rest.persistance.service;

import com.ganzux.person.rest.persistance.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {

  EmployeeDto get(String userId);

  List<EmployeeDto> getAll();

  boolean save(List<EmployeeDto> employeeDtoList);

  String[] getSecondSupervisor(String employeeName);
}
