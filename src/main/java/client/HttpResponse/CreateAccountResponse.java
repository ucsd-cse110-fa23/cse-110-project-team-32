package client.HttpResponse;

import client.Recipe;

public class CreateAccountResponse implements ServerResponse<String> {

  private String errorMsg;
  private int statusCode;
  private String accountDetails;

  public CreateAccountResponse() {
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
  public String getResponse() {
    return accountDetails;
  }

  @Override
  public void setValidResponse(String res) {
    statusCode = 200;
    errorMsg = null;
    accountDetails = res;
    // String[] hashSeparatedResultArray = res.split("#");
    // String mealType = hashSeparatedResultArray[0].strip();
    // String title = hashSeparatedResultArray[1].strip();
    // String recipeDetail = hashSeparatedResultArray[2].strip();

    // recipe = new Recipe(title, mealType, recipeDetail);
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
}
