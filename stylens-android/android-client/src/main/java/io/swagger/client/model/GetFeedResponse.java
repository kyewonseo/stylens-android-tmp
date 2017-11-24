/**
 * style-api
 * This is a API document for Stylens Service
 *
 * OpenAPI spec version: 0.0.1
 * Contact: master@bluehack.net
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.model;

import io.swagger.client.model.Product;
import java.util.*;
import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;

@ApiModel(description = "")
public class GetFeedResponse {
  
  @SerializedName("message")
  private String message = null;
  @SerializedName("data")
  private List<Product> data = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<Product> getData() {
    return data;
  }
  public void setData(List<Product> data) {
    this.data = data;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetFeedResponse getFeedResponse = (GetFeedResponse) o;
    return (this.message == null ? getFeedResponse.message == null : this.message.equals(getFeedResponse.message)) &&
        (this.data == null ? getFeedResponse.data == null : this.data.equals(getFeedResponse.data));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.message == null ? 0: this.message.hashCode());
    result = 31 * result + (this.data == null ? 0: this.data.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetFeedResponse {\n");
    
    sb.append("  message: ").append(message).append("\n");
    sb.append("  data: ").append(data).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
