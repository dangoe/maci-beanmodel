package de.maci.beanmodel.testing;

import de.maci.beanmodel.Bean;
import de.maci.beanmodel.IgnoredProperty;

@Bean
public class A extends IgnoredClass<String> {

    private String a;
    private String b;

    @IgnoredProperty
    private String c;


    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }
}
