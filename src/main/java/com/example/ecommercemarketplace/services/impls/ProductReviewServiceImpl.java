package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.ProductReviewRequestDto;
import com.example.ecommercemarketplace.dto.ProductReviewResponseDto;
import com.example.ecommercemarketplace.exceptions.UnsupportedContentFileTypeException;
import com.example.ecommercemarketplace.mappers.ProductReviewMapper;
import com.example.ecommercemarketplace.models.Product;
import com.example.ecommercemarketplace.models.ProductReview;
import com.example.ecommercemarketplace.models.ProductReviewMediaContent;
import com.example.ecommercemarketplace.repositories.ProductRepository;
import com.example.ecommercemarketplace.repositories.ProductReviewRepository;
import com.example.ecommercemarketplace.repositories.UserRepository;
import com.example.ecommercemarketplace.services.ProductReviewService;
import com.example.ecommercemarketplace.services.ProductService;
import com.example.ecommercemarketplace.services.ReviewMediaFileBucketService;
import com.example.ecommercemarketplace.validation.ImageValidator;
import com.example.ecommercemarketplace.validation.VideoValidator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductReviewServiceImpl implements ProductReviewService {

    private final ProductService productService;
    private final ReviewMediaFileBucketService reviewMediaFileBucketService;
    private final ProductReviewRepository productReviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductReviewMapper productReviewMapper;

    @Override
    public ProductReviewResponseDto createProductReview(Long productId, ProductReviewRequestDto createRequest,
                                                        List<MultipartFile> mediaContent, Authentication authentication) {
        productService.throwIfProductNotFound(productId);

        ProductReview productReview = ProductReview.builder()
                .textContent(createRequest.getTextContent())
                .build();

        productReview.setProduct(productRepository.findById(productId).get());
        productReview.setUser(userRepository.findByEmail(authentication.getName()).get());

        if (mediaContent != null) {
            validateMediaContents(mediaContent);
            List<ProductReviewMediaContent> uploadedMediaContent = reviewMediaFileBucketService.uploadProductMediaContent(mediaContent);
            uploadedMediaContent.forEach(media -> media.setProductReview(productReview));
            productReview.setMediaContents(uploadedMediaContent);
        }

        ProductReview savedProductReview = productReviewRepository.save(productReview);
        return productReviewMapper.mapToResponseDto(savedProductReview);
    }

    @Override
    public Page<ProductReviewResponseDto> findAllProductReviewsByProduct(Long productId, Pageable pageable) {
        productService.throwIfProductNotFound(productId);

        Product product = productRepository.findById(productId).get();
        Page<ProductReview> pageOfProductReviews = productReviewRepository.findAllByProduct(product, pageable);
        return pageOfProductReviews.map(productReviewMapper::mapToResponseDto);
    }

    private void validateMediaContents(List<MultipartFile> mediaContents){
        for (MultipartFile mediaContent : mediaContents){
            validateMediaContentFile(mediaContent);
        }
    }

    private void validateMediaContentFile(MultipartFile mediaContentFile){
        if (mediaContentFile.getContentType().startsWith("image/")) {
            ImageValidator.validateFile(mediaContentFile);
        } else if (mediaContentFile.getContentType().startsWith("video/")){
            VideoValidator.validateFile(mediaContentFile);
        } else {
            throw new UnsupportedContentFileTypeException("File with type=%s is not supported".formatted(mediaContentFile.getContentType()));
        }
    }
}
