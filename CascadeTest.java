package com.zf.qqcy.dataService.common.excel;

import org.junit.Assert;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Taylor on 2016/2/26.
 */
public class CascadeTest {

    @Test
    public void test() throws Exception {
        String idField = "b.c.value";
        String value = "v";
        A a = new A();
        setValue(a, idField, value);
        Assert.assertEquals(a.getB().getC().getValue(), value);
    }

    private void setValue(Object obj, String fieldName, String value) throws IntrospectionException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InstantiationException {
        if (!fieldName.contains(".")) {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, obj.getClass());
            Method writeMethod = propertyDescriptor.getWriteMethod();
            writeMethod.invoke(obj, value);
        } else {
            setValue(setCascadeObj(obj, fieldName.split("\\.")[0]), fieldName.substring(fieldName.indexOf(".") + 1), value);
        }
    }

    private Object setCascadeObj(Object obj, String fieldName) throws IntrospectionException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException {
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, obj.getClass());
        Method readMethod = propertyDescriptor.getReadMethod();
        Object cascadeObj = readMethod.invoke(obj);
        if (cascadeObj == null) {
            cascadeObj = propertyDescriptor.getPropertyType().newInstance();
            Method writeMethod = propertyDescriptor.getWriteMethod();
            writeMethod.invoke(obj, cascadeObj);
        }
        return cascadeObj;
    }
}
