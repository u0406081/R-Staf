package com.example.rstaf_test_task.response;

public class FileInfo {
    Integer id;
    Long size;
    String name;

    public FileInfo(Integer id, Long size, String name) {
        this.id = id;
        this.size = size;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
