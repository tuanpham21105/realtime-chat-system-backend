package com.owl.user_service.persistence.jpa.entity;

import java.lang.reflect.Field;

public abstract class EntityBase<T> {

    @SuppressWarnings("unchecked")
    public void copyFrom(T source) {
        if (source == null) {
            throw new IllegalArgumentException("Source must not be null");
        }

        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = this.getClass();

        while (sourceClass != null && targetClass != null) {

            Field[] sourceFields = sourceClass.getDeclaredFields();

            for (Field sourceField : sourceFields) {
                try {
                    // Skip static or final fields
                    if (java.lang.reflect.Modifier.isStatic(sourceField.getModifiers())
                        || java.lang.reflect.Modifier.isFinal(sourceField.getModifiers())) {
                        continue;
                    }

                    sourceField.setAccessible(true);
                    Object value = sourceField.get(source);

                    // Skip null values (VERY IMPORTANT for updates)
                    if (value == null) {
                        continue;
                    }

                    Field targetField = findField(targetClass, sourceField.getName());
                    if (targetField == null) {
                        continue;
                    }

                    targetField.setAccessible(true);
                    targetField.set(this, value);

                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to copy field: " + sourceField.getName(), e);
                }
            }

            sourceClass = sourceClass.getSuperclass();
            targetClass = targetClass.getSuperclass();
        }
    }

    private Field findField(Class<?> clazz, String name) {
        while (clazz != null) {
            try {
                return clazz.getDeclaredField(name);
            } catch (NoSuchFieldException ignored) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }
}

