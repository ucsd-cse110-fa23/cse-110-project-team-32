package client.CreateRecipeScene;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CreateRecipeView {
    private BorderPane borderPane;

    // pane header
    private Button cancelButton;

    // pane center
    private final String mealTypePrompt = "Please record a meal type (breakfast, lunch, dinner)";
    private final String ingredientsPrompt = "Please record the ingredients you have";
    private Text prompt = new Text();
    private Text mealType = new Text();
    private Text ingredients = new Text();
    private Text recordingErrorMsg = new Text();
    private Button startRecordingButton;
    private Button stopRecordingButton;
    private Text recordingToolTip = new Text("Recording...");

    private Button createDummyRecipeButton;

    public CreateRecipeView() {
        borderPane = new BorderPane();

        cancelButton = new Button("Cancel");
        HBox buttonGroup = new HBox(cancelButton);
        
        borderPane.setTop(buttonGroup);

        startRecordingButton = new Button("Start Recording");
        stopRecordingButton = new Button("Stop Recording");
        HBox recordButtonGroup = new HBox(startRecordingButton, stopRecordingButton);
        reset();
        HBox recordedMealType = new HBox(new Text("Recorded Meal Type: "), mealType);
        HBox recordedIngredients = new HBox(new Text("Recorded Ingredients: "), ingredients);
        VBox center = new VBox(prompt, recordedMealType, recordedIngredients, recordingErrorMsg, recordButtonGroup, recordingToolTip);

        borderPane.setCenter(center);

        createDummyRecipeButton = new Button("Create Dummy Recipe");
        // borderPane.setBottom(createDummyRecipeButton);
    }

    public void setCreateDummyRecipeButtonAction (EventHandler<ActionEvent> eventHandler) {
        this.createDummyRecipeButton.setOnAction(eventHandler);
    }

    public BorderPane getBorderPane() {
        return this.borderPane;
    }

    public void setCancelButtonAction(EventHandler<ActionEvent> eventHandler) {
        this.cancelButton.setOnAction(eventHandler);
    }

    public void setStartRecordingButtonAction(EventHandler<ActionEvent> eventHandler) {
        this.startRecordingButton.setOnAction(eventHandler);
    }

    public void setStopRecordingButtonAction(EventHandler<ActionEvent> eventHandler) {
        this.stopRecordingButton.setOnAction(eventHandler);
    }

    public String getMealType() {
        return mealType.getText();
    }

    public String getIngredients() {
        return ingredients.getText();
    }

    public void toggleDisplayToolTip(boolean show) {
        recordingToolTip.setVisible(show);
    }

    public void switchPrompt() {
        prompt.setText(ingredientsPrompt);
    }

    public void setRecordedMealType(String mealType) {
        recordingErrorMsg.setVisible(false);
        this.mealType.setText(mealType);
        this.mealType.setVisible(true);
    }

    public void setRecordedIngredients(String ingredients) {
        recordingErrorMsg.setVisible(false);
        this.ingredients.setText(ingredients);
        this.ingredients.setVisible(true);
    }

    public void setRecordingErrorMsg(String errorMsg) {
        recordingErrorMsg.setText(errorMsg);
        recordingErrorMsg.setVisible(true);
    }

    public void reset() {
        // System.out.println("I'm the logic for reseting the create recipe form");
        // Erase and hide all messages, only show the recording buttons
        prompt.setText(mealTypePrompt);
        mealType.setText("");
        mealType.setVisible(false);
        ingredients.setText("");
        ingredients.setVisible(false);
        recordingErrorMsg.setText("");
        recordingErrorMsg.setVisible(false);
        // recordingToolTip.setText("Recording...");
        recordingToolTip.setVisible(false);
    }
}