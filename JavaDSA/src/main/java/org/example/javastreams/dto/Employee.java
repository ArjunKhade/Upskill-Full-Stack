package org.example.javastreams.dto;
import java.util.List;

public class Employee {

    private String name ;
    private int id;
    private double salary;
    private String dept;
    private List<Project> projects;
    private String gender;

    public Employee() {
    }

    public Employee(int id, String name, double salary, String dept, List<Project> projects, String gender){
      this.id = id;
      this.name = name;
      this.salary = salary;
      this.dept = dept;
      this.projects = projects;
      this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", dept='" + dept + '\'' +
                ", projects=" + projects +
                ", gender='" + gender + '\'' +
                '}';
    }
}
