package org.example.javastreams.dto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class EmployeeDatabase {

    public static List<Employee> getAllEmployees() {
        Project p1 = new Project("Delta", "P001", "Google", "Robert");
        Project p2 = new Project("Beta", "P002", "Amazon", "Sarah");
        Project p3 = new Project("Alpha", "P003", "Microsoft", "Robert");
        Project p4 = new Project("Omega", "P004", "Apple", "Michael");
        Project p5 = new Project("Gamma", "P005", "Netflix", "Sarah");
        Project p6 = new Project("Sigma", "P006", "Meta", "Michael");

        return Arrays.asList(
                new Employee(1, "John Doe", 85000.0, "Development", Arrays.asList(p1, p2), "Male"),
                new Employee(2, "Jane Smith", 95000.0, "Development", Arrays.asList(p2, p3), "Female"),
                new Employee(3, "Robert Brown", 60000.0, "QA", Arrays.asList(p1), "Male"),
                new Employee(4, "Emily Davis", 72000.0, "QA", Arrays.asList(p3, p4), "Female"),
                new Employee(5, "Michael Wilson", 55000.0, "HR", Collections.emptyList(), "Male"),
                new Employee(6, "Sarah Taylor", 105000.0, "Development", Arrays.asList(p4, p5), "Female"),
                new Employee(7, "David Clark", 68000.0, "DevOps", Arrays.asList(p5, p6), "Male"),
                new Employee(8, "Sophia Martinez", 78000.0, "DevOps", Arrays.asList(p1, p6), "Female"),
                new Employee(9, "James Anderson", 48000.0, "HR", Collections.emptyList(), "Male"),
                new Employee(10, "Olivia Thomas", 88000.0, "Finance", Arrays.asList(p2, p4), "Female")
        );
    }

    public static List<Employee> getEmployees() {
        return getAllEmployees();
    }

    public static List<Employee> getAll() {
        return getAllEmployees();
    }

    public static Stream<Employee> stream() {
        return getAllEmployees().stream();
    }
}
