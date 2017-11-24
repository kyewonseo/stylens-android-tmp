# swagger-android-client

## Requirements

Building the API client library requires [Maven](https://maven.apache.org/) to be installed.

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn deploy
```

Refer to the [official documentation](https://maven.apache.org/plugins/maven-deploy-plugin/usage.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-android-client</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "io.swagger:swagger-android-client:1.0.0"
```

### Others

At first generate the JAR by executing:

    mvn package

Then manually install the following JARs:

* target/swagger-android-client-1.0.0.jar
* target/lib/*.jar

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

import io.swagger.client.api.FeedApi;

public class FeedApiExample {

    public static void main(String[] args) {
        FeedApi apiInstance = new FeedApi();
        try {
            GetFeedResponse result = apiInstance.getFeeds();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling FeedApi#getFeeds");
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *http://api.stylelens.io*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*FeedApi* | [**getFeeds**](docs/FeedApi.md#getFeeds) | **GET** /feeds | 
*ObjectApi* | [**getObjects**](docs/ObjectApi.md#getObjects) | **POST** /objects | Query to search multiple objects
*ProductApi* | [**getProducts**](docs/ProductApi.md#getProducts) | **POST** /products | Query to search products


## Documentation for Models

 - [BoxArray](docs/BoxArray.md)
 - [BoxObject](docs/BoxObject.md)
 - [BoxesArray](docs/BoxesArray.md)
 - [GetFeedResponse](docs/GetFeedResponse.md)
 - [GetObjectsResponse](docs/GetObjectsResponse.md)
 - [GetObjectsResponseData](docs/GetObjectsResponseData.md)
 - [GetProductsResponse](docs/GetProductsResponse.md)
 - [Product](docs/Product.md)
 - [ProductsArray](docs/ProductsArray.md)
 - [SearchImageResponse](docs/SearchImageResponse.md)


## Documentation for Authorization

Authentication schemes defined for the API:
### api_key

- **Type**: API key
- **API key parameter name**: api_key
- **Location**: HTTP header


## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author

master@bluehack.net

