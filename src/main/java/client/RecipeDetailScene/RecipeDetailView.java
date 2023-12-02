package client.RecipeDetailScene;

import client.Recipe;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
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

  public RecipeDetailView() {
    borderPane = new BorderPane();

    regenerateButton.managedProperty().bind(regenerateButton.visibleProperty());
    regenerateButton.setVisible(false);
    HBox buttonGroup = new HBox();
    buttonGroup.setSpacing(15);
    buttonGroup
      .getChildren()
      .addAll(regenerateButton, saveOrEditButton, backButton, deleteButton);
    buttonGroup.setPrefSize(500, 60); // Size of the header
    buttonGroup.setAlignment(Pos.CENTER);
    borderPane.setTop(buttonGroup);

    recipeContentHolder = new RecipeContentHolder();
    borderPane.setCenter(recipeContentHolder);
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
    recipeContentHolder.renderAnotherRecipe(recipe, isEditing);
  }

  public void renderExistingRecipe(Recipe recipe) {
    isNewRecipe = false;
    isEditing = false;
    hasEdited = false;
    saveOrEditButton.setText("Edit");
    regenerateButton.setVisible(false);
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
        new Image(new File(recipe.getImgPath()).toURI().toString())
      );
    } catch (Exception e) {
      System.out.println(
        "There is no img with this recipe (created in Version 1)"
      );
    }
  }
}
