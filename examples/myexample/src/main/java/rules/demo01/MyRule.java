package rules.demo01;

import org.apache.commons.digester.Rule;

/**
 * 当匹配到模式时，会触发规则处理，具体的规则处理机制是由这个org.apache.commons.digester3.Rule接口封装的，该接口定义了以下几个方法：
 * begin() - 匹配到xml元素开始标记时，调用该方法；
 * body() - 匹配到xml元素body时，调用该方法；
 * end() - 匹配到xml元素结束标记时，调用该方法；
 * finish() - 当所有解析方法解析完毕后，调用该方法，用于清楚临时数据等；
 */
public class MyRule extends Rule {
    String clazs;
    String methodName;
    
    public MyRule(String methodName) {
        this.methodName = methodName;
    }
    
    public MyRule(String methodName, String clazs) {
        this.methodName = methodName;
        this.clazs = clazs;
    }
}
