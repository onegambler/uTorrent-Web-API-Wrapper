package com.utorrent.webapiwrapper.core.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class TorrentFileList {

    @Getter
    @Setter
    private String hash;
    private List<File> files;

    public TorrentFileList() {
        files = new ArrayList<>();
    }

    public void addFile(String name, int size, int downloaded, Priority priority) {
        files.add(new File(name, size, downloaded, priority));
    }

    public List<File> getFiles() {
        return files;
    }

    public void addFile(File file) {
        files.add(file);
    }

    @Getter
    @Builder
    public static class File {
        private final String name;
        private final int size;
        private final int downloaded;
        private final Priority priority;
    }
}
