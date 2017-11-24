# ObjectApi

All URIs are relative to *http://api.stylelens.io*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getObjects**](ObjectApi.md#getObjects) | **POST** /objects | Query to search multiple objects


<a name="getObjects"></a>
# **getObjects**
> GetObjectsResponse getObjects(file)

Query to search multiple objects



### Example
```java
// Import classes:
//import io.swagger.client.api.ObjectApi;

ObjectApi apiInstance = new ObjectApi();
File file = new File("/path/to/file.txt"); // File | Image file to upload (only support jpg format yet)
try {
    GetObjectsResponse result = apiInstance.getObjects(file);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ObjectApi#getObjects");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **file** | **File**| Image file to upload (only support jpg format yet) | [optional]

### Return type

[**GetObjectsResponse**](GetObjectsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: application/json

