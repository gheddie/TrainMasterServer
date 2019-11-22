package de.gravitex.trainmaster;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import de.gravitex.trainmaster.entity.Employee;
import de.gravitex.trainmaster.repo.EmployeeRepository;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void testOK() {

        employeeRepository.save(new Employee("admin", "admin", "admin@gmail.com"));
        employeeRepository.save(new Employee("admin", "admin", "admin@gmail.com"));
        assertEquals(2, employeeRepository.findAll().size());
    }
    
    @Test
    public void testError() {

        employeeRepository.save(new Employee("admin", "admin", "admin@gmail.com"));
        employeeRepository.save(new Employee("admin", "admin", "admin@gmail.com"));
        assertEquals(3, employeeRepository.findAll().size());
    }
}