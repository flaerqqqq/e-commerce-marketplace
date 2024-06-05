package com.example.ecommercemarketplace.validation;

import com.example.ecommercemarketplace.exceptions.UnexpectedVideoDurationException;
import com.example.ecommercemarketplace.exceptions.FileSizeExceededException;
import com.example.ecommercemarketplace.exceptions.InvalidFileTypeException;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.function.Function;

public class VideoValidator {

    private final static List<String> ACCEPTED_FORMATS = List.of("video/mp4", "video/x-msvideo", "video/quicktime", "video/x-matroska");
    private final static long MAX_FILE_SIZE = 209_715_200;
    private final static long MIN_VIDEO_DURATION = 15_000000;
    private final static long MAX_VIDEO_DURATION = 60_0000000;

    public static void validateFile(MultipartFile multipartFile){
        validateFileSize(multipartFile);
        validateVideoTypeFormat(multipartFile);
        validateVideoDuration(multipartFile);
    }

    private static void validateFileSize(MultipartFile multipartFile){
        if (multipartFile.getSize() > MAX_FILE_SIZE){
            throw new FileSizeExceededException("File size exceeded the limit in %d bytes".formatted(MAX_FILE_SIZE));
        }
    }

    private static void validateVideoTypeFormat(MultipartFile multipartFile){
        if (!ACCEPTED_FORMATS.contains(multipartFile.getContentType())){
            throw new InvalidFileTypeException("Video with type=%s is not supported".formatted(multipartFile.getContentType()));
        }
    }

    private static void validateVideoDuration(MultipartFile multipartFile){
        Long duration = getVideoFormat(multipartFile, FFmpegFrameGrabber::getLengthInTime);

        if (duration < MIN_VIDEO_DURATION) {
            throw new UnexpectedVideoDurationException("Video duration is less than 15 seconds");
        } else if (duration > MAX_VIDEO_DURATION) {
            throw new UnexpectedVideoDurationException("Video duration is greater than 10 minutes");
        }
    }

    private static <T> T getVideoFormat(MultipartFile multipartFile, Function<FFmpegFrameGrabber, T> func){
        try {
            Path tempFile = Files.createTempFile("temp-video", null);
            Files.copy(multipartFile.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
            try {
                FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(tempFile.toFile());
                grabber.start();
                T result = func.apply(grabber);
                grabber.stop();
                grabber.release();

                return result;
            } finally {
                Files.delete(tempFile);
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
