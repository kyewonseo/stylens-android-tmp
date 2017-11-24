# FeedApi

All URIs are relative to *http://api.stylelens.io*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getFeeds**](FeedApi.md#getFeeds) | **GET** /feeds | 


<a name="getFeeds"></a>
# **getFeeds**
> GetFeedResponse getFeeds()



Returns Main Feeds

### Example
```java
// Import classes:
//import io.swagger.client.api.FeedApi;

FeedApi apiInstance = new FeedApi();
try {
    GetFeedResponse result = apiInstance.getFeeds();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FeedApi#getFeeds");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**GetFeedResponse**](GetFeedResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

