package org.nkcoder.controller;

import org.nkcoder.domain.Employee;
import org.nkcoder.exception.EmployeeNotFoundException;
import org.nkcoder.service.EmployeeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

  private EmployeeService employService;

  public EmployeeController(EmployeeService employService) {
    this.employService = employService;
  }

  @GetMapping("/employees/{name}")
  public Employee getEmployee(@PathVariable("name") String name) {

    Employee employee = employService.getEmployee(name);
    if (employee == null) {
      throw new EmployeeNotFoundException();
    }

    return employee;
  }

}
