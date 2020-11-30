package rules.demo;

import java.util.Vector;

public class Teacher {
    private String name;  
    private Vector<String> certifications;
  
    public Teacher() {  
        certifications = new Vector();  
    }  
  
    public void addCertification(String certification) {  
        certifications.addElement(certification);  
    }  
  
    public String getName() {  
        return name;  
    }  
  
    public void setName(String newName) {  
        name = newName;  
    }  
  
    public void setCertifications(Vector certifications) {  
        this.certifications = certifications;  
    }  
  
    public Vector getCertifications() {  
        return certifications;  
    }  
}  
