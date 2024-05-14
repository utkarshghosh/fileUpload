package com.example.demo.service.impl;

import com.example.demo.dto.FileMetaDataDto;
import com.example.demo.exceptions.FileNotFoundException;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.service.FileService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.util.*;

@Service
public class FileServiceImpl implements FileService {



    @Autowired
    private ResourceLoader resourceLoader;


    private Map<String , List<String>> fileNameMapping = new HashMap<>();


    @Override
    public String uploadFile(MultipartFile file , String userId) {
        try {
            Path rootLocation = Paths.get(Constants.FILE_UPLOAD_PATH);
            String uniqueFileName = getUniqueFileNameForUser(file.getOriginalFilename() , userId);
            Path destinationFile = rootLocation.resolve(
                            Paths.get(uniqueFileName))
                    .normalize().toAbsolutePath();
            updateFileNameMapping(file.getOriginalFilename() ,uniqueFileName );
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
            return Constants.SUCCESS_RESPONSE;
        } catch (IOException ex) {
            System.out.printf("ex : ");
            throw new InvalidDataException("File cannot be stored , Exception :" + ex);
        }
    }

    private void updateFileNameMapping(String originalFilename, String uniqueFileName) {
        List<String> uniqueFileNames = Collections.singletonList(uniqueFileName);
        List<String> alreadyPresentUniqueNames = fileNameMapping.get(originalFilename);
        if(CollectionUtils.isEmpty(alreadyPresentUniqueNames)){
            fileNameMapping.put(originalFilename , uniqueFileNames);
        }else {
            alreadyPresentUniqueNames.addAll(uniqueFileNames);
        }
    }

    private String getUniqueFileNameForUser(String fileName , String userId){
        StringBuilder stringBuilder  = new StringBuilder();
        if(!StringUtils.isEmpty(userId))
            return stringBuilder.append(userId).append("_").append(fileName).toString();
        else
            return fileName;
    }

    @Override
    public byte[] getFile(String fileName , String userId) {
        try {
            String uniqueFileName = getStoredFileNameForUser(fileName , userId);
            Resource resource = resourceLoader.getResource("file:uploads/" + uniqueFileName);
            InputStream inputStream =  resource.getInputStream();
            byte[] content = inputStream.readAllBytes();
            inputStream.close();
            return content;
        } catch (Exception ex) {
            System.out.printf("File cannot be retrieved ");
            System.out.printf("Error :  " + ex.getMessage());
            throw new FileNotFoundException("File Cannot be retrived : "+ ex.getMessage());
        }
    }

    private String getStoredFileNameForUser(String fileName, String userId) {
        List<String> uniqueFileNames = fileNameMapping.get(fileName);
        if(CollectionUtils.isEmpty(uniqueFileNames)){
            return fileName;
        }
        String uniqueFilename = getUniqueFileNameForUser(fileName , userId);
        Optional<String> fileNameOptional = uniqueFileNames.stream().filter(e->e.equals(uniqueFilename)).findFirst();
        if(fileNameOptional.isPresent()){
            return fileNameOptional.get();
        }else{
            return uniqueFileNames.get(uniqueFileNames.size()-1);
        }
    }

    @Override
    public FileMetaDataDto getMetaDataOfFile(String fileName , String userId) {
        try
        {
            String uniqueFileName = getStoredFileNameForUser(fileName , userId);
            Resource resource = resourceLoader.getResource("file:uploads/" + uniqueFileName);
            File file = resource.getFile();
            FileMetaDataDto fileMetaDataDto = FileMetaDataDto.builder().name(fileName)
                .size(file.getTotalSpace()).lastModifiedDate(new Date(file.lastModified()))
                .build();
        return fileMetaDataDto;
    }catch(Exception ex){
            throw new FileNotFoundException("File Cannot be retrived " + ex.getMessage());
        }
    }
}
