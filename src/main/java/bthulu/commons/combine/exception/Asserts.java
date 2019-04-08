package bthulu.commons.combine.exception;


import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

public abstract class Asserts {
    public static void state(boolean expression, String message) {
        state(expression, message, true);
    }

    public static void state(boolean expression, String message, boolean writableStackTrace) {
        if (!expression) {
            throw new BusinessException(message, writableStackTrace).setCode(BusinessException.CODE_ILLEGAL_ARGUMENT);
        }
    }

    public static void state(boolean expression, Supplier<String> messageSupplier) {
        state(expression, messageSupplier, true);
    }

    public static void state(boolean expression, Supplier<String> messageSupplier, boolean writableStackTrace) {
        if (!expression) {
            throw new BusinessException(nullSafeGet(messageSupplier), writableStackTrace).setCode(BusinessException.CODE_ILLEGAL_ARGUMENT);
        }
    }

    public static void isTrue(boolean expression, String message) {
        isTrue(expression, message, true);
    }

    public static void isTrue(boolean expression, String message, boolean writableStackTrace) {
        if (!expression) {
            throw new BusinessException(message, writableStackTrace).setCode(BusinessException.CODE_ILLEGAL_ARGUMENT);
        }
    }

    public static void isTrue(boolean expression, Supplier<String> messageSupplier) {
        isTrue(expression, messageSupplier, true);
    }

    public static void isTrue(boolean expression, Supplier<String> messageSupplier, boolean writableStackTrace) {
        if (!expression) {
            throw new BusinessException(nullSafeGet(messageSupplier), writableStackTrace).setCode(BusinessException.CODE_ILLEGAL_ARGUMENT);
        }
    }

    public static void isNull(@Nullable Object object, String message) {
        isNull(object, message, true);
    }

    public static void isNull(@Nullable Object object, String message, boolean writableStackTrace) {
        if (object != null) {
            throw new BusinessException(message, writableStackTrace).setCode(BusinessException.CODE_ILLEGAL_ARGUMENT);
        }
    }

    public static void isNull(@Nullable Object object, Supplier<String> messageSupplier) {
        isNull(object, messageSupplier, true);
    }

    public static void isNull(@Nullable Object object, Supplier<String> messageSupplier, boolean writableStackTrace) {
        if (object != null) {
            throw new BusinessException(nullSafeGet(messageSupplier), writableStackTrace).setCode(BusinessException.CODE_ILLEGAL_ARGUMENT);
        }
    }

    public static void notNull(@Nullable Object object, String message) {
        notNull(object, message, true);
    }

    public static void notNull(@Nullable Object object, String message, boolean writableStackTrace) {
        if (object == null) {
            throw new BusinessException(message, writableStackTrace).setCode(BusinessException.CODE_ILLEGAL_ARGUMENT);
        }
    }

    public static void notNull(@Nullable Object object, Supplier<String> messageSupplier) {
        notNull(object, messageSupplier, true);
    }

    public static void notNull(@Nullable Object object, Supplier<String> messageSupplier, boolean writableStackTrace) {
        if (object == null) {
            throw new BusinessException(nullSafeGet(messageSupplier), writableStackTrace).setCode(BusinessException.CODE_ILLEGAL_ARGUMENT);
        }
    }

    public static void hasLength(@Nullable String text, String message) {
        hasLength(text, message, true);
    }

    public static void hasLength(@Nullable String text, String message, boolean writableStackTrace) {
        if (!hasLength(text)) {
            throw new BusinessException(message, writableStackTrace).setCode(BusinessException.CODE_ILLEGAL_ARGUMENT);
        }
    }

    public static void hasLength(@Nullable String text, Supplier<String> messageSupplier) {
        hasLength(text, messageSupplier, true);
    }

    public static void hasLength(@Nullable String text, Supplier<String> messageSupplier, boolean writableStackTrace) {
        if (!hasLength(text)) {
            throw new BusinessException(nullSafeGet(messageSupplier), writableStackTrace).setCode(BusinessException.CODE_ILLEGAL_ARGUMENT);
        }
    }

    public static void hasText(@Nullable String text, String message) {
        hasText(text, message, true);
    }

    public static void hasText(@Nullable String text, String message, boolean writableStackTrace) {
        if (!hasText(text)) {
            throw new BusinessException(message, writableStackTrace).setCode(BusinessException.CODE_ILLEGAL_ARGUMENT);
        }
    }

    public static void hasText(@Nullable String text, Supplier<String> messageSupplier) {
        hasText(text, messageSupplier, true);
    }

    public static void hasText(@Nullable String text, Supplier<String> messageSupplier, boolean writableStackTrace) {
        if (!hasText(text)) {
            throw new BusinessException(nullSafeGet(messageSupplier), writableStackTrace).setCode(BusinessException.CODE_ILLEGAL_ARGUMENT);
        }
    }

    public static void doesNotContain(@Nullable String textToSearch, String substring, String message) {
        doesNotContain(textToSearch, substring, message, true);
    }

    public static void doesNotContain(@Nullable String textToSearch, String substring, String message, boolean writableStackTrace) {
        if (hasLength(textToSearch) && hasLength(substring) &&
                textToSearch.contains(substring)) {
            throw new BusinessException(message, writableStackTrace).setCode(BusinessException.CODE_ILLEGAL_ARGUMENT);
        }
    }

    public static void doesNotContain(@Nullable String textToSearch, String substring, Supplier<String> messageSupplier) {
        doesNotContain(textToSearch, substring, messageSupplier, true);
    }

    public static void doesNotContain(@Nullable String textToSearch, String substring, Supplier<String> messageSupplier, boolean writableStackTrace) {
        if (hasLength(textToSearch) && hasLength(substring) &&
                textToSearch.contains(substring)) {
            throw new BusinessException(nullSafeGet(messageSupplier), writableStackTrace).setCode(BusinessException.CODE_ILLEGAL_ARGUMENT);
        }
    }

    public static void notEmpty(@Nullable Object[] array, String message) {
        notEmpty(array, message, true);
    }

    public static void notEmpty(@Nullable Object[] array, String message, boolean writableStackTrace) {
        if (array == null || array.length == 0) {
            throw new BusinessException(message, writableStackTrace).setCode(BusinessException.CODE_ILLEGAL_ARGUMENT);
        }
    }

    public static void notEmpty(@Nullable Object[] array, Supplier<String> messageSupplier) {
        notEmpty(array, messageSupplier, true);
    }

    public static void notEmpty(@Nullable Object[] array, Supplier<String> messageSupplier, boolean writableStackTrace) {
        if (array == null || array.length == 0) {
            throw new BusinessException(nullSafeGet(messageSupplier), writableStackTrace).setCode(BusinessException.CODE_ILLEGAL_ARGUMENT);
        }
    }

    public static void noNullElements(@Nullable Object[] array, String message) {
        noNullElements(array, message, true);
    }

    public static void noNullElements(@Nullable Object[] array, String message, boolean writableStackTrace) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new BusinessException(message, writableStackTrace).setCode(BusinessException.CODE_ILLEGAL_ARGUMENT);
                }
            }
        }
    }

    public static void noNullElements(@Nullable Object[] array, Supplier<String> messageSupplier) {
        noNullElements(array, messageSupplier, true);
    }

    public static void noNullElements(@Nullable Object[] array, Supplier<String> messageSupplier, boolean writableStackTrace) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new BusinessException(nullSafeGet(messageSupplier), writableStackTrace).setCode(BusinessException.CODE_ILLEGAL_ARGUMENT);
                }
            }
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection, String message) {
        notEmpty(collection, message, true);
    }

    public static void notEmpty(@Nullable Collection<?> collection, String message, boolean writableStackTrace) {
        if (collection == null || collection.isEmpty()) {
            throw new BusinessException(message, writableStackTrace).setCode(BusinessException.CODE_ILLEGAL_ARGUMENT);
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection, Supplier<String> messageSupplier) {
        notEmpty(collection, messageSupplier, true);
    }

    public static void notEmpty(@Nullable Collection<?> collection, Supplier<String> messageSupplier, boolean writableStackTrace) {
        if (collection == null || collection.isEmpty()) {
            throw new BusinessException(nullSafeGet(messageSupplier), writableStackTrace).setCode(BusinessException.CODE_ILLEGAL_ARGUMENT);
        }
    }

    public static void notEmpty(@Nullable Map<?, ?> map, String message) {
        notEmpty(map, message, true);
    }

    public static void notEmpty(@Nullable Map<?, ?> map, String message, boolean writableStackTrace) {
        if (map == null || map.isEmpty()) {
            throw new BusinessException(message, writableStackTrace).setCode(BusinessException.CODE_ILLEGAL_ARGUMENT);
        }
    }

    public static void notEmpty(@Nullable Map<?, ?> map, Supplier<String> messageSupplier) {
        notEmpty(map, messageSupplier, true);
    }

    public static void notEmpty(@Nullable Map<?, ?> map, Supplier<String> messageSupplier, boolean writableStackTrace) {
        if (map == null || map.isEmpty()) {
            throw new BusinessException(nullSafeGet(messageSupplier), writableStackTrace).setCode(BusinessException.CODE_ILLEGAL_ARGUMENT);
        }
    }

    public static void isInstanceOf(Class<?> type, @Nullable Object obj, String message) {
        isInstanceOf(type, obj, message, true);
    }

    public static void isInstanceOf(Class<?> type, @Nullable Object obj, String message, boolean writableStackTrace) {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            instanceCheckFailed(type, obj, message, writableStackTrace);
        }
    }

    public static void isInstanceOf(Class<?> type, @Nullable Object obj, Supplier<String> messageSupplier) {
        isInstanceOf(type, obj, messageSupplier, true);
    }

    public static void isInstanceOf(Class<?> type, @Nullable Object obj, Supplier<String> messageSupplier, boolean writableStackTrace) {
        notNull(type, "Type to check against must not be null", writableStackTrace);
        if (!type.isInstance(obj)) {
            instanceCheckFailed(type, obj, nullSafeGet(messageSupplier), writableStackTrace);
        }
    }

    public static void isInstanceOf(Class<?> type, @Nullable Object obj) {
        isInstanceOf(type, obj, true);
    }

    public static void isInstanceOf(Class<?> type, @Nullable Object obj, boolean writableStackTrace) {
        isInstanceOf(type, obj, "", writableStackTrace);
    }

    public static void isAssignable(Class<?> superType, @Nullable Class<?> subType, String message) {
        isAssignable(superType, subType, message, true);
    }

    public static void isAssignable(Class<?> superType, @Nullable Class<?> subType, String message, boolean writableStackTrace) {
        notNull(superType, "Super type to check against must not be null", writableStackTrace);
        if (subType == null || !superType.isAssignableFrom(subType)) {
            assignableCheckFailed(superType, subType, message, writableStackTrace);
        }
    }

    public static void isAssignable(Class<?> superType, @Nullable Class<?> subType, Supplier<String> messageSupplier) {
        isAssignable(superType, subType, messageSupplier, true);
    }

    public static void isAssignable(Class<?> superType, @Nullable Class<?> subType, Supplier<String> messageSupplier, boolean writableStackTrace) {
        notNull(superType, "Super type to check against must not be null", writableStackTrace);
        if (subType == null || !superType.isAssignableFrom(subType)) {
            assignableCheckFailed(superType, subType, nullSafeGet(messageSupplier), writableStackTrace);
        }
    }

    public static void isAssignable(Class<?> superType, Class<?> subType) {
        isAssignable(superType, subType, true);
    }

    public static void isAssignable(Class<?> superType, Class<?> subType, boolean writableStackTrace) {
        isAssignable(superType, subType, "", writableStackTrace);
    }

    private static void instanceCheckFailed(Class<?> type, @Nullable Object obj, @Nullable String msg, boolean writableStackTrace) {
        String className = (obj != null ? obj.getClass().getName() : "null");
        String result = "";
        boolean defaultMessage = true;
        if (hasLength(msg)) {
            if (endsWithSeparator(msg)) {
                result = msg + " ";
            } else {
                result = messageWithTypeName(msg, className);
                defaultMessage = false;
            }
        }
        if (defaultMessage) {
            result = result + ("Object of class [" + className + "] must be an instance of " + type);
        }
        throw new BusinessException(result, writableStackTrace).setCode(BusinessException.CODE_ILLEGAL_ARGUMENT);
    }

    private static void assignableCheckFailed(Class<?> superType, @Nullable Class<?> subType, @Nullable String msg, boolean writableStackTrace) {
        String result = "";
        boolean defaultMessage = true;
        if (hasLength(msg)) {
            if (endsWithSeparator(msg)) {
                result = msg + " ";
            } else {
                result = messageWithTypeName(msg, subType);
                defaultMessage = false;
            }
        }
        if (defaultMessage) {
            result = result + (subType + " is not assignable to " + superType);
        }
        throw new BusinessException(result, writableStackTrace).setCode(BusinessException.CODE_ILLEGAL_ARGUMENT);
    }

    private static boolean endsWithSeparator(String msg) {
        return (msg.endsWith(":") || msg.endsWith(";") || msg.endsWith(",") || msg.endsWith("."));
    }

    private static String messageWithTypeName(String msg, @Nullable Object typeName) {
        return msg + (msg.endsWith(" ") ? "" : ": ") + typeName;
    }

    @Nullable
    private static String nullSafeGet(@Nullable Supplier<String> messageSupplier) {
        return (messageSupplier != null ? messageSupplier.get() : null);
    }

    private static boolean hasLength(@Nullable String text) {
        return (text != null && text.length() > 0);
    }

    private static boolean hasText(@Nullable String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
