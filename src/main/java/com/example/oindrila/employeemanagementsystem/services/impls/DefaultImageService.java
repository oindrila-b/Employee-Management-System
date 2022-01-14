package com.example.oindrila.employeemanagementsystem.services.impls;

import com.example.oindrila.employeemanagementsystem.exceptions.NoResourceFoundException;
import com.example.oindrila.employeemanagementsystem.models.ImageResource;
import com.example.oindrila.employeemanagementsystem.models.PhotoInfo;
import com.example.oindrila.employeemanagementsystem.services.ImageService;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DefaultImageService implements ImageService {

    @Value("${file.resource-upload-temp-location}")
    private String photoUploadPath;

    @Value("${firebase.token-location}")
    private String firebaseTokenPath;

    @Value("${firebase.bucket-name}")
    private String bucketName;

    @Value("${firebase.image-url-expiry}")
    private Long imageExpiryValue;

    private Path root;

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(Paths.get(photoUploadPath));
        root = Paths.get(photoUploadPath);
    }

////    @Override
//    public String saveResource(final ImageResource imageResource) throws Exception {
//        log.info("Request to save Image Resource: {}", imageResource);
//        final MultipartFile file = imageResource.getFile();
//        String fileName = imageResource.getFileName();
//        final String TEMP_URL;
//
////        File convertedFile;
////        if (imageResource!= null) {
////            convertedFile  = convertToFile(file,fileName);
////        }
//        try {
//            String imageFileName  = file.getOriginalFilename();
//            imageFileName = UUID.randomUUID().toString().concat(this.getExtension(imageFileName));
//
//            File convertedFile = this.convertToFile(file, imageFileName);
//            TEMP_URL = this.uploadFile(convertedFile, imageFileName);
//            convertedFile.delete();
//
//            return TEMP_URL;
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        //        Objects.requireNonNull(file);
////        Files.copy(file.getInputStream(), root.resolve(imageResource.getFileName()));
//
//        return "";
//    }
//
////    @Override
//    public File getFile(final String fileName) throws Exception {
//        log.info("Retrieving File with name: {}", fileName);
//        final Path targetPath = root.resolve(fileName);
//        final Resource resource = new UrlResource(targetPath.toUri());
//        if (resource.exists() || resource.isReadable()) {
//            log.info("Resource found");
//            return resource.getFile();
//        }
//        throw new NoResourceFoundException("No resource found by File Name: " + fileName);
//    }
//
//    private File convertToFile(final  MultipartFile file, final String fileName){
//        File tempFile = new File(fileName);
//        try(FileOutputStream fos = new FileOutputStream(tempFile)){
//            fos.write(file.getBytes());
//        }
//        catch (Exception e){
//            log.info(e.getMessage());
//        }
//        return tempFile;
//    }
//
//    private File toFile(final MultipartFile multipartFile) throws IOException {
//        File convertedFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
//        if (convertedFile.createNewFile()) {
//            try (final InputStream is = multipartFile.getInputStream()) {
//                Files.copy(is, convertedFile.toPath());
//            }
//            return convertedFile;
//        } else {
//            throw new RuntimeException("Could not create new file");
//        }
//    }
//
//    private String uploadFile(File file, String fileName) throws IOException {
//        log.info("Upload File to Firebase ");
//        BlobId blobId = BlobId.of(bucketName, fileName);
//        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
//        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(firebaseTokenPath));
//        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
//        final Blob b = storage.create(blobInfo, Files.readAllBytes(file.toPath()));
//        final URL url = b.signUrl(10L, TimeUnit.MINUTES);
//        log.info("Signed URL: {}", url.toString());
//
//        final BlobId newBlobId = BlobId.of("employeemanagementsystem-ad8ed.appspot.com", fileName);
//        final Blob b2 = storage.get(newBlobId);
//        final String secondUrl = b2.signUrl(10L, TimeUnit.MINUTES).toString();
//
//        log.info("Signed URL 2: {}", secondUrl);
//        log.info("Are they same: {}", (url.toString().equals(secondUrl)));
//        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
//    }

//    @Override
//    public URL saveProfilePhoto(final ImageResource imageResource) throws Exception {
//        log.info("Saving Image Resource: {}", imageResource);
//        saveTemporarily(imageResource.getFile(),  imageResource.getFileName());
//        final File file = retrieveFile(imageResource.getFileName());
//        final String mimeType = getMimeType(file);
//        final String fileName = imageResource.getFileName();
//        final BlobId blobId = BlobId.of(bucketName, fileName);
//        log.info("File Name: {}", file.getName());
//        log.info("Mime-Type: {}", mimeType);
//        return null;
//    }

    private String getMimeType(final File file) throws IOException {
        final Path path = file.toPath();
        return Files.probeContentType(path);
    }

    private void saveTemporarily(final MultipartFile multipartFile, final String fileName) throws IOException {
        log.info("Saving Multipart File temporarily with file name {}", fileName);
        Objects.requireNonNull(multipartFile);
        Files.copy(multipartFile.getInputStream(), root.resolve(fileName));
    }

    private File retrieveFile(final String fileName) {
        log.info("Retrieving File {} from temporary location", fileName);
        final Path path = root.resolve(fileName);
        return new File(path.toUri());
    }

    @Override
    public void uploadImage(final ImageResource imageResource) throws Exception {
        log.info("Uploading Image Resource: {}", imageResource);
        saveTemporarily(imageResource.getFile(), imageResource.getFileName());
        final File file = retrieveFile(imageResource.getFileName());
        final String mimeType = getMimeType(file);
        uploadToFireBase(file, imageResource.getFileName(), mimeType);
    }

    private void uploadToFireBase(final File file, final String fileName, final String mimeType) throws IOException {
        log.info("Uploading to Firebase:\nFile: {}\nFile Name:{}\nMime-Type: {}", file, fileName, mimeType);
        final BlobId blobId = BlobId.of(bucketName,  fileName);
        log.info("Created Blob ID: {}", blobId);
        final BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(mimeType).build();
        log.info("Created Blob Info: {}", blobInfo);
        final Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(firebaseTokenPath));
        final Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        log.info("Created Storage: {}", storage);
        final Blob blob = storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        log.info("Stored Blob: {}", blob);
        log.info("Deleting temporary file: {}", file);
        if (file.delete()) {
            log.info("Successfully deleted file");
        } else {
            log.warn("Failed to delete file");
        }
    }

    @Override
    public URL getImageURLByPhotoInfo(final PhotoInfo photoInfo) throws IOException {
        log.info("Getting Image URL by Photo Info: {}", photoInfo);
        final BlobId blobId = BlobId.of(bucketName, photoInfo.getName());
        final Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(firebaseTokenPath));
        final Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        final Blob blob = storage.get(blobId);
        log.info("Retrieved Blob: {}", blob);
        return blob.signUrl(imageExpiryValue, TimeUnit.MINUTES);
    }
}
