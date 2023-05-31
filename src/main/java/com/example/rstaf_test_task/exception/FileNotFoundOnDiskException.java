package com.example.rstaf_test_task.exception;

public class FileNotFoundOnDiskException extends RuntimeException {
    private String fileName;

    public FileNotFoundOnDiskException(String fileName) {
        super();
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}