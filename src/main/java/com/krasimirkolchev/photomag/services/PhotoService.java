package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.entities.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PhotoService {

    Photo createPhoto(MultipartFile file, String dir) throws IOException;

    List<Photo> getPhotosByUserId(String id);

    Photo getPhotoById(String id);
}
