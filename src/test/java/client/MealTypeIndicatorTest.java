package client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class MealTypeIndicatorTest {
    
    @Test void checkMealType(){
        AppController app = new AppController();
        Recipe r = new Recipe("Chicken and Rice", "Dinner", "Chicken, Rice", "img");
        app.addNewRecipeToList(r);
        String recipeMealType = app.getRecipeList().get(app.getRecipeList().indexOf(r)).getMealType();
        assertEquals("Dinner", recipeMealType);
    }

    @Test void checkIncorrectMealType(){
        AppController app = new AppController();
        Recipe r = new Recipe("Chicken and Rice", "Dinner", "Chicken, Rice", "img");
        Recipe r2 = new Recipe("PB and J Sandwich", "Breakfast", "Bread, Peanut Butter, Jam", "img");
        app.addNewRecipeToList(r);
        app.addNewRecipeToList(r2);
        String recipeMealType = app.getRecipeList().get(app.getRecipeList().indexOf(r2)).getMealType();
        assertNotEquals("Dinner", recipeMealType);
        assertNotEquals("Lunch", recipeMealType);
    }
}
