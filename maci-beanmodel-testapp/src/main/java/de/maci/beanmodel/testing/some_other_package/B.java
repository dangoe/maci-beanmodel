/**
 * Copyright 2015 Daniel Götten
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
