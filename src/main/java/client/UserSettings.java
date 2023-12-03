package client;

import java.io.*;

public class UserSettings {

  private static final String SETTINGS_FILE = "user_settings.txt";
  private String username;
  private boolean autoLogin;

  public UserSettings() {
    // Try to read the UUID from the file
    String settings = readSettingsFromFile();

    // If no UUID is stored, generate a new one
    if (settings == null) {
      this.autoLogin = false;
      this.username = null;
    } else {
      String[] settingsItems = settings.split("&");
      this.autoLogin = settingsItems[0].split("=")[1].equals("true");
      this.username = settingsItems[1].split("=")[1];
    }
  }

  public String getUsername() {
    return username;
  }

  public boolean isAutoLoginOn() {
    return this.autoLogin;
  }

  private String readSettingsFromFile() {
    try (
      BufferedReader reader = new BufferedReader(new FileReader(SETTINGS_FILE))
    ) {
      return reader.readLine();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}