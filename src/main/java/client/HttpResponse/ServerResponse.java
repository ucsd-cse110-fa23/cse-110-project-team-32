package client.HttpResponse;

public interface ServerResponse<E> {
  public int getStatusCode();

  public String getErrorMsg();

  public E getResponse();

  public void setValidResponse(String res);

  public void setErrorResponse(int statusCode, String err);

  public void setServerDownResponse();
}
