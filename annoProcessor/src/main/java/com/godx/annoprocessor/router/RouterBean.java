package com.godx.annoprocessor.router;

import javax.lang.model.element.Element;

public class RouterBean {
    private String path;
    private String group;
    private Element element;
    private TypeEnum type;

    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public enum TypeEnum{
        ACTIVITY,FRAGMENT,CALL
    }

    public RouterBean(String path, String group, Element element) {
        this.path = path;
        this.group = group;
        this.element = element;
    }

    public String getPath() {
        return path;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Element getElement() {
        return element;
    }
}
