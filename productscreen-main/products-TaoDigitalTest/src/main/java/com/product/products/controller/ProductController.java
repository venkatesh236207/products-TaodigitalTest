package com.product.products.controller;

import com.product.products.entity.ProductDTO;
import com.product.products.entity.ProductEntity;
import com.product.products.service.ProductService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {


    @Autowired
    private ProductService productService;

    @ApiOperation(value = "API to List Active Products", notes = "Get the list of active products sorted by the latest first.")
    @GetMapping
    public List<ProductEntity> getProducts(){
        return productService.getAllActiveProducts();
    }

    @ApiOperation(value = "API to Search Products", notes = "Search for products based on the given criteria (product name, price range,\n" +
            "and posted date range)." )
    @GetMapping("/search")
    public List<ProductEntity> getSearchProducts(
            @RequestParam(value = "productName", required=false) String productName,
            @RequestParam(value = "minPrice", required=false) Integer minPrice,
            @RequestParam(value = "maxPrice", required=false) Integer maxPrice,
            @ApiParam(value = "Date in 'yyyy-MM-dd' format")
            @RequestParam(value = "minPostedDate", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date minPostedDate,
            @ApiParam(value = "Date in 'yyyy-MM-dd' format")
            @RequestParam(value = "maxPostedDate", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date maxPostedDate){


        return productService.getAllSearchProducts(productName, minPrice, maxPrice, minPostedDate, maxPostedDate);
    }

    @ApiOperation(value = "API to Create a Product" , notes = "Create a new product, but the price must not exceed $10,000. If the price is\n" +
            "more than $5,000, the product should be pushed to the approval queue.")
    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody ProductDTO product) {

        if(product.getPrice() > 10000){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Product price should not be more than $10000.");
        }else{
            return productService.createProduct(product);
        }

    }

    @ApiOperation(value = "API to Update a Product", notes = "Update an existing product, but if the price is more than 50% of its previous\n" +
            "price, the product should be pushed to the approval queue.")
    @PutMapping("/{productId}")
    public ResponseEntity<Object> updateProduct(@PathVariable("productId") long productId, @RequestBody ProductDTO product) {

        if(product.getPrice() > 10000){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Product price should not be more than $10000.");
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(productId, product));
        }

    }

    @ApiOperation(value = "API to Delete a Product", notes = "Delete a product, and it should be pushed to the approval queue.")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Object> deleteProduct(@PathVariable("productId") long productId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productService.deleteProduct(productId));
    }

    @ApiOperation(value = "API to Get Products in Approval Queue", notes = "Get all the products in the approval queue, sorted by request date (earliest\n" +
            "first).")
    @GetMapping("/approval-queue")
    public ResponseEntity<Object> getAllProductFromApprovalQueue(){
        return  ResponseEntity.status(HttpStatus.OK).body(productService.getAllProductFromApprovalQueue());
    }

    @ApiOperation(value = "API to Approve a Product", notes = "Approve a product from the approval queue. The product state should be\n" +
            "updated, and it should no longer appear in the approval queue.")
    @PutMapping("/approval-queue/{approvalId}/approve")
    public ResponseEntity<Object> approveProductByApprovalId(@PathVariable("approvalId") long approvalId){
        return  ResponseEntity.status(HttpStatus.OK).body(productService.approveProductByApprovalId(approvalId));
    }

    @ApiOperation(value = "API to Reject a Product", notes = "Reject a product from the approval queue. The product state should remain\n" +
            "the same, and it should no longer appear in the approval queue.")
    @PutMapping("/approval-queue/{approvalId}/reject")
    public ResponseEntity<Object> rejectProductByApprovalId(@PathVariable("approvalId") long approvalId){
        return  ResponseEntity.status(HttpStatus.OK).body(productService.rejectProductByApprovalId(approvalId));
    }
}
