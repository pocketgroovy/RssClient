//package com.pocketgroovy.rss.demo.client.rssclient.service;
//
//public class RssFeedDetailService {
//
//    private final RssService rssService;
//
//    public Mono<ProductDetails> getProductDetails(String productId) {
//        Mono<Product> productMono = productService.getProductById(productId);
//        Flux<Category> categoryFlux = categoryService.getCategoriesByProductId(productId);
//
//        return productMono.zipWith(categoryFlux.collectList(),
//                (product, categories) -> new ProductDetails(product, categories));
//    }
//
//}
