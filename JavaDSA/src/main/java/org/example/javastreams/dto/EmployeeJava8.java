package org.example.javastreams.dto;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmployeeJava8 {

    public static void main(String[] args) {
        List<Employee> employees = EmployeeDatabase.getAllEmployees();

//        for(Employee emp : employees){
//            System.out.println(emp);
//        }
//        employees.forEach(e -> System.out.println(e));
//        employees.forEach(System.out::println);
//        employees.stream().forEach(System.out::println);


//        List<Employee> empsFromDevDept = employees.stream()
//                .filter(e -> e.getDept().equals("Development") && e.getSalary() > 95000)
//                .collect(Collectors.toList());
//
//        System.out.println(empsFromDevDept);


//        Map<Integer, String> developmentEmps = employees.stream()
//                .filter(e -> e.getDept().equals("Development") && e.getSalary() > 95000)
//                .collect(Collectors.toMap(Employee::getId, Employee::getDept));
//        System.out.println(developmentEmps);

//        List<String> emps = employees.stream()
//                .map(Employee::getDept)
//                .distinct()
//                .collect(Collectors.toList());
//        System.out.println(emps);

//        Set<String> emps1 = employees.stream()
//                .map(Employee::getDept)
//                .collect(Collectors.toSet());
//        System.out.println(emps1);

//        List<Stream<String>> collect = employees.stream()
//                .map(e -> e.getProjects().stream().map(em -> em.getName()))
//                .collect(Collectors.toList());

//        List<String> emps = employees.stream()
//                .flatMap(e -> e.getProjects().stream())
//                .map(e -> e.getName())
//                .distinct()
//                .collect(Collectors.toList());
//
//        System.out.println(emps);

//        List<Employee> sortedEmps = employees.stream()
//                .sorted(Comparator.comparing(Employee::getSalary))
//                .collect(Collectors.toList());
//
//        sortedEmps.forEach(System.out::println);

//        List<Employee> descSortedEmps = employees.stream()
//                .sorted(Collections.reverseOrder(Comparator.comparing(Employee::getSalary)))
//                .collect(Collectors.toList());
//
//        descSortedEmps.forEach(System.out::println);

// Max/Min
//        Optional<Employee> highestPaidEmp = employees.stream()
//                .max(Comparator.comparingDouble(Employee::getSalary));
//        highestPaidEmp.ifPresent(System.out::println);

//        Optional<Employee> lowestPaidEmp = employees.stream()
//                .min(Comparator.comparingDouble(Employee::getSalary));
//        lowestPaidEmp.ifPresent(System.out::println);


//Grouping By

//        Map<String, List<Employee>> groupEmpByDept = employees.stream()
//                .collect(Collectors.groupingBy(Employee::getDept));
//
//        System.out.println(groupEmpByDept);

//        Map<String, List<String>> groupEmpByGender = employees.stream()
//                .collect(Collectors.groupingBy(
//                                Employee::getGender,
//                                Collectors.mapping(Employee::getName, Collectors.toList())
//                        )
//                );
//        System.out.println(groupEmpByGender);

//        Map<String, List<String>> groupEmpByDept = employees.stream()
//                .collect(Collectors.groupingBy(
//                                Employee::getDept,
//                                Collectors.mapping(Employee::getName, Collectors.toList())
//                        )
//                );
//        System.out.println(groupEmpByDept);


//        Map<String, Long> stringLongMap = employees.stream()
//                .collect(Collectors.groupingBy(
//                                Employee::getDept,
//                                Collectors.mapping(Employee::getName, Collectors.counting())
//                        )
//                );
//        System.out.println(stringLongMap);

//        Map<String, Long> stringLongMap = employees.stream()
//                .collect(Collectors.groupingBy(
//                                Employee::getGender,
//                                Collectors.mapping(Employee::getName, Collectors.counting())
//                        )
//                );
//        System.out.println(stringLongMap);


//        Optional<Employee> SecondHighestSalaryEmp = employees.stream()
//                .sorted(Collections.reverseOrder(Comparator.comparing(Employee::getSalary)))
//                .distinct()
//                .skip(1)
//                .findFirst();
//
//        SecondHighestSalaryEmp.ifPresent(e -> System.out.println(e.getName()+" ->"+ e.getSalary()));
//
//        if(SecondHighestSalaryEmp.isPresent()){
//            System.out.println(SecondHighestSalaryEmp.get());
//        }

//        Employee SecondHighestSalaryEmp = employees.stream()
//                .sorted(Collections.reverseOrder(Comparator.comparing(Employee::getSalary)))
//                .distinct()
//                .skip(1)
//                .findFirst()
//                .orElse(new Employee());
//
//        System.out.println(SecondHighestSalaryEmp.getName()+" ->"+ SecondHighestSalaryEmp.getSalary());


//        Employee findAnyElement = employees.stream()
//                .findAny()
//                .orElse(new Employee());
//
//        boolean matchAnyElement = employees.stream()
//                .anyMatch(e -> e.getDept().equals("Development"));
//
//        boolean matchAllElement = employees.stream()
//                .allMatch(e -> e.getDept().equals("Development"));
//
//        boolean matchNoneElement = employees.stream()
//                .noneMatch(e -> e.getDept().equals("Development"));


            List<Employee> employeeList = employees.stream()
                    .sorted(Comparator.comparing(Employee::getSalary).reversed())
                    .limit(3)
                    .skip(1)
                    .collect(Collectors.toList());

           employeeList.forEach(e -> System.out.println(e.getName()+ " "+ e.getSalary())); ;

    }
}