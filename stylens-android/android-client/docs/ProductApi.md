# ProductApi

All URIs are relative to *http://api.stylelens.io*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getProducts**](ProductApi.md#getProducts) | **POST** /products | Query to search products


<a name="getProducts"></a>
# **getProducts**
> GetProductsResponse getProducts(file)

Query to search products



### Example
```java
// Import classes:
//import io.swagger.client.api.ProductApi;

ProductApi apiInstance = new ProductApi();
File file = new File("/path/to/file.txt"); // File | Image file to upload (only support jpg format yet)
try {
    GetProductsResponse result = apiInstance.getProducts(file);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ProductApi#getProducts");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **file** | **File**| Image file to upload (only support jpg format yet) | [optional]

### Return type

[**GetProductsResponse**](GetProductsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: application/json

