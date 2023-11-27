package client.LogInScene;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.CheckBox;

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

    public LogInView(){
        borderPane = new BorderPane();
        title = new HBox(new Text("Pantry Pal 2"));
        borderPane.setTop(title);
        username = new TextField();
        username.setPromptText("username");
        password = new TextField();
        password.setPromptText("password");
        rememberMe = new CheckBox("Remember Me");
        rememberMe.setAllowIndeterminate(false); //Only care if CheckBox is selected or unselected
        error = new Text("Incorrect Username or Password");
        accountDetails = new VBox(username, password, rememberMe, error);
        borderPane.setCenter(accountDetails);
        logInButton = new Button("Log In");
        createAccButton = new Button("Create Account");
        error.setVisible(false);


        buttonList = new HBox(createAccButton,logInButton);
        borderPane.setBottom(buttonList);
    }

    // getter method for borderpane
    public BorderPane getBorderPane() {
        return this.borderPane;
    }

    public String getUsername(){
        return username.getText();
    }

    public String getPassword(){
        return password.getText();
    }

    public void setCreateButtonOnAction(EventHandler<ActionEvent> eventHandler){
        this.createAccButton.setOnAction(eventHandler);
    }

    public void setLogInButtonOnAction(EventHandler<ActionEvent> eventHandler){
        isChecked();
        this.logInButton.setOnAction(eventHandler);
    }

    public Boolean isChecked(){
        if(rememberMe.isSelected()){
            //TODO: set auto log-in method?
            checked = true;
            System.out.println("Box is Checked!");
            return checked;
        }
        else{
            checked = false;
            return checked; 
        }
    }

    public void showError() {
        error.setVisible(true);
    }
}