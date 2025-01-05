package net.javaguids.da09.service.imp;

import net.javaguids.da09.model.File;
import net.javaguids.da09.repository.FileRepository;
import net.javaguids.da09.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Objects;
@Service
public class FileServiceImp implements FileService {

    private static final String STORAGE_DIRECTORY = "E:\\storage";

    @Autowired
    private FileRepository fileRepository;


    @Override
    public void saveFile(MultipartFile fileToSave) throws IOException {
        if(fileToSave == null){
            throw new NullPointerException("file save is null");
        }

        var targetFile = new java.io.File(STORAGE_DIRECTORY + java.io.File.separator + fileToSave.getOriginalFilename());
        if(!Objects.equals(targetFile.getParent(), STORAGE_DIRECTORY)){
            throw new SecurityException("Unsupported filename!");
        }

        // Save physical file
        Files.copy(fileToSave.getInputStream(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        // Save file metadata to database
        File fileMetadata = new File();
        fileMetadata.setFileName(fileToSave.getOriginalFilename());
        fileMetadata.setUploadAt(LocalDateTime.now());
//        fileMetadata.setIdUserUpload(userId);
        fileRepository.save(fileMetadata);
    }

    @Override
    public java.io.File getDownloadFile(String fileName) throws FileNotFoundException {
        if(fileName == null){
            throw new NullPointerException("file name is null");
        }

        // Check if file exists in database
        File fileMetadata = fileRepository.findByFileName(fileName);
        if(fileMetadata == null) {
            throw new FileNotFoundException("No file metadata found for: " + fileName);
        }

        var fileToDownload = new java.io.File(STORAGE_DIRECTORY + java.io.File.separator + fileName);
        if(!Objects.equals(fileToDownload.getParent(), STORAGE_DIRECTORY)){
            throw new SecurityException("Unsupported filename!");
        }
        if(!fileToDownload.exists()){
            throw new FileNotFoundException("Physical file not found: " + fileName);
        }
        return fileToDownload;
    }
}
