package main.java.edu.ucsd.cse110.pantrypal;
import java.io.*;
import java.util.*;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.layout.VBox;

//Either Dinner, Lunch, Breakfast
class MealType {
    private String type;

    // Constructor for MealTypes
    MealType(String mealtype){
        //Calls on voice activation:
        //Gets meal time from 1st voice activation
        String type = mealtype;
    }

    public String getMealType(){
        return type;
    }
}

class Recipe extends HBox {

    private MealType timeOfMeal;
    private String title;
    private List<String> ingredients;
    private String instructions;

    // Constructor for taking in ChatGPT responses
    Recipe(MealType mealType, String title, List<String> ingredients, String instructions){
        timeOfMeal = mealType;
        this.title = title;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public String getTitle(){
        return this.title;
    }
    
    public List<String> getIngredients(){
        return this.ingredients;
    }

    public String getInstructions(){
        return this.instructions;
    }

}

class RecipeList extends VBox{ 

    RecipeList() {
        this.setSpacing(5); 
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

    public void clearAll(){
        this.getChildren().clear();
    }

}

class Footer extends HBox {
    private Button createButton;

    Footer() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        createButton = new Button("CREATE");
        createButton.setStyle(defaultButtonStyle); 
        this.getChildren().add(createButton);
        this.setAlignment(Pos.CENTER);
    }

    public Button getCreateButton(){
        return this.createButton;
    }
}

class Header extends HBox {
    Header() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        Text titleText = new Text("PantryPal"); 
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); 
    }
}

class AppFrame extends BorderPane{

    private Header header;
    private Footer footer;
    private RecipeList recipeList;
    private ScrollPane scrollBar;
    private Button createButton;

    AppFrame()
    {
        // Initialise the header Object
        header = new Header();

        // Create a tasklist Object to hold the tasks
        recipeList = new RecipeList();
        
        // Initialise the Footer Object
        footer = new Footer();

        // Adds a Scroller to the Task List
        scrollBar = new ScrollPane(recipeList);
        if(recipeList.getChildren().size() < 11){
            scrollBar.setFitToHeight(true);
        }
        scrollBar.setFitToWidth(true);


        // Add header to the top of the BorderPane
        this.setTop(header);
        // Add scroller to the centre of the BorderPane
        this.setCenter(scrollBar); 
        // Add footer to the bottom of the BorderPane
        this.setBottom(footer);

        // Initialise Button Variables through the getters in Footer
        this.createButton = footer.getCreateButton();
        addListeners();
    }

    public void addListeners()
    {

            createButton.setOnAction(e -> {
                //Get Voice Activation Stuff
                //Create Start, Stop --> With Start stop 
                //Add a prompt "Specify Meal Type: Breakfast, Lunch, Dinner"
                //After Meal Type Said(Stop Button)
                //Change Prompt to be: 'List Ingredients'

                //After stop button --> Change buttons to be Save, Cancel
            });


    }
}

public class Main extends Application {

    // To display images
    private ImageView imageView = new ImageView();

    // To open a file dialog for selecting images
    private FileChooser fileChooser = new FileChooser();

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Setting the Layout of the Window- Should contain a Header, Footer and the TaskList
        AppFrame root = new AppFrame();

        // Set the title of the app
        primaryStage.setTitle("PantryPal");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(root, 500, 600));
        // Make window non-resizable
        // primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}