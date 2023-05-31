package com.example.rstaf_test_task.controller;

import com.example.rstaf_test_task.response.FileInfo;
import com.example.rstaf_test_task.service.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FilesController {
    FilesStorageService filesStorageService;

    @Autowired
    public FilesController(FilesStorageService filesStorageService) {
        this.filesStorageService = filesStorageService;
    }

    @PostMapping(path = "/upload", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FileInfo> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(filesStorageService.save(file));
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws IOException {
        Resource resource = filesStorageService.loadFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> getFiles() {
        return ResponseEntity.status(HttpStatus.OK).body(filesStorageService.getAllFiles());
    }
}
