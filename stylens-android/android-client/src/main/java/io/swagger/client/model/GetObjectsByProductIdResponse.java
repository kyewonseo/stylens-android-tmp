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

import io.swagger.client.model.GetObjectsByProductIdResponseData;
import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;

@ApiModel(description = "")
public class GetObjectsByProductIdResponse {
  
  @SerializedName("message")
  private String message = null;
  @SerializedName("data")
  private GetObjectsByProductIdResponseData data = null;

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
  public GetObjectsByProductIdResponseData getData() {
    return data;
  }
  public void setData(GetObjectsByProductIdResponseData data) {
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
    GetObjectsByProductIdResponse getObjectsByProductIdResponse = (GetObjectsByProductIdResponse) o;
    return (this.message == null ? getObjectsByProductIdResponse.message == null : this.message.equals(getObjectsByProductIdResponse.message)) &&
        (this.data == null ? getObjectsByProductIdResponse.data == null : this.data.equals(getObjectsByProductIdResponse.data));
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
    sb.append("class GetObjectsByProductIdResponse {\n");
    
    sb.append("  message: ").append(message).append("\n");
    sb.append("  data: ").append(data).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
