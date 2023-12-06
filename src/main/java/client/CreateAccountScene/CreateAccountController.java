package client.CreateAccountScene;

import client.AppController;
import client.HttpResponse.ServerResponse;
import client.Recipe;
import java.io.File;
import javafx.event.ActionEvent;

public class CreateAccountController {

  CreateAccountView createAccountView;
  CreateAccountModel createAccountModel;
  AppController appController;

  public CreateAccountController(
    CreateAccountView createAccountView,
    CreateAccountModel createAccountModel,
    AppController appController
  ) {
    this.createAccountModel = createAccountModel;
    this.createAccountView = createAccountView;
    this.appController = appController;

    createAccountView.setLogInButtonOnAction(this::handleLogInButtonAction);
    createAccountView.setCreateButtonOnAction(this::handleCreateButtonAction);
  }

  private void handleLogInButtonAction(ActionEvent event) {
    appController.changeToLogInScene();
  }

  private void handleCreateButtonAction(ActionEvent event) {
    String user = createAccountView.getUsername();
    String pass = createAccountView.getPassword();
    if (user == null || pass == null || user.isEmpty() || pass.isEmpty()) {
      createAccountView.showError("Please enter username and password.");
      return;
    }
    ServerResponse<String> createAccResponse = createAccountModel.performStoreDetails(
      user,
      pass
    );
    // if (createAccResponse.getStatusCode() == 503) {
    //   appController.handleServerDown();
    //   return;
    // }
    if (createAccResponse.getStatusCode() != 200) {
      createAccountView.showError(createAccResponse.getErrorMsg());
      // System.out.println("Response: " + createAccResponse.getResponse());
      // System.out.println("Create Acc Error Msg: " + createAccResponse.getErrorMsg());
    } else {
      // System.out.println("Successfully created Account");
      appController.changeToLogInScene();
    }
    // if(createAccountModel.performStoreDetails(user, pass).getStatusCode()==503
    // || createAccountModel.performStoreDetails(user, pass).getStatusCode()==501
    // ){
    //     createAccountView.showError();
    // }
    // else{
    //     appController.changeToRecipeListScene();
    // }

    // if (createAccountModel.checkValidUsername(user) == false) {
    //     createAccountModel.performStoreDetails(user, pass);
    //     appController.changeToRecipeListScene();
    // }
    // else {
    //     createAccountView.showError();
    //     System.out.println("Invalid Username");
    // }
  }
}
