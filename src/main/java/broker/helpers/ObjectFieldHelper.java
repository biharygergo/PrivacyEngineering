package broker.helpers;

import java.lang.reflect.Field;

public class ObjectFieldHelper {
    public static void setProperty(Object object, String fieldName, String value) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        if (field.getType() == String.class) {
            field.set(object, value);
            return;
        }
        if (field.getType() == Character.TYPE) {
            field.set(object, value.charAt(0));
            return;
        }
        if (field.getType() == Short.TYPE) {
            field.set(object, value.length() == 0 ? 0 : Short.parseShort(value));
            return;
        }
        if (field.getType() == Integer.TYPE) {
            field.set(object, value.length() == 0 ? 0 : Integer.parseInt(value));
            return;
        }
        if (field.getType() == Long.TYPE) {
            field.set(object, value.length() == 0 ? 0L : Long.parseLong(value));
            return;
        }
        if (field.getType() == Float.TYPE) {
            field.set(object, value.length() == 0 ? 0 : Float.parseFloat(value));
            return;
        }
        if (field.getType() == Double.TYPE) {
            field.set(object, value.length() == 0 ? 0 : Double.parseDouble(value));
            return;
        }
        if (field.getType() == Byte.TYPE) {
            field.set(object, Byte.parseByte(value));
            return;
        }
        if (field.getType() == Boolean.TYPE) {
            field.set(object, Boolean.parseBoolean(value));
            return;
        }
        field.set(object, value);
    }

    public static String getProperty(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (String) field.get(object);
    }

}
