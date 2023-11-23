package client.HttpResponse;

public class WhisperResponse implements ServerResponse {

  private String errorMsg;
  private int statusCode;
  private String whisperResponseString;

  public WhisperResponse() {
    errorMsg = null;
    whisperResponseString = null;
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
  public Object getResponse() {
    return this.whisperResponseString;
  }

  @Override
  public void setValidResponse(String res) {
    statusCode = 200;
    whisperResponseString = res;
    errorMsg = null;
  }

  @Override
  public void setErrorResponse(int statusCode, String err) {
    this.statusCode = statusCode;
    this.errorMsg = err;
    whisperResponseString = null;
  }

  @Override
  public void setServerDownResponse() {
    this.statusCode = 503;
    this.errorMsg = "The server is Down!";
    whisperResponseString = null;
  }
}
