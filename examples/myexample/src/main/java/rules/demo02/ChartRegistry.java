package rules.demo02;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class ChartRegistry {
    
    public  List<ChartConfig> registry = new ArrayList<ChartConfig>();
    
    public void addChart(ChartConfig obj) {
        registry.add(obj);
    }
    
    public  List<ChartConfig> getRegistry() {
        return registry;
    }
    
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}