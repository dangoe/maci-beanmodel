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
package de.maci.beanmodel;

/**
 * Identifies a single property of a {@link Bean}.
 *
 * @author Daniel Götten <daniel.goetten@googlemail.com>
 * @since 09.04.2014
 */
public final class Property<T> {

    private final Class<?> declaringClass;

    private final String name;
    private final Class<T> type;

    public Property(final Class<?> declaringClass, final String name, final Class<T> type) {
        super();

        this.name = name;
        this.type = type;

        this.declaringClass = declaringClass;
    }

    /**
     * Returns the {@link Class} object this {@link Property} belongs to.
     *
     * @return An object representing the declaring class of the underlying
     *         member.
     */
    public Class<?> declaringClass() {
        return declaringClass;
    }

    /**
     * Returns the name of the property represented by this {@link Property}
     * object.
     *
     * @return The simple of the underlying member.
     */
    public String name() {
        return name;
    }

    /**
     * Returns a {@link Class} object that identifies the declared type for the
     * property represented by this {@link Property} object.
     *
     * @return A {@link Class} object identifying the declared type of the
     *         property represented by this object.
     */
    public Class<T> type() {
        return type;
    }
}
