package com.eaccid.hocreader.provider.file.findner;

import java.io.File;
import java.util.List;

public interface FileProvider {
    List<File> findFiles();

    List<File> findFiles(File directory);
}
