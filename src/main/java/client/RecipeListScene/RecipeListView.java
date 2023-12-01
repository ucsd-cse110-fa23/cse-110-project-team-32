package client.RecipeListScene;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

// View class for recipe list
public class RecipeListView {
    // Main layout
    private BorderPane borderPane;
    // Container to hold content (list of recipes and buttons)
    private VBox recipeListContainer;
    // Button to add new recipe
    private Button newRecipeButton;
    // Button to log out
    private Button logOutButton;
    // Button to filter by meal type
    private MenuButton filterButton;
    private RadioMenuItem lastSelectedFilter;
    private ToggleGroup mealTypeToggle;
    
    // Constructor
    public RecipeListView() {
        borderPane = new BorderPane();
        // create buttons
        newRecipeButton = new Button("New Recipe");
        newRecipeButton.getStyleClass().add("textBox");
        logOutButton = new Button("Log Out");
        
        // drop down menu for filter
        filterButton = new MenuButton("Filter By");
        filterButton.getStyleClass().add("textBox");
        
        mealTypeToggle = new ToggleGroup();

        RadioMenuItem breakfastItem = new RadioMenuItem("Breakfast");
        breakfastItem.setToggleGroup(mealTypeToggle);
        RadioMenuItem lunchItem = new RadioMenuItem("Lunch");
        lunchItem.setToggleGroup(mealTypeToggle);
        RadioMenuItem dinnerItem = new RadioMenuItem("Dinner");
        dinnerItem.setToggleGroup(mealTypeToggle);
        RadioMenuItem resetFilter = new RadioMenuItem("Reset Filter");
        resetFilter.setToggleGroup(mealTypeToggle);

        filterButton.getItems().addAll(breakfastItem, lunchItem, dinnerItem, resetFilter);

        Region r = new Region();
        Region r1 = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        HBox.setHgrow(r1, Priority.ALWAYS);

        // creates horizontal box for the buttons and adds buttons to button group
        HBox buttonGroup = new HBox(newRecipeButton, r,  filterButton, r1,  logOutButton);
        buttonGroup.setPrefSize(500D,20);

        // set button group up top
        borderPane.setTop(buttonGroup);
        // creates container for recipe list
        recipeListContainer = new VBox();
        borderPane.setCenter(recipeListContainer);
        
        initializeFilterSelection();
    }

    // @param test
    public RecipeListView(boolean test) {
        // constructor for testing purposes
        recipeListContainer = new VBox();
    }

    // Gets the main layout of recipe list view
    public BorderPane getBorderPane() {
        return this.borderPane;
    }

    // gets the container for recipe list
    public VBox getRecipeListContainer() {
        return this.recipeListContainer;
    }

    // Setter for action when new recipe button is clicked (set to an event)
    public void setNewRecipeButtonAction(EventHandler<ActionEvent> eventHandler) {
        this.newRecipeButton.setOnAction(eventHandler);
    }

    // setter method for "Log Out" button
    public void setLogOutButtonAction(EventHandler<ActionEvent> eventHandler){
        this.logOutButton.setOnAction(eventHandler);
    }

    private void initializeFilterSelection() {
        mealTypeToggle.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                lastSelectedFilter = (RadioMenuItem) newValue;
            }
        });
    }

    public String getSelectedMealType() {
        if (lastSelectedFilter != null && lastSelectedFilter.isSelected()) {
            return lastSelectedFilter.getText();
        }
        return null;
    }

    public void setFilterAction(EventHandler<ActionEvent> eventHandler) {
        if (mealTypeToggle != null) {
            mealTypeToggle.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    lastSelectedFilter = (RadioMenuItem) newValue;
                    eventHandler.handle(new ActionEvent());
                }
            });
        }
    }
}
     

