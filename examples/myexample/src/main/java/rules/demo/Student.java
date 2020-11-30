package rules.demo;

import java.util.Vector;

public class Student {
    private Vector<Course> courses;
    private String name;  
    private String division;  
  
    public Student() {  
        courses = new Vector();  
    }  
  
    public void addCourse(Course course) {  
        courses.addElement(course);  
    }  
  
    public String getName() {  
        return name;  
    }  
    public void setName(String newName) {  
        name = newName;  
    }  
  
    public String getDivision() {  
        return division;  
    }  
  
    public void setDivision(String newDivision) {  
        division = newDivision;  
    }  
  
    public void setCourses(Vector courses) {  
        this.courses = courses;  
    }  
  
    public Vector getCourses() {  
        return courses;  
    }  
} 