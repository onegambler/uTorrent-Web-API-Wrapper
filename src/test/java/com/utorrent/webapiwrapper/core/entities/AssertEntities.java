package com.utorrent.webapiwrapper.core.entities;

import static org.assertj.core.api.Assertions.assertThat;

public class AssertEntities {

    private AssertEntities(){}

    public static void assertEquals(TorrentFileList.File file, String name, long size, long downloaded, Priority priority, long startingPart, long numberOfParts) {
        assertThat(file.getName()).isEqualTo(name);
        assertThat(file.getSize()).isEqualTo(size);
        assertThat(file.getDownloaded()).isEqualTo(downloaded);
        assertThat(file.getPriority()).isEqualTo(priority);
        assertThat(file.getNumberOfParts()).isEqualTo(numberOfParts);
        assertThat(file.getStartingPart()).isEqualTo(startingPart);
    }
}
