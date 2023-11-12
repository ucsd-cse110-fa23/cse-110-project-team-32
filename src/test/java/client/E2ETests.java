// package client;
// import org.junit.jupiter.api.Test;

// import javafx.scene.text.Text;
// import static org.junit.jupiter.api.Assertions.*;
// import java.util.*;
// import java.io.*;
// import javax.sound.sampled.*;
// // import client.mockChatGPT;
// // import client.mockVoiceRecorder;
// // import client.mockWhisper;

// public class E2ETests {

//     @Test void CaitlinUsesApp(){
//     AppController app = new AppController();
//     List<Recipe> currRecipes;
//     mockChatGPT gpt = new mockChatGPT();
//     mockWhisper whisp = new mockWhisper("lol.mp3");
//     mockVoiceRecorder vRec = new mockVoiceRecorder();
//     vRec.setInput("lol.mp3");

//     String audioFile = vRec.getOutput();
//     whisp.setInput(audioFile);
//     String mealType = whisp.getOutput();

//     audioFile = vRec.getOutput();
//     String ingredients = whisp.getOutput();

//     Recipe r1 = gpt.generate(mealType, ingredients);

//     app.addNewRecipeToList(r1);
//     //Checks for Recipe Title
//     currRecipes = app.getRecipeList();
//     assertEquals("Chicken and Rice", r1.getTitle());

//     }
// }
