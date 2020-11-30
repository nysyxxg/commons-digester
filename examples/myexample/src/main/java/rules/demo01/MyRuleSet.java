package rules.demo01;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSetBase;
import org.apache.commons.digester.SetNextRule;

public class MyRuleSet extends RuleSetBase {
    
    String classz;
    String methodName;
    
    public MyRuleSet(String classz, String methodName) {
        this("", classz, methodName);
    }
    
    public MyRuleSet(String prefix, String classz, String methodName) {
        super();
        this.prefix = prefix;
        this.classz = classz;
        this.methodName = methodName;
    }
    
    protected String prefix = null;
    
    public void addRuleInstances(Digester digester) {
        digester.addObjectCreate(prefix + "foo/bar", "rules.demo01.Foo");
        digester.addSetProperties(prefix + "foo/bar");
    
    }
    
}