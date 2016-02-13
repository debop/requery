/*
 * Copyright 2016 requery.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.requery.meta;

import io.requery.util.ClassMap;
import io.requery.util.Objects;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

final class ImmutableEntityModel implements EntityModel {

    private final String name;
    private final Map<Class<?>, Type<?>> map;

    ImmutableEntityModel(String name, Set<Type<?>> types) {
        this.name = name;
        ClassMap<Type<?>> map = new ClassMap<>();
        for (Type<?> type : types) {
            map.put(type.classType(), type);
            map.put(type.baseType(), type);
        }
        this.map = Collections.unmodifiableMap(map);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public <T> Type<T> typeOf(Class<? extends T> entityClass) {
        @SuppressWarnings("unchecked")
        Type<T> type = (Type) map.get(entityClass);
        if (type == null) {
            throw new NotMappedException();
        }
        return type;
    }

    @Override
    public Set<Type<?>> allTypes() {
        return new LinkedHashSet<>(map.values());
    }

    @Override
    public String toString() {
        return name + " : " + map.keySet().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EntityModel) {
            EntityModel other = (EntityModel) obj;
            return Objects.equals(name, other.name()) && allTypes().equals(other.allTypes());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, map);
    }
}
