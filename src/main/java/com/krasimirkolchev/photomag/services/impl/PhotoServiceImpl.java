package com.krasimirkolchev.photomag.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.krasimirkolchev.photomag.models.entities.Photo;
import com.krasimirkolchev.photomag.repositories.PhotoRepository;
import com.krasimirkolchev.photomag.services.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class PhotoServiceImpl implements PhotoService {
    private final PhotoRepository photoRepository;
    private final Cloudinary cloudinary;


    @Autowired
    public PhotoServiceImpl(PhotoRepository photoRepository, Cloudinary cloudinary) {
        this.photoRepository = photoRepository;
        this.cloudinary = cloudinary;
    }

    @Override
    public Photo createPhoto(MultipartFile file, String dir) throws IOException {
        Map upload;

        Map params = ObjectUtils.asMap(
                "public_id", String.format("users/%s/gallery/%s", dir, file.getOriginalFilename()),
                "overwrite", true,
                "notification_url", "https://mysite/notify_endpoint",
                "resource_type", "image"
        );
        upload = this.cloudinary.uploader().upload(file.getBytes(), params);

        return this.photoRepository.save(new Photo(upload.get("url").toString()));
    }

    @Override
    public List<Photo> getPhotosByUserId(String id) {
        return this.photoRepository.findAllByUser_Id(id);
    }

    @Override
    public Photo getPhotoById(String id) {
        return this.photoRepository.findById(id).orElse(null);
    }


}
