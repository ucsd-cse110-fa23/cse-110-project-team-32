package client;

import java.util.List;
import java.util.ArrayList;
import client.CreateRecipeScene.*;
import client.RecipeDetailScene.*;
import client.RecipeListScene.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;
import javax.sound.sampled.*;

public class AppController {
    private Stage stage;
    private RecipeListView recipeListView; // => Rlv
    private VBox recipeListContainer;
    private RecipeListModel recipeListModel;
    private Scene recipeListScene;
    private RecipeDetailView recipeDetailView; // => Rdv
    private RecipeDetailModel recipeDetailModel;
    private Scene recipeDetailScene;
    private CreateRecipeView createRecipeView; // => Crv
    private CreateRecipeModel createRecipeModel;
    private Scene createRecipeScene;
    
    private ChatGPT chatGPT = new ChatGPT();
    private static final String audioFilePath = "recording.wav";
    private Whisper whisper = new Whisper(audioFilePath);
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    

    public AppController(RecipeListView recipeListView, RecipeListModel recipeListModel, Scene recipeListScene,
                      RecipeDetailView recipeDetailView, RecipeDetailModel recipeDetailModel, Scene recipeDetailScene,
                      CreateRecipeView createRecipeView, CreateRecipeModel createRecipeModel, Scene createRecipeScene, Stage stage) {
        this.recipeListView = recipeListView;
        this.recipeListContainer = this.recipeListView.getRecipeListContainer();
        this.recipeListModel = recipeListModel;
        this.recipeListScene = recipeListScene;
        this.recipeDetailView = recipeDetailView;
        this.recipeDetailModel = recipeDetailModel;
        this.recipeDetailScene = recipeDetailScene;
        this.createRecipeView = createRecipeView;
        this.createRecipeModel = createRecipeModel;
        this.createRecipeScene = createRecipeScene;
        this.stage = stage;
        this.audioFormat = this.setUpAudioFormat();

        this.recipeListView.setNewRecipeButtonAction(this::handleRlvNewRecipeButtonAction);

        this.recipeDetailView.setSaveOrEditButtonAction(this::handleRdvSaveOrEditButtonAction);
        this.recipeDetailView.setBackButtonAction(this::handleRdvBackButtonAction);
        this.recipeDetailView.setDeleteButtonAction(this::handleRdvDeleteButtonAction);

        this.createRecipeView.setCancelButtonAction(this::handleCrvCancelButtonAction);
        this.createRecipeView.setStartRecordingButtonAction(this::handleCrvStartRecordingButtonAction);
        this.createRecipeView.setStopRecordingButtonAction(this::handleCrvStopRecordingButtonAction);
        this.createRecipeView.setCreateDummyRecipeButtonAction(this::handleCrvCreateDummyRecipeButtonAction);
    }

    public AppController() {
        // constructor for testing
        this.recipeListView = new RecipeListView(true);
        this.recipeListContainer = recipeListView.getRecipeListContainer();
    }

    public List<Recipe> getRecipeList() {
        // use this function to test
        ObservableList<Node> recipeItemsList = this.recipeListContainer.getChildren();
        List<Recipe> recipeList = new ArrayList<>();
        for (Node c : recipeItemsList) {
            if (c instanceof RecipeListItem) {
                recipeList.add(((RecipeListItem)c).getRecipe());
            }
        }
        return recipeList;
    }

    private void handleRlvNewRecipeButtonAction(ActionEvent event) {
        this.stage.setScene(createRecipeScene);
    }

    private void handleRdvSaveOrEditButtonAction(ActionEvent event) {
        // if button says save and isNewRecipe, then run save recipe logic and exit to list view
        // if button says save but not new recipe, then just save recipe without changing scene
        // if button save edit, run allow edit logic
        boolean recipeDetailViewIsEditing = recipeDetailView.isEditing();
        boolean recipeDetailViewIsNewRecipe = recipeDetailView.isNewRecipe();
        // int recipeDetailViewRecipeIndex = recipeDetailView.getRecipeIndex();

        if (recipeDetailViewIsEditing) {
            recipeDetailView.updateRecipeDetail();
            recipeDetailView.switchToViewOnlyMode();

            if (recipeDetailViewIsNewRecipe) {
                // MAKE A CALL TO MODEL TO ADD A NEW RECIPE
                // add recipe to list
                // and set the whole list's listener? really need to do this? Recipe is a reference
                this.addNewRecipeToList(recipeDetailView.getRecipe());
            }
        } else {
            recipeDetailView.switchToEditMode();
        }
    }

    private void handleRdvBackButtonAction(ActionEvent event) {
        // go back to the recipe list view
        changeToRecipeListScene();
    }

    private void handleRdvDeleteButtonAction(ActionEvent event) {
        // if this is a new recipe, deleting is the same as going back to list,
        // not actually adding the recipe to the recipe list
        if (recipeDetailView.isNewRecipe()) {
            changeToRecipeListScene();
            return;
        }

        // delete the recipe from the VBox recipeList 
        // if child matches (recipeDetailView.getRecipe())
        Recipe currentRecipe = recipeDetailView.getRecipe();
        removeRecipeFromRecipeList(currentRecipe);
        changeToRecipeListScene();
    }

    public void removeRecipeFromRecipeList(Recipe recipe) {
        int indexOfRecipeToRemove = 0;
        ObservableList<Node> recipeListItems = recipeListContainer.getChildren();
        for (; indexOfRecipeToRemove < recipeListItems.size(); indexOfRecipeToRemove++) {
            if (recipeListItems.get(indexOfRecipeToRemove) instanceof RecipeListItem) {
                if (((RecipeListItem)recipeListItems.get(indexOfRecipeToRemove)).getRecipe() == recipe) break;
            }
        }
        recipeListContainer.getChildren().remove(indexOfRecipeToRemove);
        System.out.println("After deleting a recipe, the size of the recipe list is now " + recipeListContainer.getChildren().size());
    }

    private void handleCrvCancelButtonAction(ActionEvent event) {
        // go back to the recipe list view
        changeToRecipeListScene();
    }

    private void handleCrvStartRecordingButtonAction(ActionEvent event) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // the format of the TargetDataLine
                    DataLine.Info dataLineInfo = new DataLine.Info(
                        TargetDataLine.class,
                        audioFormat);
                    // the TargetDataLine used to capture audio data from the microphone
                    targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                    targetDataLine.open(audioFormat);
                    targetDataLine.start();
                    createRecipeView.toggleDisplayToolTip(true);

                    // the AudioInputStream that will be used to write the audio data to a file
                    AudioInputStream audioInputStream = new AudioInputStream(
                        targetDataLine);

                    // the file that will contain the audio data
                    File audioFile = new File(audioFilePath);
                    AudioSystem.write(
                        audioInputStream,
                        AudioFileFormat.Type.WAVE,
                        audioFile);
                    createRecipeView.toggleDisplayToolTip(false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        t.start();
    }

    private void handleCrvStopRecordingButtonAction(ActionEvent event) {
        targetDataLine.stop();
        targetDataLine.close();
        String text = "";
        try {
            text = whisper.translateVoiceToText().toLowerCase();
            text = text.charAt(text.length()-1) == '.' ? text.substring(0, text.indexOf('.')) : text;
            System.out.println("Whisper output: |" + text + "|");

            if (createRecipeView.getMealType() == null || createRecipeView.getMealType().equals("")) {
                // System.out.println("0");
                if (!text.equals("breakfast") && !text.equals("lunch") && !text.equals("dinner")) {
                    // System.out.println("1");
                    createRecipeView.setRecordingErrorMsg("Oops... I couldn't recognize the meal type you said. Please try again :>");
                } else {
                    // System.out.println("2");
                    // System.out.println("recorded: " + text);
                    createRecipeView.switchPrompt();
                    createRecipeView.setRecordedMealType(text);
                }
            } else {
                // System.out.println("3");
                createRecipeView.setRecordedIngredients(text);
                Recipe newRecipe = chatGPT.generate(createRecipeView.getMealType(), createRecipeView.getIngredients(), false);
                changeToRecipeDetailScene(newRecipe, true);
                createRecipeView.reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleCrvCreateDummyRecipeButtonAction(ActionEvent event) {
        Recipe newRecipe = chatGPT.generate("", "", true);
        createRecipeView.reset();
        this.recipeDetailView.renderNewRecipe(newRecipe);
        this.stage.setScene(recipeDetailScene);
    }

    private void changeToRecipeListScene() {
        if (stage != null && recipeListScene != null)
            stage.setScene(recipeListScene);
    }

    private void changeToRecipeDetailScene(Recipe recipe, boolean isNewRecipe) {
        if (isNewRecipe) {
            this.recipeDetailView.renderNewRecipe(recipe);
        } else {
            this.recipeDetailView.renderExistingRecipe(recipe);
        }
        this.stage.setScene(recipeDetailScene);
    }

    public void addNewRecipeToList(Recipe recipe) {
        // can use this to 
        RecipeListItem recipeListItem = new RecipeListItem(recipe);
        recipeListItem.setOnMouseClicked(e -> {
            // the next time to render the detail of this recipe, this recipe would be existing
            this.changeToRecipeDetailScene(recipe, false);
        });
        recipeListContainer.getChildren().add(0, recipeListItem);
        changeToRecipeListScene();
    }

    private AudioFormat setUpAudioFormat() {
        // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;
        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;
        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;
        // whether the data is signed or unsigned.
        boolean signed = true;
        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;
        return new AudioFormat(
            sampleRate,
            sampleSizeInBits,
            channels,
            signed,
            bigEndian);
    }
}


/*
generated recipes are automatically in edit mode
Recipe original: null
Recipe updating: {values from GPT}

In DetailView: 
boolean isNewRecipe;
boolean inEditMode; init false for existing recipe, true for new recipe
    if true: edit/save button says save, recipe body is TextField
    if false: edit/save button says edit, recipe body is Text

Back button actions:
    if the Recipe original is not null:
        simply exit to list view
    if the Recipe original is null:
        this means the current recipe is newly generated and is placed on the recipe list
        execute delete logic on that recipe
        exit to list view
Edit button actions:
    on

https://piazza.com/class/lmy9axhgowe53s/post/174
10. Should the user be able to edit the generated recipe, or does editing only entail making changes to their prompt?
The user should be able to edit the generated recipe. They won’t be able to edit their prompt (meal type or ingredients).

take the user from add recipe straight to recipe detail page

From e2e story
After editng and clicking save, the app stays in the detail view
this means: the app stays in detail view unless user click back or delete

Caitlin clicks the "Save" button to add it to her collection. 
The new recipe now takes the top spot in her list
This means clicking save after new recipe generation exits to the list view

https://piazza.com/class/lmy9axhgowe53s/post/164
1. Once Caitlyn makes a recipe, what happens if she does not want to save it?
She can just exit out of the detailed view.

2.If Caitlyn is editing a recipe and doesn’t want to save her changes, what should she do?
She can just exit out of the edit view.

3. Before recipes are deleted, should there be a confirmation message/interface for the user?
Sure, that would be a good way to handle deletion in case the user accidentally clicks on delete.
*/