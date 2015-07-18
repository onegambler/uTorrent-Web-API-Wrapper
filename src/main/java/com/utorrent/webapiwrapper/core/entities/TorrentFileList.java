package com.utorrent.webapiwrapper.core.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
public class TorrentFileList {

    @Getter
    @Setter
    private String hash;
    private List<File> files;

    public TorrentFileList() {
        files = new ArrayList<>();
    }

    public void addFile(String name, long size, long downloaded, Priority priority, boolean streamable, Duration streamDuration, long startingPart, long numberOfParts, long speed, int videoWidth, int videoHeight) {
        files.add(new File(name, size, downloaded, priority, startingPart, numberOfParts, streamable, streamDuration, speed, videoWidth, videoHeight));
    }

    public List<File> getFiles() {
        return files;
    }

    public void addFile(File file) {
        files.add(file);
    }

    @Getter
    @ToString
    @Builder
    public static class File {
        private final String name;
        private final long size;
        private final long downloaded;
        private final Priority priority;
        private final long startingPart;
        private final long numberOfParts;
        private final boolean streamable;
        private final Duration streamDuration;
        private final long videoSpeed;
        private final long videoWidth;
        private final long videoHeight;
    }
}
