package client.HttpResponse;

public class WhisperResponse implements ServerResponse<String> {

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
  public String getResponse() {
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

  @Override
  public String toString() {
    String temp =
      "Whisper Response: \nStatus Code: %d \nError Msg: %s \nTranslated Message: %s";
    return String.format(temp, statusCode, errorMsg, whisperResponseString);
  }
}
