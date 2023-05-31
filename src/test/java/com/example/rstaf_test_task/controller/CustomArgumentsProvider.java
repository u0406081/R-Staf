package com.example.rstaf_test_task.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.mock.web.MockMultipartFile;
import java.util.Arrays;
import java.util.stream.Stream;

public class CustomArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        String testName1 = "Upload txt file";
        String testName2 = "Upload csv file";
        String testName3 = "Upload csv file again - exception";
        String testName4 = "Upload jpg file - exception";
        String testName5 = "Upload txt file more 100 kb  - exception";

        String name = "file";
        String fileName1 = "sampleFile.txt";
        String fileName2 = "sampleFile.csv";
        String fileName4 = "sampleFile.jpg";
        String fileName5 = "bigFile.txt";

        String contentType = "text/plain";
        String content = "This is the file content";

        char[] chars = new char[101_000];
        Arrays.fill(chars, 'a');
        String content5 = new String(chars);

        MockMultipartFile sampleFile1 = new MockMultipartFile(name, fileName1, contentType, content.getBytes());
        MockMultipartFile sampleFile2 = new MockMultipartFile(name, fileName2, contentType, content.getBytes());
        MockMultipartFile sampleFile4 = new MockMultipartFile(name, fileName4, contentType, content.getBytes());
        MockMultipartFile sampleFile5 = new MockMultipartFile(name, fileName5, contentType, content5.getBytes());

        String string1 = "\"name\":\"" + fileName1 + "\"}";
        String string2 = "\"name\":\"" + fileName2 + "\"}";
        String string3 = "Файл с таким именем уже существует";
        String string4 = "Расширение jpg не поддерживается";
        String string5 = "Размер файла не должен превышать 100 кб.";

        return Stream.of(
                Arguments.of(testName1, sampleFile1, status().isOk(), string1),
                Arguments.of(testName2, sampleFile2, status().isOk(), string2),
                Arguments.of(testName3, sampleFile2, status().is4xxClientError(), string3),
                Arguments.of(testName4, sampleFile4, status().is4xxClientError(), string4),
                Arguments.of(testName5, sampleFile5, status().is4xxClientError(), string5)
        );
    }
}
