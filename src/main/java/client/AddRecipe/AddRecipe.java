package client.AddRecipe;

import client.Recipe;
import client.RecipeList.RecipeList;
import client.RecipeList.RecipeListView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AddRecipe extends VBox {
    private Input titleInput;
    private Input mealTypeInput;
    private Input ingredientsInput;
    private ChatGPT chatGPT = new ChatGPT();
    private Recipe newRecipe;

    public AddRecipe() {
        setUpAddRecipeForm();
    }

    private void setUpAddRecipeForm() {
        this.titleInput = new Input("Title: ");
        this.mealTypeInput = new Input("Meal Type: ");
        this.ingredientsInput = new Input("Ingredients : ");
        Button generateRecipeButton = new Button("Generate Recipe");
        generateRecipeButton.setOnAction(e -> {
            try {
            this.newRecipe = this.chatGPT.generate(getTitle(), getMealType(), getIngredientsDetail());
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
            
        });
        this.getChildren().addAll(this.titleInput, this.mealTypeInput, this.ingredientsInput, generateRecipeButton);
    }

    public Recipe createNewRecipe() {
        // Recipe recipe = new Recipe(getTitle(), getMealType(), getRecipeDetail());
        // clearForm();
        // setUpAddRecipeForm();
        return this.newRecipe;
    }

    private void clearForm() {
        this.getChildren().clear();
    }

    private String getTitle() {
        return this.titleInput.getInput();
    }

    private String getMealType() {
        return this.mealTypeInput.getInput();
    }

    private String getIngredientsDetail() {
        return this.ingredientsInput.getInput();
    }
}

class Input extends HBox {
    private Text label;
    private TextField inputField;
    public Input(String label) {
        this.label = new Text(label);
        this.inputField = new TextField();
        this.getChildren().addAll(this.label, this.inputField);
    }
    public String getInput() {
        return this.inputField.getText();
    }
}
