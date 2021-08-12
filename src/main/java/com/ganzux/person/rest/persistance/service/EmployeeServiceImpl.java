package com.ganzux.person.rest.persistance.service;

import com.ganzux.person.rest.mapper.MapStructMapper;
import com.ganzux.person.rest.persistance.dto.EmployeeDto;
import com.ganzux.person.rest.persistance.entity.Employee;
import com.ganzux.person.rest.persistance.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeServiceImpl implements EmployeeService {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

  @Autowired private EmployeeRepository userRepository;

//  @Autowired
//  private MapStructMapper mapstructMapper;

  @Override
  public boolean save(List<EmployeeDto> employeeDtoList) {

    for (EmployeeDto employeeDto : employeeDtoList) {

      // if there is manager, try to find it
      if (employeeDto.getManager() != null) {
        EmployeeDto manager = get(employeeDto.getManager().getName());
        if (null == manager) {
          userRepository.save(new Employee(employeeDto.getManager().getName()));
        }
      }

//      userRepository.save(mapstructMapper.employeeDtoToEmployee(employeeDto));
      userRepository.save(employeeDto.toData());
    }


    return true;
  }

  @Override
  public List<EmployeeDto> getAll() {
    List<Employee> resolutionRow = userRepository.findAllByManagerIsNull();

//    return resolutionRow.stream()
//            .map(mapstructMapper::employeeToEmployeeDto)
//            .collect(Collectors.toList());

    return resolutionRow.stream()
            .map(Employee::toDto)
            .collect(Collectors.toList());
  }

  @Override
  public EmployeeDto get(String userId) {
    LOGGER.info("get operation invoked {}", userId);

    Employee user = userRepository.getEmployeeByUserId(userId);

    if (user == null) {
      return null;
    }

//    EmployeeDto userDto = mapstructMapper.employeeToEmployeeDto(user);
    EmployeeDto userDto = user.toDto();

    return userDto;
  }

  @Override
  public String[] getSecondSupervisor(String employeeName) {

    String directSupervisor = null;
    String superSupervisor = null;

    Employee employeeData = userRepository.getEmployeeByUserId(employeeName);

    if (null != employeeData.getManager()) {
      directSupervisor = employeeData.getManager().getUserId();

      // TODO this could perfectly be recursive if we have to do it again
      Employee supervisorData = userRepository.getEmployeeByUserId(directSupervisor);
      if (null != supervisorData.getManager()) {
        superSupervisor = supervisorData.getManager().getUserId();
      }
    }

    return new String[]{directSupervisor, superSupervisor};
  }

}
