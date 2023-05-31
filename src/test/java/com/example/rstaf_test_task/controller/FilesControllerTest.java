package com.example.rstaf_test_task.controller;

import com.example.rstaf_test_task.RStafTestTaskApplication;
import com.example.rstaf_test_task.service.FilesStorageServiceImpl;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.io.File;
import java.io.IOException;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//https://stackoverflow.com/questions/74946784/java-lang-classnotfoundexception-jakarta-servlet-http-httpsessioncontext-with-s

@AutoConfigureMockMvc
@SpringBootTest(classes = RStafTestTaskApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(SpringExtension.class)
class FilesControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(CustomArgumentsProvider.class)
    void uploadFile(String testName, MockMultipartFile file, ResultMatcher status, String string)
            throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload").file(file))
                .andExpect(status)
                .andExpect(content().string(containsString(string)));
    }

    @Test
    void downloadFile() throws Exception {
        MvcResult result = mockMvc.perform(get("/download/1")).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(24, result.getResponse().getContentAsByteArray().length);
        assertEquals("application/octet-stream", result.getResponse().getContentType());
    }

    @Test
    void getFiles() throws Exception {
        mockMvc.perform(get("/files"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @AfterAll
    public static void after() throws IOException {
        File directory = new File(FilesStorageServiceImpl.UPLOADS_FOLDER.toString());
        FileUtils.cleanDirectory(directory);
    }
}