package com.digisign.kms.util;

import java.io.FileInputStream;
import java.io.IOException;

public class FileUtil {

    public byte[] FiletoByte(java.io.File file)
            throws IOException
    {

        // Creating an object of FileInputStream to
        // read from a file
        FileInputStream fl = new FileInputStream(file);

        // Now creating byte array of same length as file
        byte[] arr = new byte[(int)file.length()];

        // Reading file content to byte array
        // using standard read() method
        fl.read(arr);

        // lastly closing an instance of file input stream
        // to avoid memory leakage
        fl.close();

        // Returning above byte array
        return arr;
    }
}
