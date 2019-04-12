package org.nkcoder.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.nkcoder.domain.Employee;
import org.nkcoder.exception.EmployeeNotFoundException;
import org.nkcoder.repo.EmployeeRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {

  @Mock
  private EmployeeRepository employeeRepository;

  private EmployeeService employeeService;

  @BeforeEach
  public void setUp() {
    employeeService = new EmployeeService(employeeRepository);
  }

  @Test
  public void getEmployee_returnEmployeeInfo() {
    String name = "daniel";
    given(employeeRepository.findByName(name)).willReturn(new Employee(name, "junior"));

    Employee employee = employeeService.getEmployee(name);

    SoftAssertions softAssertions = new SoftAssertions();
    softAssertions.assertThat(employee).isNotNull();
    softAssertions.assertThat(employee.getName()).isEqualTo(name);
    softAssertions.assertThat(employee.getGrade()).isEqualTo("junior");
    softAssertions.assertAll();

  }

  @Test
  public void getEmployee_notFound() {
    given(employeeRepository.findByName(anyString())).willReturn(null);

    assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployee("notExist"));

  }

}
