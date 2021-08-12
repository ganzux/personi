package com.ganzux.person.rest.persistance.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ganzux.person.rest.persistance.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
//@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    public String name;

    //    @JsonBackReference
    public EmployeeDto manager;

    //    @JsonManagedReference
    public List<EmployeeDto> subordinates;

    public EmployeeDto() {
    }

    public EmployeeDto(String name) {
        this.name = name;
    }

    public EmployeeDto(String name, String manager) {
        this.name = name;
        this.manager = new EmployeeDto(manager);
    }

    public Employee toData() {
        Employee e = new Employee();

        e.setUserId(name);
        if (null != manager) {
            e.setManager(new Employee(manager.getName()));
        }

        return e;
    }

    protected String toJsonStringRecursive() {

        // base case for children
        if (null == subordinates || subordinates.isEmpty()) {
            return "\"" + name + "\": " + "{}";
        }

        // recursive case
        StringBuilder stringBuilder = new StringBuilder();


        stringBuilder
                .append("\"")
                .append(name)
                .append("\":");

        stringBuilder.append(
                subordinates.stream()
                        .map(EmployeeDto::toJsonStringRecursive)
                        .collect(Collectors.toList())
        );

        return stringBuilder.toString();
    }

    public String toJsonString() {
        return "{"
                + toJsonStringRecursive()
                    .replaceAll("\\[", "{")
                    .replaceAll("]", "}")
                + "}";
    }
}
