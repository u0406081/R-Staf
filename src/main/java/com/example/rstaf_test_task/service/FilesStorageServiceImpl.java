package com.example.rstaf_test_task.service;

import com.example.rstaf_test_task.exception.FileNotFoundByIdException;
import com.example.rstaf_test_task.exception.FileNotFoundOnDiskException;
import com.example.rstaf_test_task.exception.UnsupportedExtensionException;
import com.example.rstaf_test_task.response.FileInfo;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {
    public static final Path UPLOADS_FOLDER = Paths.get("uploads");
    private static final Path IDS_NAMES_FILE = UPLOADS_FOLDER.resolve("idsNames.txt");
    private static final String DELIMITER = "|";
    private static final List extensions = List.of("txt", "csv");

    @Override
    public void init() {
        try {
            Files.createDirectories(UPLOADS_FOLDER);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }

        if (!Files.exists(IDS_NAMES_FILE)) {
            try {
                Files.createFile(IDS_NAMES_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public FileInfo save(MultipartFile file) throws IOException {
        if(file.getSize() > 100_000) {
            throw new MaxUploadSizeExceededException(100);
        }
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!extensions.contains(extension)) {
            throw new UnsupportedExtensionException(extension);
        }
        Path target = UPLOADS_FOLDER.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), target);

        List<String> list = readInfoFromFile();
        Integer id;
        if (list.size() > 0) {
            String[] lastElement = list.get(list.size() - 1).split(DELIMITER);
            id = Integer.valueOf(lastElement[0]) + 1;
        } else {
            id = 1;
        }
        writeInfoToFile(id + DELIMITER + file.getOriginalFilename() + DELIMITER + file.getSize() + "\n");
        return new FileInfo(id, file.getSize(), file.getOriginalFilename());
    }

    @Override
    public Resource loadFile(String fileId) throws IOException {
        List<String> list = readInfoFromFile();
        String delimiter = Pattern.quote("|");
        String findedFileName = list.stream()
                .filter(x -> x.split(delimiter)[0].equals(fileId))
                .map(x-> x.split(delimiter)[1])
                .limit(1)
                .findFirst()
                .orElseThrow(()->new FileNotFoundByIdException());
        Resource resource = new UrlResource(Paths.get(UPLOADS_FOLDER + "/" + findedFileName).toUri());
        if (!resource.exists()) {
            throw new FileNotFoundOnDiskException(findedFileName);
        }
        return resource;
    }

    @Override
    public List<FileInfo> getAllFiles() {
        List<String> list = readInfoFromFile();
        String delimiter = Pattern.quote("|");
        return list.stream().map(x -> x.split(delimiter))
                .map(x -> new FileInfo(Integer.valueOf(x[0]), Long.valueOf(x[2]), x[1]))
                .collect(Collectors.toList());
    }

    public List<String> readInfoFromFile() {
        try {
            return Files.readAllLines(IDS_NAMES_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public void writeInfoToFile(String line) {
        try {
            Files.writeString(IDS_NAMES_FILE, line, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
