package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class FileMetaDataDto {

    private String name ;
    private Long size;
    private Date lastModifiedDate;
}
