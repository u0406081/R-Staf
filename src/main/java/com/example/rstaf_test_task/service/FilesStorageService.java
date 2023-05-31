package com.example.rstaf_test_task.service;

import com.example.rstaf_test_task.response.FileInfo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FilesStorageService {
    void init();
    FileInfo save(MultipartFile file) throws IOException;
    Resource loadFile(String fileId) throws IOException;
    List<FileInfo> getAllFiles();
}
