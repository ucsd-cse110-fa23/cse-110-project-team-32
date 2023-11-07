package client.RecipeList;

import java.util.ArrayList;
import java.util.List;

import client.Recipe;
import client.RecipeDetail.RecipeDetailView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RecipeList extends VBox {
    private List<Recipe> lst;
    private List<RecipeListItem> recipeList;

    public RecipeList() {
        this.setSpacing(5);
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");

        this.recipeList = new ArrayList<>();
        lst = new ArrayList<>();
        /*
         * dummy recipe objects
         * {
         * title: "Recipe 1"
         * recipeDetail: "This is the first recipe"
         * },
         * {
         * title: "Recipe 2"
         * recipeDetail: "second recipe with a lot of lorem ipsum. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur faucibus tellus vitae ex sollicitudin, quis eleifend nibh bibendum. Aliquam sit amet purus dui. Etiam tincidunt urna aliquam, imperdiet dolor id, blandit ante. Morbi porta elit sit amet fringilla egestas. Ut tincidunt lectus auctor ex sollicitudin feugiat. Integer congue tellus vitae nulla ornare, vitae gravida dolor pharetra. In eget dui orci. Nunc vestibulum nibh ac maximus ullamcorper. Integer dignissim accumsan neque vitae ultricies. Nunc tellus dolor, congue commodo orci tempus, lacinia congue leo. Donec fringilla, lectus vitae feugiat finibus, diam mauris egestas orci, in fringilla leo nulla sed velit. Praesent egestas euismod magna sed volutpat. Morbi consequat dignissim erat, sed mattis est suscipit vitae. Phasellus vitae sodales velit. \nPraesent blandit velit vel tristique eleifend. Phasellus ac est neque. Praesent lobortis gravida justo vel mollis. Vestibulum ut neque metus. Maecenas lacus mauris, pellentesque in urna rutrum, scelerisque porta libero. Praesent in rutrum sem. Quisque in ultrices ex. Cras mattis lacinia lacus, in sagittis lectus fermentum nec. Aenean commodo felis in quam semper sollicitudin. Sed ultricies faucibus velit, vitae tincidunt eros ullamcorper in. Pellentesque porta, purus at ultrices pharetra, odio mauris euismod neque, eu vehicula nisl odio id mi. Praesent maximus, erat sed porttitor varius, lorem turpis pellentesque sapien, vel scelerisque neque quam ut metus. Suspendisse tempus, nulla at mollis malesuada, metus est pellentesque ligula, vel elementum arcu odio id purus. Ut porta dictum turpis id commodo. Maecenas quis ex hendrerit, lacinia mi placerat, iaculis quam."
         * }
         */

        // String title1 = "Recipe 1";
        // String recipeDetail1 = "This is the first recipe";
        // String title2 = "Recipe 2";
        // String recipeDetail2 = "second recipe with a lot of lorem ipsum. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur faucibus tellus vitae ex sollicitudin, quis eleifend nibh bibendum. Aliquam sit amet purus dui. Etiam tincidunt urna aliquam, imperdiet dolor id, blandit ante. Morbi porta elit sit amet fringilla egestas. Ut tincidunt lectus auctor ex sollicitudin feugiat. Integer congue tellus vitae nulla ornare, vitae gravida dolor pharetra. In eget dui orci. Nunc vestibulum nibh ac maximus ullamcorper. Integer dignissim accumsan neque vitae ultricies. Nunc tellus dolor, congue commodo orci tempus, lacinia congue leo. Donec fringilla, lectus vitae feugiat finibus, diam mauris egestas orci, in fringilla leo nulla sed velit. Praesent egestas euismod magna sed volutpat. Morbi consequat dignissim erat, sed mattis est suscipit vitae. Phasellus vitae sodales velit. \nPraesent blandit velit vel tristique eleifend. Phasellus ac est neque. Praesent lobortis gravida justo vel mollis. Vestibulum ut neque metus. Maecenas lacus mauris, pellentesque in urna rutrum, scelerisque porta libero. Praesent in rutrum sem. Quisque in ultrices ex. Cras mattis lacinia lacus, in sagittis lectus fermentum nec. Aenean commodo felis in quam semper sollicitudin. Sed ultricies faucibus velit, vitae tincidunt eros ullamcorper in. Pellentesque porta, purus at ultrices pharetra, odio mauris euismod neque, eu vehicula nisl odio id mi. Praesent maximus, erat sed porttitor varius, lorem turpis pellentesque sapien, vel scelerisque neque quam ut metus. Suspendisse tempus, nulla at mollis malesuada, metus est pellentesque ligula, vel elementum arcu odio id purus. Ut porta dictum turpis id commodo. Maecenas quis ex hendrerit, lacinia mi placerat, iaculis quam.";
        // this.recipeList.add(new RecipeListItem(new Recipe(title1, "Dinner", recipeDetail1)));
        // this.recipeList.add(new RecipeListItem(new Recipe(title2, "Lunch", recipeDetail2)));

        for (RecipeListItem recipeListItem : this.recipeList) {
            this.getChildren().add(recipeListItem);
            
        }
        
    }
    public RecipeList(Recipe recipe) {
        this.setSpacing(5);
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");

        this.recipeList = new ArrayList<>();
        lst = new ArrayList<>();
        lst.add(recipe);

    }

    public List<RecipeListItem> getRecipeList() {
        return this.recipeList;
    }

    public void addRecipe(Recipe recipe) {
        RecipeListItem recipeListItem = new RecipeListItem(recipe);
        this.recipeList.add(0, recipeListItem);
        this.getChildren().add(0, recipeListItem);
        lst.add(0, recipe);
    }

    public void addRecipeToList(Recipe recipe){
        // this.getChildren().add(0, );
        lst.add(0, recipe);
    }

    public Recipe getMostRecentRecipe(){
        return lst.get(0);
    }

    public boolean containsRecipe(Recipe recipe){
        return lst.contains(recipe);
    }
}

    class RecipeListItem extends HBox {
    private Recipe recipe;
    private Text title;
    private RecipeDetailView recipeDetailView = RecipeDetailView.getRecipeDetailView();

    RecipeListItem(Recipe recipe) {
        this.recipe = recipe;
        this.title = new Text(this.recipe.getTitle());
        this.setPrefSize(500, 20);
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");
        this.getChildren().add(title);
    }

    public Text getTitle() {
        return this.title;
    }

    public void setToRecipeDetailButtonAction(EventHandler<MouseEvent> eventHandler) {
        this.title.setOnMouseClicked(e -> {
            eventHandler.handle(e);
            recipeDetailView.loadRecipeDetail(this.recipe);
            System.out.println(this.recipe.getRecipeDetail());
        });
    }
}