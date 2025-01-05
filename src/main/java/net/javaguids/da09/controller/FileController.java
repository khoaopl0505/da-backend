package net.javaguids.da09.controller;

import net.javaguids.da09.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.logging.Logger;

@Controller
public class FileController {
    @Autowired
    private FileService fileService;
    private static final Logger log = Logger.getLogger(FileController.class.getName());

    @PostMapping("/group/upload-file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            fileService.saveFile(file); // Call the service method to save the file
            return ResponseEntity.ok("File uploaded successfully.");
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body("File is null: " + e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unsupported filename: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/group/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("file") String filename){
        try{
            var fileDownload = fileService.getDownloadFile(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentLength(fileDownload.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new FileSystemResource(fileDownload));
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
}
