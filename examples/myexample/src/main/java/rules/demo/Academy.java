package rules.demo;

import java.util.Vector;

public class Academy {
    private Vector<Student> students;
    private Vector<Teacher> teachers;
    private String name;  
  
    public Academy() {  
        students = new Vector();  
        teachers = new Vector();  
    }  
  
    public void addStudent(Student student) {  
        students.addElement(student);  
    }  
  
    public void addTeacher(Teacher teacher) {  
        teachers.addElement(teacher);  
    }  
  
    public Vector<Student> getStudents() {
        return students;  
    }  
  
    public void setStudents(Vector newStudents) {  
        students = newStudents;  
    }  
    public Vector<Teacher>  getTeachers() {
        return teachers;  
    }  
  
    public void setTeachers(Vector newTeachers) {  
        teachers = newTeachers;  
    }  
  
    public String getName() {  
        return name;  
    }  
  
    public void setName(String newName) {  
        name = newName;  
    }  
}  