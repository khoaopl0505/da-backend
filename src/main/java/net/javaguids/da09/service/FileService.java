package net.javaguids.da09.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public interface FileService {
    void saveFile(MultipartFile fileToSave) throws IOException;
    File getDownloadFile(String fileName) throws FileNotFoundException;
}
