package client.LogInScene;

import client.AppController;
import client.Recipe;
import java.io.File;
import javafx.event.ActionEvent;

public class LogInController {
    LogInView logInView;
    LogInModel logInModel;
    AppController appController;

    public LogInController(
     LogInView logInView,
     LogInModel logInModel,
     AppController appController) {
        
        this.logInView = logInView;
        this.logInModel = logInModel;
        this.appController = appController;

        logInView.setLogInButtonOnAction(this::handleLogInButtonAction);
        logInView.setCreateButtonOnAction(this::handleCreateButtonAction);
    }

    // Log In button event
    private void handleLogInButtonAction(ActionEvent event) {
        String user = logInView.getUsername();
        String pass = logInView.getPassword();
        if (logInModel.checkUserPass(user, pass) == true) {
            appController.changeToRecipeListScene();
        }
        else {
            logInView.showError();
            System.out.println("Invalid Username");
            
        }
    }

    // Create Account Button event
    private void handleCreateButtonAction(ActionEvent event) {
        appController.changeToCreateAccountScene();
    }








}
