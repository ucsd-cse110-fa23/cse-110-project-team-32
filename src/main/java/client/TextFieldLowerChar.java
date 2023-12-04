package client;

import javafx.scene.control.TextField;

public class TextFieldLowerChar extends TextField {

  @Override
  public void replaceText(int start, int end, String text) {
    // If the replaced text would end up being invalid, then simply
    // ignore this call!
    if (text.isEmpty() || text.matches("[a-z0-9]+")) {
      super.replaceText(start, end, text);
    }
  }

  @Override
  public void replaceSelection(String text) {
    if (text.isEmpty() || text.matches("[0-9a-z]+")) {
      super.replaceSelection(text);
    }
  }
}
