package client.CreateRecipeScene;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

// View class for creating a recipe
public class CreateRecipeView {
    private BorderPane borderPane;

    // Header element
    private Button cancelButton;

    // Center elements (recording and prompts)
    private final String mealTypePrompt = "Please record a meal type (breakfast, lunch, dinner)";
    private final String ingredientsPrompt = "Please record the ingredients you have";
    private Text prompt = new Text();
    private Text mealType = new Text();
    private Text ingredients = new Text();
    private Text recordingErrorMsg = new Text();
    private Button startRecordingButton;
    private Button stopRecordingButton;
    private Text recordingToolTip = new Text("Recording...");
    
    // Button for logging out
    private Button logOutButton;

    // Bottom elements (create recipe)
    private Button createDummyRecipeButton;

    // Constructor for initializing the view
    public CreateRecipeView() {
        borderPane = new BorderPane();

        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        // Header element initialization (cancel button)
        cancelButton = new Button("Cancel");
        // Places Log Out on Bottom of view
        logOutButton = new Button("Log Out");

        HBox buttonGroup = new HBox(cancelButton, r, logOutButton);
        buttonGroup.setPrefSize(500D,20);
    
        borderPane.setTop(buttonGroup);

        // Center elements intialization (recording and prompts)
        startRecordingButton = new Button("Start Recording");
        stopRecordingButton = new Button("Stop Recording");
        HBox recordButtonGroup = new HBox(startRecordingButton, stopRecordingButton);
        recordButtonGroup.setSpacing(10);
        reset();
        HBox recordedMealType = new HBox(new Text("Recorded Meal Type: "), mealType);
        HBox recordedIngredients = new HBox(new Text("Recorded Ingredients: "), ingredients);
        VBox center = new VBox(prompt, recordedMealType, recordedIngredients, recordingErrorMsg, recordButtonGroup, recordingToolTip);
        borderPane.setCenter(center);

        // Bottom elements initialization (create recipe)
        createDummyRecipeButton = new Button("Create Dummy Recipe");
        borderPane.setBottom(createDummyRecipeButton);

        

    }

    // setter method for creating a recipe 
    public void setCreateDummyRecipeButtonAction (EventHandler<ActionEvent> eventHandler) {
        this.createDummyRecipeButton.setOnAction(eventHandler);
    }

    // getter method for borderpane
    public BorderPane getBorderPane() {
        return this.borderPane;
    }

    // setter method for cancel button
    public void setCancelButtonAction(EventHandler<ActionEvent> eventHandler) {
        this.cancelButton.setOnAction(eventHandler);
    }

    // setter method for start recording button
    public void setStartRecordingButtonAction(EventHandler<ActionEvent> eventHandler) {
        this.startRecordingButton.setOnAction(eventHandler);
    }
    // setter method for stop recording button
    public void setStopRecordingButtonAction(EventHandler<ActionEvent> eventHandler) {
        this.stopRecordingButton.setOnAction(eventHandler);
    }

    // setter method for "Log Out" button
    public void setLogOutButtonAction(EventHandler<ActionEvent> eventHandler){
        this.logOutButton.setOnAction(eventHandler);
    }

    // getter method for recorded meal type
    public String getMealType() {
        return mealType.getText();
    }

    // getter method for recorded ingredients
    public String getIngredients() {
        return ingredients.getText();
    }

    // display the recording tool tip
    public void toggleDisplayToolTip(boolean show) {
        recordingToolTip.setVisible(show);
    }

    // switches prompt from meal type to ingredients
    public void switchPrompt() {
        prompt.setText(ingredientsPrompt);
    }

    // setter method for recorded meal type
    public void setRecordedMealType(String mealType) {
        recordingErrorMsg.setVisible(false);
        this.mealType.setText(mealType);
        this.mealType.setVisible(true);
    }

    // setter method for recorded ingredients
    public void setRecordedIngredients(String ingredients) {
        recordingErrorMsg.setVisible(false);
        this.ingredients.setText(ingredients);
        this.ingredients.setVisible(true);
    }

    // setter method for recording error messages
    public void setRecordingErrorMsg(String errorMsg) {
        recordingErrorMsg.setText(errorMsg);
        recordingErrorMsg.setVisible(true);
    }

    // Reset view elements
    public void reset() {
        prompt.setText(mealTypePrompt);
        mealType.setText("");
        mealType.setVisible(false);
        ingredients.setText("");
        ingredients.setVisible(false);
        recordingErrorMsg.setText("");
        recordingErrorMsg.setVisible(false);
        recordingToolTip.setVisible(false);
    }




}