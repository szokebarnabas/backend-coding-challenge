package com.bs.expense.infrastructure.driven;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class TestHelper {

    public static String readFromClassPath(final String fileName) throws Exception {
        return Files
                .lines(Paths.get(ClassLoader.getSystemResource(fileName).toURI()))
                .collect(Collectors.joining());
    }
}
