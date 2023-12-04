package client.LogInScene;

import client.TextFieldLowerChar;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LogInView {

  private BorderPane borderPane;
  private Button createAccButton;
  private Button logInButton;
  private HBox title;
  private TextField username;
  private TextField password;
  private VBox accountDetails;
  private HBox buttonList;
  private CheckBox rememberMe;
  private Boolean checked;
  private Text error;

  //Remember me checkbox

  public LogInView() {
    borderPane = new BorderPane();
    title = new HBox(new Text("Pantry Pal 2"));
    borderPane.setTop(title);
    username = new TextFieldLowerChar();
    username.setPromptText("username");
    password = new TextFieldLowerChar();
    password.setPromptText("password");
    rememberMe = new CheckBox("Auto Login");
    rememberMe.setAllowIndeterminate(false); //Only care if CheckBox is selected or unselected
    error = new Text("Incorrect Username or Password");
    accountDetails = new VBox(username, password, rememberMe, error);
    borderPane.setCenter(accountDetails);
    logInButton = new Button("Log In");
    createAccButton = new Button("Create Account");
    error.setVisible(false);

    buttonList = new HBox(createAccButton, logInButton);
    borderPane.setBottom(buttonList);
  }

  // getter method for borderpane
  public BorderPane getBorderPane() {
    return this.borderPane;
  }

  public TextField getUsernameField() {
    return this.username;
  }

  public TextField getPasswordField() {
    return this.password;
  }

  public Button getSignupButton() {
    return this.createAccButton;
  }

  public Button getLoginButton() {
    return this.logInButton;
  }

  public CheckBox getAutoLoginCheckBox() {
    return this.rememberMe;
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

  public void setAutoLoginCheckBoxAction(
    EventHandler<ActionEvent> eventHandler
  ) {
    this.rememberMe.setOnAction(eventHandler);
  }

  public Boolean autoLoginChecked() {
    return rememberMe.isSelected();
  }

  public void showError(String err) {
    error.setText(err);
    error.setVisible(true);
  }

  public void clearForm() {
    username.setText("");
    password.setText("");
    rememberMe.setSelected(false);
    error.setVisible(false);
  }
}
