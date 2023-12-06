package client.LogInScene;

import client.AppController;
import client.HttpResponse.ServerResponse;
import client.UserSettings;
import javafx.event.ActionEvent;

public class LogInController {

  LogInView logInView;
  LogInModel logInModel;
  AppController appController;
  private final UserSettings USER_SETTINGS = UserSettings.getInstance();

  public LogInController(
    LogInView logInView,
    LogInModel logInModel,
    AppController appController
  ) {
    this.logInView = logInView;
    this.logInModel = logInModel;
    this.appController = appController;

    logInView.setLogInButtonOnAction(this::handleLogInButtonAction);
    logInView.setCreateButtonOnAction(this::handleCreateButtonAction);
    logInView.setAutoLoginCheckBoxAction(this::handleAutoLoginCheckboxAction);

    appController.registerLogInController(this);
    init();
  }

  private void init() {
    // ping server, if server is ready, determine if auto login was selected
    ServerResponse<Boolean> res = this.logInModel.pingServer();
    System.out.println(res);
    if (!res.getResponse()) {
      logInView.showError(res.getErrorMsg()); // possibly res.errorMsg
      disableLoginUI();
      return;
    }

    // if selected, init the recipe list for user
    if (USER_SETTINGS.isAutoLoginOn()) {
      initRecipeList();
    }
    // if not, stay here, display saved username if there is one
    // String savedUsername = USER_SETTINGS.getUsername();
    // if (savedUsername != null) {
    //   logInView.getUsernameField().setText(savedUsername);
    // }
  }

  private void initRecipeList() {
    appController.loadRecipeList();
    logInView.clearForm();
    appController.changeToRecipeListScene();
  }

  public void showError(String errMsg) {
    logInView.showError(errMsg);
  }

  public void disableLoginUI() {
    logInView.getUsernameField().setDisable(true);
    logInView.getPasswordField().setDisable(true);
    logInView.getSignupButton().setDisable(true);
    logInView.getLoginButton().setDisable(true);
    logInView.getAutoLoginCheckBox().setDisable(true);
  }

  // Log In button event
  private void handleLogInButtonAction(ActionEvent event) {
    String user = logInView.getUsername();
    String pass = logInView.getPassword();
    if (user == null || user.isEmpty()) {
      logInView.showError("Please Enter Username.");
      return;
    }
    if (pass == null || pass.isEmpty()) {
      logInView.showError("Please Enter Password.");
      return;
    }
    // look at the response object
    ServerResponse<Boolean> authRes = logInModel.checkUserPass(user, pass);
    System.out.println(authRes);
    if (authRes.getStatusCode() == 503) {
      appController.handleServerDown();
      return;
    }
    if (authRes.getResponse()) {
      USER_SETTINGS.writeSettingsToFile(
        logInView.autoLoginChecked(),
        logInView.getUsername()
      );
      // clear the form
      logInView.clearForm();
      initRecipeList();
    } else {
      logInView.showError(authRes.getErrorMsg()); // possibly res.errorMsg
    }
  }

  // Create Account Button event
  private void handleCreateButtonAction(ActionEvent event) {
    appController.changeToCreateAccountScene();
  }

  private void handleAutoLoginCheckboxAction(ActionEvent event) {
    String savedUsername = USER_SETTINGS.getUsername();
    if (logInView.autoLoginChecked()) return;
    USER_SETTINGS.writeSettingsToFile(logInView.autoLoginChecked());
  }

  public void handleLogOut() {
    // set auto log in to false
    // clear the form
    USER_SETTINGS.writeSettingsToFile(false);
    logInView.clearForm();
  }
}
