package client.RecipeDetailScene;

import client.Recipe;
import client.UserSettings;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class RecipeDetailView {

  private BorderPane borderPane;

  private RecipeContentHolder recipeContentHolder;

  // flags
  private boolean isNewRecipe;
  private boolean isEditing;
  private boolean hasEdited;

  // button group
  private Button saveOrEditButton = new Button();
  private Button backButton = new Button("Back");
  private Button deleteButton = new Button("Delete");
  private Button regenerateButton = new Button("Regenerate");
  private Button shareButton = new Button("Share");
  private Text copyUrlMessage = new Text("Copied url to Clipboard!");
  private FadeTransition messageFadeTrasition;

  private UserSettings USER_SETTINGS = UserSettings.getInstance();

  public RecipeDetailView() {
    borderPane = new BorderPane();

    regenerateButton.managedProperty().bind(regenerateButton.visibleProperty());
    regenerateButton.setVisible(false);
    shareButton.managedProperty().bind(shareButton.visibleProperty());
    shareButton.setVisible(false);

    copyUrlMessage.setVisible(false);
    setMessageFadeTransition();
    VBox header = new VBox();
    HBox buttonGroup = new HBox();
    buttonGroup.setSpacing(15);
    buttonGroup
      .getChildren()
      .addAll(
        regenerateButton,
        saveOrEditButton,
        backButton,
        deleteButton,
        shareButton
      );
    header.getChildren().addAll(buttonGroup, copyUrlMessage);
    buttonGroup.setPrefSize(500, 60); // Size of the header
    buttonGroup.setAlignment(Pos.CENTER);
    borderPane.setTop(header);

    recipeContentHolder = new RecipeContentHolder();
    borderPane.setCenter(recipeContentHolder);
  }

  private void setMessageFadeTransition() {
    this.messageFadeTrasition =
      new FadeTransition(Duration.millis(3000), copyUrlMessage);
    messageFadeTrasition.setFromValue(1);
    messageFadeTrasition.setToValue(0);
    messageFadeTrasition.setAutoReverse(true);
    messageFadeTrasition.setOnFinished(e -> {
      copyUrlMessage.setVisible(false);
    });
    shareButton.setOnAction(e -> {
      // System.out.println("play fade");
      copyUrlMessage.setVisible(true);
      messageFadeTrasition.play();

      String domain = "http://localhost:8100/";
      String endpoint =
        "recipe/?username=%s&recipeID=%s".formatted(
            USER_SETTINGS.getUsername(),
            getRecipe().getRecipeID()
          );
      String str = domain + endpoint;
      Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
      StringSelection strse1 = new StringSelection(str);
      clip.setContents(strse1, strse1);
    });
  }

  public BorderPane getBorderPane() {
    return this.borderPane;
  }

  public boolean isEditing() {
    return isEditing;
  }

  public boolean isNewRecipe() {
    return isNewRecipe;
  }

  public boolean hasEdited() {
    return hasEdited;
  }

  public void renderNewRecipe(Recipe recipe) {
    isNewRecipe = true;
    isEditing = true;
    hasEdited = true;
    saveOrEditButton.setText("Save");
    regenerateButton.setVisible(true);
    shareButton.setVisible(false);
    copyUrlMessage.setVisible(false);
    recipeContentHolder.renderAnotherRecipe(recipe, isEditing);
  }

  public void renderExistingRecipe(Recipe recipe) {
    isNewRecipe = false;
    isEditing = false;
    hasEdited = false;
    saveOrEditButton.setText("Edit");
    regenerateButton.setVisible(false);
    shareButton.setVisible(true);
    recipeContentHolder.renderAnotherRecipe(recipe, isEditing);
  }

  public void setSaveOrEditButtonAction(
    EventHandler<ActionEvent> eventHandler
  ) {
    this.saveOrEditButton.setOnAction(eventHandler);
  }

  public void setBackButtonAction(EventHandler<ActionEvent> eventHandler) {
    this.backButton.setOnAction(eventHandler);
  }

  public void setDeleteButtonAction(EventHandler<ActionEvent> eventHandler) {
    this.deleteButton.setOnAction(eventHandler);
  }

  public void setRegenerateButtonAction(
    EventHandler<ActionEvent> eventHandler
  ) {
    this.regenerateButton.setOnAction(eventHandler);
  }

  // public void setShareButtonAction(EventHandler<ActionEvent> eventHandler) {
  //   this.shareButton.setOnAction(eventHandler);
  // }

  public void updateRecipeDetail() {
    recipeContentHolder.updateRecipeDetail();
  }

  public void switchToEditMode() {
    isEditing = true;
    saveOrEditButton.setText("Save");
    recipeContentHolder.switchToEditMode();
    recipeContentHolder.updateRecipeDetail();
  }

  public void switchToViewOnlyMode() {
    isEditing = false;
    hasEdited = true;
    saveOrEditButton.setText("Edit");
    recipeContentHolder.switchToViewOnlyMode();
  }

  public Recipe getRecipe() {
    return recipeContentHolder.getRecipe();
  }
}

class RecipeContentHolder extends VBox {

  private Text title = new Text();
  private Text mealType = new Text();
  private Text recipeDetail = new Text();
  private TextArea editedRecipeDetail = new TextArea();
  //   private Image recipeImage;
  private ImageView imageView = new ImageView();
  private Recipe recipe;

  public RecipeContentHolder() {
    recipeDetail.managedProperty().bind(recipeDetail.visibleProperty());
    editedRecipeDetail
      .managedProperty()
      .bind(editedRecipeDetail.visibleProperty());
    imageView.setFitWidth(100);
    imageView.setFitHeight(100);

    this.getChildren()
      .addAll(title, mealType, imageView, recipeDetail, editedRecipeDetail);
  }

  public void renderAnotherRecipe(Recipe recipe, boolean isEditing) {
    this.recipe = recipe;
    title.setText(recipe.getTitle());
    mealType.setText(recipe.getMealType());
    recipeDetail.setText(recipe.getRecipeDetail());
    editedRecipeDetail.setText(recipe.getRecipeDetail());
    this.setImage();

    if (isEditing) {
      switchToEditMode();
    } else {
      switchToViewOnlyMode();
    }
  }

  public String getEditedRecipeDetail() {
    return editedRecipeDetail.getText();
  }

  public void updateRecipeDetail() {
    this.recipe.setRecipeDetail(editedRecipeDetail.getText());
    recipeDetail.setText(editedRecipeDetail.getText());
  }

  public void switchToEditMode() {
    recipeDetail.setVisible(false);
    editedRecipeDetail.setVisible(true);
  }

  public void switchToViewOnlyMode() {
    recipeDetail.setVisible(true);
    editedRecipeDetail.setVisible(false);
  }

  public Recipe getRecipe() {
    return recipe;
  }

  public TextArea getRecipeDetailEditTextArea() {
    return editedRecipeDetail;
  }

  private void setImage() {
    try {
      imageView.setImage(
        new Image("data:image/png;base64," + recipe.getImgBase64Str())
      );
    } catch (Exception e) {
      System.out.println(
        "There is no img with this recipe (created in Version 1)"
      );
    }
  }
}
