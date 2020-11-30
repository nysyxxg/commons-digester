package rules.demo02;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

public class DigesterTest {
    
    public static void main(String[] args) {
        try {
            String charts = "D:\\small-light-bi-olap\\commons-digester\\examples\\myexample\\src\\main\\resources\\charts.xml";
            String rule = "D:\\small-light-bi-olap\\commons-digester\\examples\\myexample\\src\\main\\resources\\rule.xml";
            
            DigesterTest test = new DigesterTest();
            System.out.println("================1======= getChartRegistryWithCodeRule ==========================");
            ChartRegistry c = test.getChartRegistryWithCodeRule(new File(charts));
            System.out.println(c);
            
            System.out.println("================2======= getChartRegistryWithCodeRule ==========================");
            System.out.println(test.getChartRegistryWithFileRule(new File(charts), new File(rule)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //代码中设定规则
    private ChartRegistry getChartRegistryWithCodeRule(File charts) throws IOException,  org.xml.sax.SAXException {
        
        InputStream is = new FileInputStream(charts);
        Digester digester = new Digester();
        digester.setValidating(false);//不进行XML与相应的DTD的合法性验证
        
        digester.push(new ChartRegistry());                         //创建一个ChartRegistry对象, 你这样用这种形式建立顶对象（选用这种方式，注释下一行）
        //digester.addObjectCreate("charts", ChartRegistry.class);  //创建一个ChartRegistry对象, 你也这样用这种形式建立顶对象（选用这种方式，注释上一行）
        digester.addSetProperties("charts");
        digester.addObjectCreate("charts/chart", ChartConfig.class); //当遇到<chart>时创建一个ChartConfig对象
        digester.addSetProperties("charts/chart");  //根据<chart>元素的属性(attribute)，对刚创建的ChartConfig对象的属性(property)进行设置
        digester.addBeanPropertySetter("charts/chart/legendVisible");//当遇到<chart>的子元素<legendVisible>时将子元素值设置为ChartConfig对象的legendVisible属性的setter方法
        digester.addBeanPropertySetter("charts/chart/type");        //参考上面注释
        digester.addBeanPropertySetter("charts/chart/width");        //参考上面注释
        digester.addBeanPropertySetter("charts/chart/description"); //参考上面注释
     
        digester.addSetNext("charts/chart", "addChart");        //当再次遇到<charts>的子元素<chart>时调用顶对象的addChart方法。
        ChartRegistry c = (ChartRegistry) digester.parse(is);  //分析结束后，返回根元素
        Collections.sort(c.getRegistry());
        return c;
    }
    
    //配置中设定规则
    private ChartRegistry getChartRegistryWithFileRule(File charts, File rule) throws IOException, SAXException {
        Digester digester = DigesterLoader.createDigester(rule.toURL());
        ChartRegistry c = (ChartRegistry) digester.parse(charts);
        Collections.sort(c.getRegistry());
        return c;
    }
}