package client.LogInScence;
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
        accountDetails = new VBox(username, password, rememberMe);
        borderPane.setCenter(accountDetails);
        logInButton = new Button("Log In");
        createAccButton = new Button("Create Account");

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

    public void isChecked(){
        if(rememberMe.isSelected()){
            //TODO: set auto log-in method?
            System.out.println("Box is Checked!");
        }
        else{
            return; 
        }
    }
}
