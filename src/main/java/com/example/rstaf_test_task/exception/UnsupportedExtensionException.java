package com.example.rstaf_test_task.exception;

public class UnsupportedExtensionException extends RuntimeException {
    String extension;

    public UnsupportedExtensionException(String extension) {
        super();
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}