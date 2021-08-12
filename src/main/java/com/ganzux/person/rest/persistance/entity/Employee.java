package com.ganzux.person.rest.persistance.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ganzux.person.rest.persistance.dto.EmployeeDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Data
//@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "employee")
public class Employee {


  @Id
  @Size(min = 1, max = 15)
  @Column(name = "user_id", unique = true)
  private String userId;

  @ManyToOne
  @JoinColumn(name = "manager")
  @JsonBackReference
  private Employee manager;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "manager")
  @JsonManagedReference
  private List<Employee> subordinates;

  public Employee(String userId) {
    this.userId = userId;
  }

  public Employee() {
  }

  // TODO integrate with a Mapper - no need for 1 field
  public EmployeeDto toDto() {

    EmployeeDto userDto = new EmployeeDto(userId);
    // Infinite loop if we don't check id
//    if (null != manager && !userId.equals(manager.getUserId())) {
//        userDto.setManager(manager.toDto());
//    }

    if (null != subordinates && !subordinates.isEmpty()) {
      userDto.setSubordinates(subordinates.stream().map(Employee::toDto).collect(Collectors.toList()));

    }

    return userDto;
  }

}
