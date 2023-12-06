package client.CreateAccountScene;

import client.TextFieldLowerChar;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import client.TextFieldLowerChar;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.*;

public class CreateAccountView {
    private BorderPane borderPane;
    private Button createAccButton;
    private Button logInButton;
    private HBox title;
    private TextFieldLowerChar username;
    private TextFieldLowerChar password;
    private VBox accountDetails;
    private HBox buttonList;
    private Text error;

    public CreateAccountView(){
        
        borderPane = new BorderPane();

        title = new HBox(new Text("Create Account"));
        borderPane.setTop(title);
        username = new TextFieldLowerChar();
        username.setPromptText("username");
        password = new TextFieldLowerChar();
        password.setPromptText("password");
        // error = new Text("Username already taken");
        error = new Text();
        accountDetails = new VBox(username, password, error);
       
        
        borderPane.setCenter(accountDetails);
        accountDetails.setAlignment(Pos.CENTER); //delete later 
        createAccButton = new Button("Sign Up");
        logInButton = new Button("Back");
        buttonList = new HBox(createAccButton,logInButton);
        borderPane.setBottom(buttonList);
        error.setVisible(false);

            // UI styling
            title.setStyle("-fx-background-color: #F3F3F3; -fx-border-width: 0; -fx-font-weight: bold; -fx-font-size: 24"); 
            accountDetails.setPadding(new Insets(10,0,0,0) );
            accountDetails.setSpacing(10);
            buttonList.setSpacing(15);
            createAccButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
            logInButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");


           
            
        }
  
  // getter method for borderpane
  public BorderPane getBorderPane() {
    return this.borderPane;
  }

  public String getUsername() {
    return username.getText();
  }

  public String getPassword() {
    return password.getText();
  }

  public void setCreateButtonOnAction(EventHandler<ActionEvent> eventHandler) {
    // clearForm();
    this.createAccButton.setOnAction(eventHandler);
  }

  public void setLogInButtonOnAction(EventHandler<ActionEvent> eventHandler) {
    // clearForm();
    this.logInButton.setOnAction(eventHandler);
  }

  public void showError(String errorMsg) {
    error.setText(errorMsg);
    error.setVisible(true);
  }

  public void clearForm() {
    username.setText("");
    password.setText("");
    error.setVisible(false);
  }
}
