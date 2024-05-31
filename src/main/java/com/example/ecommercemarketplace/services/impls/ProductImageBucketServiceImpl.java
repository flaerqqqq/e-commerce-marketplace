package com.example.ecommercemarketplace.services.impls;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.example.ecommercemarketplace.models.MainProductImage;
import com.example.ecommercemarketplace.models.ProductImage;
import com.example.ecommercemarketplace.services.ProductImageBucketService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.multi.MultiTabbedPaneUI;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductImageBucketServiceImpl implements ProductImageBucketService {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${aws.product.images.bucket.name}")
    private String imagesBucketName;

    @Override
    public List<ProductImage> saveImages(List<MultipartFile> images) {
        List<ProductImage> productImages = new ArrayList<>();

        for (MultipartFile image : images) {
            String url = save(image);
            productImages.add(createProductImageObject(url));
        }

        return productImages;
    }

    @Override
    public MainProductImage saveMainImage(MultipartFile mainImage){
        String url = save(mainImage);

        return createMainProductImageObject(url);
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile){
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    private String generateFileName(MultipartFile multipartFile) {
        return new Date().getTime() + "-" + multipartFile.getOriginalFilename()
                .replace(" ", "-")
                .toLowerCase();
    }

    private MainProductImage createMainProductImageObject(String url){
        return MainProductImage.builder()
                .url(url)
                .build();
    }

    private ProductImage createProductImageObject(String url){
        return ProductImage.builder()
                .url(url)
                .build();
    }

    private String save(MultipartFile file){
        File imageFile = convertMultipartFileToFile(file);
        String fileName = generateFileName(file);

        PutObjectRequest putObjectRequest = new PutObjectRequest(imagesBucketName, fileName, imageFile);
        amazonS3.putObject(putObjectRequest);

        imageFile.delete();
        return amazonS3.getUrl(imagesBucketName, fileName).toExternalForm();
    }
}

