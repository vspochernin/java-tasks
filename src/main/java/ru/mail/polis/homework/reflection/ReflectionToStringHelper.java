package ru.mail.polis.homework.reflection;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Необходимо реализовать метод reflectiveToString, который для произвольного объекта
 * возвращает его строковое описание в формате:
 * <p>
 * {field_1: value_1, field_2: value_2, ..., field_n: value_n}
 * <p>
 * где field_i - имя поля
 * value_i - его строковое представление (String.valueOf),
 * за исключением массивов, для которых value формируется как:
 * [element_1, element_2, ..., element_m]
 * где element_i - строковое представление элемента (String.valueOf)
 * элементы должны идти в том же порядке, что и в массиве.
 * <p>
 * Все null'ы следует представлять строкой "null".
 * <p>
 * Порядок полей
 * Сначала следует перечислить в алфавитном порядке поля, объявленные непосредственно в классе объекта,
 * потом в алфавитном порядке поля объявленные в родительском классе и так далее по иерархии наследования.
 * Примеры можно посмотреть в тестах.
 * <p>
 * Какие поля выводить
 * Необходимо включать только нестатические поля. Также нужно пропускать поля, помеченные аннотацией @SkipField
 * <p>
 * Упрощения
 * Чтобы не усложнять задание, предполагаем, что нет циклических ссылок, inner классов, и transient полей
 * <p>
 * Реализация
 * В пакете ru.mail.polis.homework.reflection можно редактировать только этот файл
 * или добавлять новые (не рекомендуется, т.к. решение вполне умещается тут в несколько методов).
 * Редактировать остальные файлы нельзя.
 * <p>
 * Баллы
 * В задании 3 уровня сложности, для каждого свой набор тестов:
 * Easy - простой класс, нет наследования, массивов, статических полей, аннотации SkipField (4 балла)
 * Easy + Medium - нет наследования, массивов, но есть статические поля и поля с аннотацией SkipField (6 баллов)
 * Easy + Medium + Hard - нужно реализовать все требования задания (10 баллов)
 * <p>
 * Итого, по заданию можно набрать 10 баллов
 * Баллы могут снижаться за неэффективный или неаккуратный код
 */
public class ReflectionToStringHelper {

    private static final String FIELD_SEPARATOR = ", ";
    private static final String KEY_VALUE_SEPARATOR = ": ";
    private static final String NULL = "null";
    private static final String OPENING = "{";
    private static final String ENDING = "}";
    private static final String ARRAY_OPENING = "[";
    private static final String ARRAY_ENDING = "]";

    public static String reflectiveToString(Object object) {
        if (object == null) {
            return NULL;
        }

        Class<?> clazz = object.getClass();
        StringBuilder sb = new StringBuilder().append(OPENING);
        boolean isSomethingRecorded = false;

        while (clazz != Object.class) {
            List<Field> addedFields = Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                    .filter(field -> !field.isAnnotationPresent(SkipField.class))
                    .sorted(Comparator.comparing(Field::getName))
                    .collect(Collectors.toList());

            for (Field field : addedFields) {
                try {
                    field.setAccessible(true);
                    sb.append(field.getName()).append(KEY_VALUE_SEPARATOR);
                    if (field.getType().isArray()) {
                        fillStringBuilderFromArray(sb, field, object);
                    } else {
                        sb.append(field.get(object));
                    }
                    sb.append(FIELD_SEPARATOR);
                    isSomethingRecorded = true;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            clazz = clazz.getSuperclass();
        }

        if (isSomethingRecorded) {
            removeExtraComma(sb);
        }
        return sb.append(ENDING).toString();
    }

    private static void fillStringBuilderFromArray(StringBuilder sb, Field field, Object object) throws IllegalAccessException {
        Object array = field.get(object);
        if (array == null) {
            sb.append(NULL);
            return;
        }
        sb.append(ARRAY_OPENING);
        int length = Array.getLength(array);
        if (length == 0) {
            sb.append(ARRAY_ENDING);
            return;
        }
        for (int i = 0; i < length; i++) {
            sb.append(Array.get(array, i)).append(FIELD_SEPARATOR);
        }
        removeExtraComma(sb);
        sb.append(ARRAY_ENDING);
    }

    private static void removeExtraComma(StringBuilder sb) {
        sb.setLength(sb.length() - FIELD_SEPARATOR.length());
    }
}
