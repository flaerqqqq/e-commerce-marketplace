package com.example.ecommercemarketplace.repositories.elasticsearch;

import com.example.ecommercemarketplace.documents.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, String> {
    List<ProductDocument> findProductDocumentByProductName(String name);
}
