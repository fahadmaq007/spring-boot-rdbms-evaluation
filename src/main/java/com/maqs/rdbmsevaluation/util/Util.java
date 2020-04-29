package com.maqs.rdbmsevaluation.util;

public class Util {
    private Util() {}

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String TIME_FORMAT = "HH:mm:ss";

    public static final String DATETIME_FORMAT = DATE_FORMAT + " " + TIME_FORMAT;

    public static final Integer DEFAULT_PAGE_INDEX = 1;

    public static final Integer DEFAULT_PAGE_SIZE = 50;

//    public static Pageable getPageRequest(String sort, Integer pageIndex, Integer pageSize) {
//        if (pageIndex == null) {
//            pageIndex = DEFAULT_PAGE_INDEX;
//        }
//        if (pageIndex > 0) {
//            pageIndex--;
//        }
//        if (pageSize == null) {
//            pageSize = DEFAULT_PAGE_SIZE;
//        }
//
//        Sort sortBy = null;
//        if (sort != null) {
//            Sort.Direction d = Sort.Direction.ASC;
//            String param = sort;
//            if (sort.startsWith("-")) {
//                d = Sort.Direction.DESC;
//                param = sort.substring(1);
//            }
//            sortBy = Sort.by(d, param);
//
//            return PageRequest.of(pageIndex, pageSize, sortBy);
//        }
//        return PageRequest.of(pageIndex, pageSize);
//    }
//
//
//    /**
//     * Some of the values
//     * '2019-01-01,2019-01-31'
//     * 'Millis1,Millis2'
//     * '2019-01-01'
//     * 'Millis1'
//     * Millis1
//     *
//     * @param value
//     * @return
//     */
//    public static Object parseDate(Object value) {
//        if (value instanceof String) {
//            String date = (String) value;
//            return getDateFromString(date);
//        } else if (value instanceof Long) {
//            Long l = (Long) value;
//            return new Date(l);
//        } else if (value instanceof Date) {
//            return (Date) value;
//        }
//        return value;
//    }
//
//    private static Date[] getCommaSeparatedDates(String string) {
//        String[] strings = string.split(",");
//        Date[] values = new Date[strings.length];
//        int i = 0;
//        for (String s : strings) {
//            values[i] = (Date) getDateFromString(s);
//        }
//        return values;
//    }
//
//    private static Object getDateFromString(String s) {
//        if (s.contains(",")) {
//            return getCommaSeparatedDates(s);
//        }
//        if (StringUtils.isNumeric(s)) {
//            return parseDate(Long.valueOf(s));
//        } else {
//            try {
//                return DateUtils.parseDate(s, new String[] { DATE_FORMAT, DATETIME_FORMAT, TIME_FORMAT });
//            } catch (ParseException e) {
//                throw new IllegalArgumentException(e.getMessage(), e);
//            }
//        }
//    }
//
//    private static Number[] getCommaSeparatedNumbers(String string, Class<?> cls) {
//        String[] strings = string.split(",");
//        Number[] values = new Number[strings.length];
//        int i = 0;
//        for (String s : strings) {
//            values[i++] = (Number) parseNumber(s, cls);
//        }
//        return values;
//    }
//
//    public static Object getValue(Object value, Class<?> cls) {
//        if (Date.class == cls) {
//            return parseDate(value);
//        } else if (Number.class.isAssignableFrom(cls)){
//            return parseNumber(value, cls);
//        }
//        return null;
//    }
//
//    private static Object parseNumber(Object value, Class<?> cls) {
//        Class<? extends Number> nCls = (Class<? extends Number>) cls;
//        if (value instanceof Number) {
//            return NumberUtils.convertNumberToTargetClass((Number) value, nCls);
//        } else if (value instanceof String) {
//            String number = (String) value;
//            if (number.contains(",")) {
//                return getCommaSeparatedNumbers(number, cls);
//            }
//            if (StringUtils.isNumeric((String) value)) {
//                return org.apache.commons.lang.math.NumberUtils.createNumber((String) value);
//            }
//        }
//        return null;
//    }
//
//    /**
//     * Compare the order of list is by given field in both lists.
//     *
//     * @param field Field to look for
//     * @param l1 First list
//     * @param l2 Second list
//     * @param <T> Any entity that has a declared 'field' param.
//     * @return true if all the elements match the both list otherwise false.
//     */
//    public static <T> boolean compare(String field, List<T> l1, List<T> l2) {
//        if (l1.size() != l2.size()) {
//            return false;
//        }
//        try {
//            for (int i = 0; i < l1.size(); i++) {
//                T t1 = l1.get(i);
//                T t2 = l2.get(i);
//
//                Field f1 = t1.getClass().getDeclaredField(field);
//                f1.setAccessible(true);
//                Field f2 = t2.getClass().getDeclaredField(field);
//                f2.setAccessible(true);
//                Object o1 = f1.get(t1);
//                Object o2 = f2.get(t2);
//                if (! o1.equals(o2)) {
//                    return false;
//                }
//            }
//        } catch (Exception e) {
//            throw new IllegalArgumentException(e.getMessage(), e);
//        }
//
//        return true;
//    }
//
//    /**
//     * Traverses the list to see the given order is maintained.
//     *
//     * @param field Field to look for
//     * @param list List of entities
//     * @param asc true if expected to be in ascending order, otherwise false.
//     * @param <T> Any entity that has a declared 'field' param.
//     *
//     * @return true if is in given order, otherwise false.
//     */
//    public static <T> boolean isInOrder(String field, List<T> list, boolean asc) {
//        if (list == null || list.isEmpty()) {
//            return true;
//        }
//        try {
//            Comparable prev = null;
//            for (int i = 0; i < list.size(); i++) {
//                T t1 = list.get(i);
//
//                Field f1 = t1.getClass().getDeclaredField(field);
//                f1.setAccessible(true);
//                Comparable current = (Comparable) f1.get(t1);
//
//                if (asc) {
//                    if (prev != null && prev.compareTo(current) > 0) {
//                        return false;
//                    }
//                } else {
//                    if (prev != null && prev.compareTo(current) < 0) {
//                        return false;
//                    }
//                }
//                prev = current;
//            }
//        } catch (Exception e) {
//            throw new IllegalArgumentException(e.getMessage(), e);
//        }
//
//        return true;
//    }
//
//    /**
//     * Traverses the list to see whether the given field has only given value in it.
//     * For eg. status field having value 10: If the status has other than 10, it returns false.
//     *
//     * @param field Field to look for
//     * @param value Value of the field
//     * @param list List of entities
//     * @param <T> Any entity that has a declared 'field' param.
//     *
//     * @return true if the list has only the given criteria, otherwise false.
//     */
//    public static <T> boolean hasOnlyGivenCriteria(String field, Object value, List<T> list) {
//        if (list == null || list.isEmpty()) {
//            return true;
//        }
//        try {
//            for (int i = 0; i < list.size(); i++) {
//                T t1 = list.get(i);
//
//                Field f1 = t1.getClass().getDeclaredField(field);
//                f1.setAccessible(true);
//                Object current = f1.get(t1);
//                if (current != null && ! current.equals(value)) {
//                    return false;
//                }
//            }
//        } catch (Exception e) {
//            throw new IllegalArgumentException(e.getMessage(), e);
//        }
//
//        return true;
//    }
//
//    /**
//     * Generates the json string.
//     *
//     * @param src
//     *            source object to be converted.
//     * @return json string
//     */
//    public static String toJson(Object src) {
//        Gson gson = new Gson();
//        return gson.toJson(src);
//    }
//
//    /**
//     * Generates the entity of a given class from json.
//     *
//     * @param json
//     *            json string
//     * @param clazz
//     *            entity's class to be converted to.
//     * @return entity
//     */
//    public static <T> T fromJson(String json, Class<T> clazz) {
//        Gson gson = new Gson();
//        return gson.fromJson(json, clazz);
//    }
//
//    public static String encode(String url) {
//        try {
//            String encodeURL = URLEncoder.encode( url, "UTF-8" );
//            return encodeURL;
//        } catch (UnsupportedEncodingException e) {
//            return "Issue while encoding" +e.getMessage();
//        }
//    }
//
//    public static boolean isFieldDefined(Class<?> clazz, String field) {
//        try {
//            Field f = clazz.getDeclaredField(field);
//            return f != null;
//        } catch (NoSuchFieldException e) {
//            return false;
//        }
//    }
//
//    public static Pageable getDefaultPage() {
//        return PageRequest.of(DEFAULT_PAGE_INDEX, DEFAULT_PAGE_SIZE);
//    }
}
