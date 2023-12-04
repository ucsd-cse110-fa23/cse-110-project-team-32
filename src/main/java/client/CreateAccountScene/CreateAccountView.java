package client.CreateAccountScene;

import client.TextFieldLowerChar;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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

  public CreateAccountView() {
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
    createAccButton = new Button("Create Account");
    logInButton = new Button("Log In");
    buttonList = new HBox(createAccButton, logInButton);
    borderPane.setBottom(buttonList);
    error.setText("TEST");
    error.setVisible(false);
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
    this.createAccButton.setOnAction(eventHandler);
  }

  public void setLogInButtonOnAction(EventHandler<ActionEvent> eventHandler) {
    this.logInButton.setOnAction(eventHandler);
  }

  public void showError(String errorMsg) {
    error.setText(errorMsg);
    error.setVisible(true);
  }
}
