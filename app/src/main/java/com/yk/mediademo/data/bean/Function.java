package com.yk.mediademo.data.bean;

public class Function {
    private String name;
    private Class<?> cls;

    public Function(String name, Class<?> cls) {
        this.name = name;
        this.cls = cls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getCls() {
        return cls;
    }

    public void setCls(Class<?> cls) {
        this.cls = cls;
    }

    @Override
    public String toString() {
        return "Function{" +
                "name='" + name + '\'' +
                ", cls=" + cls +
                '}';
    }
}
