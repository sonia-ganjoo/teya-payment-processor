package com.payments.teya.util;

import java.io.File;
import java.net.URL;

public class FileReader {
    public File getResourceFile(String fileName)
    {
        URL url = this.getClass()
                .getClassLoader()
                .getResource(fileName);

        if(url == null) {
            throw new IllegalArgumentException(fileName + " is not found 1");
        }

        return new File(url.getFile());
    }
}
