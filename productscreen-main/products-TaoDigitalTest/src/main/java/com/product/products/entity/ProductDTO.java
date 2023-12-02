package com.product.products.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Product price should not be more than $10000 and Status should be as Pending(1) or Approved(2) or Rejected(3)")
public class ProductDTO {
    @ApiModelProperty(example = "Bed")
    private String name;
    @ApiModelProperty(example = "6000")
    private int price;
    @ApiModelProperty(example = "1")
    private int status;
}
