package com.example.rstaf_test_task.advice;

import com.example.rstaf_test_task.exception.FileNotFoundOnDiskException;
import com.example.rstaf_test_task.exception.UnsupportedExtensionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;

@ControllerAdvice
public class CustomControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(FileAlreadyExistsException.class)
    public ResponseEntity<String> handleMaxSizeException(FileAlreadyExistsException exc) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Файл с таким именем уже существует");
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<String> fileNotFoundException(FileNotFoundException exc) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Файл не найден по айдишнику");
    }

    @ExceptionHandler(FileNotFoundOnDiskException.class)
    public ResponseEntity<String> fileNotFoundOnDiskException(FileNotFoundOnDiskException exc) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Файл " + exc.getFileName() +
                " не найден на диске");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> maxUploadSizeExceededException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Размер файла не должен превышать 100 кб.");
    }

    @ExceptionHandler(UnsupportedExtensionException.class)
    public ResponseEntity<String> unsupportedExtensionException(UnsupportedExtensionException exc) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Расширение " + exc.getExtension() +
                " не поддерживается");
    }

}