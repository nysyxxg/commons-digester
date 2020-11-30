package rules.demo01;

import java.io.IOException;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.xml.sax.SAXException;

public class Main {
    
    public static void main(String[] args) {
        try {
            //1、创建Digester对象实例
            Digester digester = new Digester();
            //2、配置属性值
            digester.setValidating(false);
            
            //3、push对象到对象栈
            //digester.push(new Foo());
            //4、设置匹配模式、规则
            digester.addObjectCreate("foo", "rules.demo01.Foo");
            digester.addSetProperties("foo");
            
            digester.addObjectCreate("foo/bar",  "rules.demo01.Bar");
            digester.addSetProperties("foo/bar");
            
            // 添加规则处理： 第1种
//            digester.addSetNext("foo/bar", "addBar", "rules.demo01.Bar");
//
            // 添加规则处理： 第2种
//            digester.addSetNext("foo/bar", "addBar", Bar.class.getName());
    
            // 添加规则处理： 第3种
//            Rule rule = new SetNextRule("addBar");
//            digester.addRule("foo/bar", rule);
            
            // 添加规则处理： 第4种--自定义扩展规则
            Rule rule = new MyRule("addBar",Bar.class.getName());
            digester.addRule("foo/bar", rule);
    
//            MyRuleSet myRuleSet = new MyRuleSet("", "addBar");
//            digester.addRuleSet(myRuleSet);
            
            //5、开始解析
            Foo foo = (Foo) digester.parse(Main.class.getClassLoader().getResourceAsStream("example.xml"));
            
            //6、打印解析结果
            System.out.println(foo.getName());
            for (Bar bar : foo.getBarList()) {
                System.out.println(bar.getId() + "," + bar.getTitle());
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}