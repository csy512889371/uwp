package rongji.framework.base.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FieldProxy {

    private Field field;
    private Method readMethod;
    private Method wirteMethod;

    public FieldProxy() {
	super();
    }
    public FieldProxy(Field field, Method readMethod, Method wirteMethod) {
	super();
	this.field = field;
	this.readMethod = readMethod;
	this.wirteMethod = wirteMethod;
    }
    public Field getField() {
	return field;
    }
    public void setField(Field field) {
	this.field = field;
    }

    public Method getReadMethod() {
	return readMethod;
    }

    public void setReadMethod(Method readMethod) {
	this.readMethod = readMethod;
    }

    public Method getWirteMethod() {
	return wirteMethod;
    }

    public void setWirteMethod(Method wirteMethod) {
	this.wirteMethod = wirteMethod;
    }

}
