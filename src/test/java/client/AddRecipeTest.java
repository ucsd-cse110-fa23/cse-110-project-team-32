package client;
import client.RecipeList.RecipeList;
import client.RecipeList.RecipeListView;
import client.RecipeList.RecipeListHeader;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class AddRecipeTest {
    @Test void addRecipeTitle(){
        Recipe recipe = new Recipe("Chicken and Rice", "Dinner", "Its Chicken and Rice man...");
        assertEquals(recipe.getTitle(), "Chicken and Rice");
        // RecipeList lst = new RecipeList();
        // lst.addRecipe(new Recipe("Chicken and Rice", "Dinner", "Its Chicken and Rice man..."));
        // List<RecipeListItem> t = lst.getRecipeList();
        // assertEquals("Chicken and Rice", ((Recipe) lst.getChildren()).getTitle());
    }
    @Test void recipeList(){
        
    }
}
