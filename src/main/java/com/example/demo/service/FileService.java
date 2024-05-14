package com.example.demo.service;


import com.example.demo.dto.FileMetaDataDto;
import com.example.demo.dto.ResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {

    String uploadFile(MultipartFile file , String userId);

    byte[] getFile(String fileName , String userId );

    FileMetaDataDto getMetaDataOfFile(String fileName , String userId) ;
}
