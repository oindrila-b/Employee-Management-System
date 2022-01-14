package com.example.oindrila.employeemanagementsystem.services;

import com.example.oindrila.employeemanagementsystem.models.ImageResource;
import com.example.oindrila.employeemanagementsystem.models.PhotoInfo;

import java.net.URL;

public interface ImageService {

    void uploadImage(final ImageResource imageResource) throws Exception;
    URL getImageURLByPhotoInfo(final PhotoInfo photoInfo) throws Exception;
}
