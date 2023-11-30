package client.HttpResponse;

public class PingResponse implements ServerResponse<Boolean> {

  private String errorMsg;
  private int statusCode;

  public PingResponse() {
    errorMsg = null;
  }

  @Override
  public int getStatusCode() {
    return statusCode;
  }

  @Override
  public String getErrorMsg() {
    return this.errorMsg;
  }

  @Override
  public Boolean getResponse() {
    return this.statusCode == 200;
  }

  @Override
  public void setValidResponse(String res) {
    statusCode = 200;
    errorMsg = null;
  }

  @Override
  public void setErrorResponse(int statusCode, String err) {
    this.statusCode = statusCode;
    this.errorMsg = err;
  }

  @Override
  public void setServerDownResponse() {
    this.statusCode = 503;
    this.errorMsg = "The server is Down!";
  }

  @Override
  public String toString() {
    String temp = "Ping Server Response: \nStatus Code: %d \nError Msg: %s";
    return String.format(temp, statusCode, errorMsg);
  }
}
