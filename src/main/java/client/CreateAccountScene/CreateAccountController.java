package client.CreateAccountScene;

import client.AppController;
import client.Recipe;
import client.HttpResponse.ServerResponse;

import java.io.File;
import javafx.event.ActionEvent;

public class CreateAccountController {
    CreateAccountView createAccountView;
    CreateAccountModel createAccountModel;
    AppController appController;

    public CreateAccountController(
        CreateAccountView createAccountView,
        CreateAccountModel createAccountModel,
        AppController appController) {
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
        ServerResponse<String> createAccResponse = createAccountModel.performStoreDetails(user, pass);
        // System.out.println("STATUS CODE: " + createAccResponse.getStatusCode());
        // System.out.println("THIS DOESNT WORK SAVE ME");
        // System.out.println("Server Response: " + createAccResponse.getErrorMsg());
        if(createAccResponse.getStatusCode() != 200){
            createAccountView.showError();
            System.out.println("Response: " + createAccResponse.getResponse());
            System.out.println("Error msg: " + createAccResponse.getErrorMsg());
        }
        else{

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
