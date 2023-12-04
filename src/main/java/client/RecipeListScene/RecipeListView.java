package client.RecipeListScene;

import java.util.List;

import client.AppController;
import client.Recipe;
import client.RecipeListItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;

import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;

import javafx.scene.layout.BorderPane;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
    private Button sortButton;
    private Button reverseSortButton;
    private AppController appController;
    private Button sortDateButton;

    // Constructor
    public RecipeListView(AppController appController) {
        borderPane = new BorderPane();
        // create buttons
        this.appController = appController;
        newRecipeButton = new Button("New Recipe");
        newRecipeButton.getStyleClass().add("textBox");
        logOutButton = new Button("Log Out");
        sortButton = new Button("Sort (A-Z)");
        sortButton.getStyleClass().add("textBox");
        reverseSortButton = new Button("Sort (Z-A)");
        reverseSortButton.getStyleClass().add("textBox");
        sortDateButton = new Button("Sort (by Date)");
        sortDateButton.getStyleClass().add("textBox");
        // drop down menu for filter
        filterButton = new MenuButton("Filter By");
        filterButton.getStyleClass().add("textBox");
        setSortButtonEventHandler(appController);
        mealTypeToggle = new ToggleGroup();
        setSortButtonEventHandler(appController);
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
        HBox buttonGroup = new HBox(newRecipeButton, r, filterButton, r1, sortButton, reverseSortButton, sortDateButton,
                logOutButton);
        buttonGroup.setPrefSize(500D, 20);

        // set button group up top
        borderPane.setTop(buttonGroup);
        // creates container for recipe list
        recipeListContainer = new VBox();
        borderPane.setCenter(recipeListContainer);

        // UI Styling (WIP)
        String buttonStyle = "-fx-background-color: #DAE5EA; -fx-border-width: 0;";
        newRecipeButton.setStyle(buttonStyle);
        filterButton.setStyle(buttonStyle);
        sortButton.setStyle(buttonStyle);
       
        reverseSortButton.setStyle(buttonStyle);
       
        sortDateButton.setStyle(buttonStyle);
        
        logOutButton.setStyle(buttonStyle);

        initializeFilterSelection();
    }

    // @param test
    public RecipeListView(boolean test) {
        // constructor for testing purposes
        recipeListContainer = new VBox();
    }

    public void setSortButtonEventHandler(AppController appController) {
        if (sortButton != null) {
            sortButton.setOnAction(event -> {
                appController.sortRecipesByTitle(getSelectedMealType());
            });
        }
    }

    public void setReverseSortButtonEventHandler(AppController appController) {
        if (reverseSortButton != null) {
            reverseSortButton.setOnAction(event -> {
                appController.reverseSortRecipesByTitle(getSelectedMealType());
            });
        }
    }

    public void setSortDateButtonEventHandler(AppController appController) {
        if (sortDateButton != null) {
            sortDateButton.setOnAction(event -> {
                appController.sortRecipesByDate();
            });
        }
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
    public void setLogOutButtonAction(EventHandler<ActionEvent> eventHandler) {
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
