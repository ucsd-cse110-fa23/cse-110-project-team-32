package client.RecipeListScene;

import java.util.List;

import client.AppController;
import client.Recipe;
import client.RecipeListItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
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
    public RadioMenuItem lastSelectedFilter;
    private ToggleGroup mealTypeToggle;
    private Button sortButton;
    private Button reverseSortButton;
    private AppController appController;
    private Button sortDateButton;
    private Button sortReverseDateButton;
    private MenuButton sortMenuButton;
    private RadioMenuItem breakfastItem;
    private RadioMenuItem lunchItem;
    private RadioMenuItem dinnerItem;
    private RadioMenuItem resetFilter;

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
        sortReverseDateButton = new Button("Sort (reverse date)");
        sortReverseDateButton.getStyleClass().add("textBox");
        // drop down menu for filter
        filterButton = new MenuButton("Filter By");
        filterButton.getStyleClass().add("textBox");
        sortMenuButton = new MenuButton("Sort By");
        sortMenuButton.getStyleClass().add("textBox");
        // setSortButtonEventHandler(appController);
        mealTypeToggle = new ToggleGroup();
        // setSortButtonEventHandler(appController);
        this.breakfastItem = new RadioMenuItem("Breakfast");

        this.breakfastItem.setToggleGroup(mealTypeToggle);
        this.lunchItem = new RadioMenuItem("Lunch");
        this.lunchItem.setToggleGroup(mealTypeToggle);
        this.dinnerItem = new RadioMenuItem("Dinner");
        this.dinnerItem.setToggleGroup(mealTypeToggle);
        this.resetFilter = new RadioMenuItem("Reset Filter");
        this.resetFilter.setToggleGroup(mealTypeToggle);
        MenuItem sortTitleItem = new MenuItem("Title (A-Z)");
        MenuItem reverseSortTitleItem = new MenuItem("Title (Z-A)");
        MenuItem sortDateItem = new MenuItem("Date (Oldest to Newest)");
        MenuItem reverseSortDateItem = new MenuItem("Date (Newest to Oldest)");

        filterButton.getItems().addAll(breakfastItem, lunchItem, dinnerItem, resetFilter);
        sortMenuButton.getItems().clear();

        Region r = new Region();
        Region r1 = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        HBox.setHgrow(r1, Priority.ALWAYS);

        // creates horizontal box for the buttons and adds buttons to button group
        HBox buttonGroup = new HBox(newRecipeButton, r, filterButton, r1, sortMenuButton, logOutButton);
        buttonGroup.setPrefSize(500D, 20);

        // set button group up top
        borderPane.setTop(buttonGroup);
        // creates container for recipe list
        recipeListContainer = new VBox();
        borderPane.setCenter(recipeListContainer);

        initializeFilterSelection();
    }

    public MenuButton getSortMenuButton() {
        return this.sortMenuButton;
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
        // Add event handlers for other sorting buttons (reverseSortButton,
        // sortDateButton, sortReverseDateButton)
        // ...

        // Add menu items event handlers
        MenuItem sortTitleItem = new MenuItem("Title (A-Z)");
        sortTitleItem.setOnAction(event -> {
            appController.sortRecipesByTitle(getSelectedMealType());
        });

        MenuItem reverseSortTitleItem = new MenuItem("Title (Z-A)");
        reverseSortTitleItem.setOnAction(event -> {
            appController.reverseSortRecipesByTitle(getSelectedMealType());
        });

        MenuItem sortDateItem = new MenuItem("Date (Oldest to Newest)");
        sortDateItem.setOnAction(event -> {
            appController.sortRecipesByDate(getSelectedMealType());
        });

        MenuItem reverseSortDateItem = new MenuItem("Date (Newest to Oldest)");
        reverseSortDateItem.setOnAction(event -> {
            appController.reverseSortRecipesByDate(getSelectedMealType());
        });

        sortMenuButton.getItems().addAll(sortTitleItem, reverseSortTitleItem, sortDateItem, reverseSortDateItem);
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
                appController.sortRecipesByDate(getSelectedMealType());
            });
        }
    }

    public void setSortReverseDateButtonEventHandler(AppController appController) {
        if (sortReverseDateButton != null) {
            System.out.println("here");
            sortReverseDateButton.setOnAction(event -> {
                appController.reverseSortRecipesByDate(getSelectedMealType());
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

    public void initializeFilterSelection() {
        mealTypeToggle.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                lastSelectedFilter = (RadioMenuItem) newValue;
            }
        });
    }

    public void resetFilterButton() {
        if (mealTypeToggle != null) {
            mealTypeToggle.selectToggle(null);
        }
        lastSelectedFilter = null;
        this.breakfastItem.setSelected(false);
        this.lunchItem.setSelected(false);
        this.dinnerItem.setSelected(false);
        this.resetFilter.setSelected(false);

    }

    public void handleResetFilterButton() {
        resetFilterButton();
        // Add any additional logic or actions you need after resetting the filter
    }

    public String getSelectedMealType() {
        if (lastSelectedFilter != null && lastSelectedFilter.isSelected()) {
            return lastSelectedFilter.getText();
        }
        return null;
    }

    public void setFilterAction(EventHandler<ActionEvent> eventHandler) {
        if (mealTypeToggle != null) {
            mealTypeToggle.selectedToggleProperty().addListener((observable, oldValue,
                    newValue) -> {
                if (newValue != null) {
                    lastSelectedFilter = (RadioMenuItem) newValue;
                    eventHandler.handle(new ActionEvent());
                }
            });
        }
    }

}