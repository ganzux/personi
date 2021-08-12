package com.ganzux.person.rest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ganzux.person.rest.persistance.dto.EmployeeDto;
import com.ganzux.person.rest.persistance.entity.Employee;
import com.ganzux.person.rest.persistance.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("employees")
public class EmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService userService;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/all")
    public ResponseEntity<String> createEmployees(@RequestBody Map<String, String> employeeManagerData)
            throws Exception {

        LOGGER.info("createEmployees operation invoked {}", employeeManagerData);

        List<EmployeeDto> newemployees = new ArrayList<>();

        for (Map.Entry<String,String> entry : employeeManagerData.entrySet()) {
            newemployees.add(new EmployeeDto(entry.getKey(), entry.getValue()));
        }

        userService.save(newemployees);

        return new ResponseEntity<String>("OK", HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() throws Exception {

        List<EmployeeDto> allemployees = new ArrayList<>();


        allemployees = userService.getAll();

        System.out.println(allemployees.get(0).toJsonString());

        return new ResponseEntity<List<EmployeeDto>>(allemployees, HttpStatus.CREATED);
    }



    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/supervisors/{employeeName}", produces = "application/json")
    public @ResponseBody
    ResponseEntity<String[]> getSupervisors(@PathVariable String employeeName) {
        LOGGER.info("getSupervisors operation invoked {}", employeeName);

        String[] supervisors;

        try {
            supervisors = userService.getSecondSupervisor(employeeName);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        return new ResponseEntity<>(supervisors, HttpStatus.OK);
    }
//
//    @ResponseStatus(HttpStatus.OK)
//    @GetMapping(value = "/projects", produces = "application/json")
//    public @ResponseBody
//    ResponseEntity<List<Option>> getAllProjects() {
//        LOGGER.info("getLastCrosswalkFile operation invoked");
//
//        List<ProjectDto> allProjects;
//        List<Option> allOptions;
//
//        try {
//            allProjects = projectService.findAll();
//
//            allOptions = allProjects.stream()
//                    .map(project -> new Option(project.getProjectName(), String.valueOf(project.getId())))
//                    .collect(Collectors.toList());
//
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        LOGGER.info("Found crosswalk file operation ");
//
//        return new ResponseEntity<>(allOptions, HttpStatus.OK);
//    }
//
//    @ResponseStatus(HttpStatus.OK)
//    @GetMapping(value = "/ts/{userId}", produces = "application/json")
//    public @ResponseBody
//    ResponseEntity<List<TimeSheetDto>> getLastTimeSheet(@PathVariable String userId) {
//        LOGGER.info("getLastCrosswalkFile operation invoked");
//
//        List<TimeSheetDto> lastTimeSheets;
//
//        try {
//            lastTimeSheets = timesheetService.getLasts(userId);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        LOGGER.info("Found crosswalk file operation ");
//
//        return new ResponseEntity<>(lastTimeSheets, HttpStatus.OK);
//    }
//
//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping(value = "/ts")
//    public ResponseEntity<String> addNewRow(@RequestBody @Valid List<NewTimeSheetRequest> timeSheetRequests)
//            throws Exception {
//
//        timesheetService.save(timeSheetRequests);
//
//        return new ResponseEntity<String>("OK", HttpStatus.CREATED);
//    }

}
