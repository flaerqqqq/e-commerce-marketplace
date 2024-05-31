package com.example.ecommercemarketplace.validation;


import com.example.ecommercemarketplace.exceptions.InvalidFileTypeException;
import com.example.ecommercemarketplace.exceptions.InvalidImageAspectRatioException;
import com.example.ecommercemarketplace.exceptions.InvalidImageResolutionException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageValidator {

    private static final int MIN_WIDTH = 250;
    private static final int MIN_HEIGHT = 250;
    private static final int MAX_WIDTH = 5000;
    private static final int MAX_HEIGHT = 5000;
    private static final double MIN_ASPECT_RATIO = 0.5;
    private static final double MAX_ASPECT_RATIO = 2.0;

    public static void validateFile(MultipartFile file){
        validateFileType(file);
        validateImageDimensions(file);
        validateImageAspectRatio(file);
    }

    private static void validateFileType(MultipartFile file){
        if (!file.getContentType().startsWith("image")) {
            throw new InvalidFileTypeException("Given %s, but should be image/*".formatted(file.getContentType()));
        }
    }

    private static void validateImageDimensions(MultipartFile file){
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            int height = image.getHeight();
            int width = image.getWidth();

            if (width < MIN_WIDTH || height < MIN_HEIGHT) {
                throw new InvalidImageResolutionException("Minimum image size is 250x250, but given is %dx%d".formatted(width, height));
            } else if (width > MAX_WIDTH && height > MAX_HEIGHT) {
                throw new InvalidImageResolutionException("Maximum image size is 5000x5000, but given is %dx%d".formatted(width, height));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void validateImageAspectRatio(MultipartFile file){
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            int width = image.getWidth();
            int height = image.getHeight();

            if (((double) width / height) < MIN_ASPECT_RATIO) {
                throw new InvalidImageAspectRatioException("Width and height are less than the minimum aspect ratio");
            } else if (((double) width / height) > MAX_ASPECT_RATIO) {
                throw new InvalidImageAspectRatioException("Width and height are greater than the maximum aspect ratio");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
