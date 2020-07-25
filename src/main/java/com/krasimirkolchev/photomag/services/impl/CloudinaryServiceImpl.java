package com.krasimirkolchev.photomag.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryServiceImpl {
    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }


    public String createPhoto(MultipartFile file, String dirLoc, String dirName) throws IOException {

        Map params = ObjectUtils.asMap(
                "public_id", String.format("%s/%s/gallery/%s", dirLoc, dirName, file.getOriginalFilename()),
                "overwrite", true,
                "notification_url", "https://mysite/notify_endpoint",
                "resource_type", "image"
        );

        return this.cloudinary.uploader().upload(file.getBytes(), params).get("url").toString();
    }

    public List<String> createPhotos(List<MultipartFile> files, String dirLoc, String dirName) throws IOException {

        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files) {
            Map params = ObjectUtils.asMap(
                    "public_id", String.format("%s/%s/gallery/%s", dirLoc, dirName, file.getOriginalFilename()),
                    "overwrite", true,
                    "notification_url", "https://mysite/notify_endpoint",
                    "resource_type", "image"
            );
            urls.add(this.cloudinary.uploader().upload(file.getBytes(), params).get("url").toString());
        }

        return urls;
    }

}
