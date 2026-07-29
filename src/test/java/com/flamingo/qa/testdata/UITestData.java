package com.flamingo.qa.testdata;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final class UITestData {

    private UITestData() {
    }

    public static final String BASE_URL = "https://demoqa.com/webtables";

    private static final String[] DEPARTMENTS = {"QA", "Engineering", "Sales", "Marketing", "HR"};
    private static final AtomicInteger COUNTER = new AtomicInteger();

    public static Map<String, String> randomRecord() {
        int id = COUNTER.incrementAndGet();

        Map<String, String> record = new LinkedHashMap<>();
        record.put("firstName", "Test" + id);
        record.put("lastName", "User" + id);
        record.put("age", "25");
        record.put("email", "test.user" + id + "@example.com");
        record.put("salary", "50000");
        record.put("department", DEPARTMENTS[id % DEPARTMENTS.length]);
        return record;
    }
}
