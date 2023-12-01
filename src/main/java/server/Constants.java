package server;

public final class Constants {

  public static final String GET = "GET";
  public static final String POST = "POST";
  public static final String PUT = "PUT";
  public static final String DELETE = "DELETE";
  public static final String TRUE = "TRUE";
  public static final String FALSE = "FALSE";
  public static final String INVALID_REQ_TO_ROUTE =
    "ERROR: Invalid Request Method to Route";
  public static final String INVALID_GET_TO_ROUTE =
    "ERROR: Invalid Get Request to Route";
  public static final String INVALID_POST_TO_ROUTE =
    "ERROR: Invalid Post Request to Route";
  public static final String INVALID_PUT_TO_ROUTE =
    "ERROR: Invalid Put Request to Route";
  public static final String INVALID_DELETE_TO_ROUTE =
    "ERROR: Invalid Delete Request to Route";
  public static final String USER_EXISTS = "ERROR: Username Already Exists";
  public static final String WRONG_AUTH = "ERROR: Incorrect Username/Password";

  private Constants() {}
}
