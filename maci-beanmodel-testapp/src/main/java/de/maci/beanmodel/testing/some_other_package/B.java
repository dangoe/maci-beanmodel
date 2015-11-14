package de.maci.beanmodel.testing.some_other_package;

import de.maci.beanmodel.Bean;
import de.maci.beanmodel.IgnoredProperty;
import de.maci.beanmodel.testing.A;

import java.util.List;

@Bean
public class B extends A {

    private String a;
    private String x;
    private List<String> y;

    @IgnoredProperty
    private String z;

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public List<String>  getY() {
        return y;
    }

    public void setY(List<String>  y) {
        this.y = y;
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }

    @Override
    public String getA() {
        return a;
    }

    @Override
    public void setA(String a) {
        this.a = a;
    }
}
