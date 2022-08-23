package com.appgodx.router;

public class RouterBean {
    private String path;
    private String group;
    private Class<?> clazz;
    private TypeEnum type;



    public enum TypeEnum{
        ACTIVITY,FRAGMENT,CALL
    }

    public RouterBean() {
    }
    public RouterBean(String path, String group, Class<?> clazz) {
        this.path = path;
        this.group = group;
        this.clazz = clazz;
        type = TypeEnum.ACTIVITY;
    }
    public RouterBean(String path, String group, Class<?> clazz,TypeEnum type) {
        this.path = path;
        this.group = group;
        this.clazz = clazz;
        this.type = type;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }
}
