package com.example.demo.controller;

import com.example.demo.dto.FileMetaDataDto;
import com.example.demo.dto.ResponseDto;
import com.example.demo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@RestController
@RequestMapping("/file")
public class Controller {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file , @RequestPart("userId") String userId){
        String response = fileService.uploadFile(file , userId);
        return ResponseEntity.ok().body(response);
    }

    @ResponseBody
    @GetMapping("/retrive")
    public ResponseEntity<byte[]> getFile(@RequestParam String fileName ,  @RequestParam(required = false) String userId) throws IOException {
        return ResponseEntity.ok()
                .body(fileService.getFile(fileName , userId));
    }

    @GetMapping("/getmetaData")
    public ResponseEntity<FileMetaDataDto> getMetaData(@RequestParam String fileName , @RequestParam(required = false) String userId ) throws IOException{
        FileMetaDataDto metaDataDto = fileService.getMetaDataOfFile(fileName , userId);
        return ResponseEntity.ok().body(metaDataDto);
    }
}
