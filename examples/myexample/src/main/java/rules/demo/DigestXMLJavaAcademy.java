package rules.demo;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;

import java.util.Map;
import java.util.Vector;

public class DigestXMLJavaAcademy {
    public void digest() {
        try {
            //Create Digester using rules defined in academyRules.xml  
            Digester digester = DigesterLoader.createDigester(this.getClass().getClassLoader().getResource("academyRules.xml"));
            
            //Parse academy.xml using the Digester to get an instance of Academy  
            Academy a = (Academy) digester.parse(this.getClass().getClassLoader().getResourceAsStream("academy.xml"));
            
            Vector<Student> vStud = a.getStudents();
            Vector<Teacher>  vTeach = a.getTeachers();
            
            
            for (int i = 0; i < vStud.size(); i++) {
                Map map = PropertyUtils.describe(vStud.get(i));
                System.out.println("Student>> " + map);
                for(Object obj :map.keySet()){
                    String key = (String) obj;
                    System.out.println(key + "----------->" + map.get(key));
                }
            }
            
            for (int i = 0; i < vTeach.size(); i++) {
                System.out.println("Teacher>> " + PropertyUtils.describe(vTeach.get(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        DigestXMLJavaAcademy xmlDigest = new DigestXMLJavaAcademy();
        xmlDigest.digest();
    }
}  