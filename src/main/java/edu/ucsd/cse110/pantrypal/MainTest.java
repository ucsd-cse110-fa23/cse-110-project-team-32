package main.java.edu.ucsd.cse110.pantrypal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MainTest {
    private String testType1 = "Breakfast";
    
    @Test
    void testMealType() {
        int i = 1;
        int j = 1;
        int ans = i + j;
        assertEquals(2, ans);
        
    }
    
}
