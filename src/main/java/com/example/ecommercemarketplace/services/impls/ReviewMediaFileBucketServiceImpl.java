package com.example.ecommercemarketplace.services.impls;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.ecommercemarketplace.models.ProductReviewMediaContent;
import com.example.ecommercemarketplace.services.ReviewMediaFileBucketService;
import com.example.ecommercemarketplace.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewMediaFileBucketServiceImpl implements ReviewMediaFileBucketService {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${aws.product.review.media-content.bucket-name}")
    private String bucketName;

    @Override
    public List<ProductReviewMediaContent> uploadProductMediaContent(List<MultipartFile> mediaContent) {
        List<ProductReviewMediaContent> uploadedMediaContent = new ArrayList<>();

        for (MultipartFile multipartFile : mediaContent) {
            String url = save(multipartFile);
            uploadedMediaContent.add(
                    ProductReviewMediaContent.builder()
                            .url(url)
                            .build()
            );
        }

        return uploadedMediaContent;
    }

    private String save(MultipartFile file) {
        File imageFile = FileUtils.convertMultipartFileToFile(file);
        String fileName = FileUtils.generateFileName(file);
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, imageFile);

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(imageFile.length());

            putObjectRequest.setMetadata(objectMetadata);
            amazonS3.putObject(putObjectRequest);
        } finally {
            imageFile.delete();
        }
        return amazonS3.getUrl(bucketName, fileName).toExternalForm();
    }
}
