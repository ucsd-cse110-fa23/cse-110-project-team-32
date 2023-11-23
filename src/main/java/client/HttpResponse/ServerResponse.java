package client.HttpResponse;

public interface ServerResponse {
  public int getStatusCode();

  public String getErrorMsg();

  public Object getResponse();

  public void setValidResponse(String res);

  public void setErrorResponse(int statusCode, String err);

  public void setServerDownResponse();
}
