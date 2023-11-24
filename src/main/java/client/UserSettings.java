package client;

import java.io.*;
import java.util.UUID;

// First time users: creates a user id and store it in "user_uuid.txt" file
// If not first time user: gets the uuid previously generated from "user_uuid.txt"
// To retrieve data from sql table

public class UserSettings {

  private static final String SETTINGS_FILE = "user_settings.txt";
  private String username;
  private String password;
  private boolean autoLogin;

  public UserSettings() {
    // Try to read the UUID from the file
    String settings = readUUIDFromFile();

    // If no UUID is stored, generate a new one
    if (settings == null) {
      settings = generateAndStoreUUID();
    }

    String[] settingsItems = settings.split("&");
    this.autoLogin = settingsItems[0].split("=")[1].equals("true");
    this.username = settingsItems[1].split("=")[1];
    this.password = settingsItems[2].split("=")[1];
  }

  public String getUsername() {
    return username;
  }

  private String readUUIDFromFile() {
    try (
      BufferedReader reader = new BufferedReader(new FileReader(SETTINGS_FILE))
    ) {
      return reader.readLine();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private String generateAndStoreUUID() {
    String newUUID = "user_" + UUID.randomUUID().toString();
    try (
      BufferedWriter writer = new BufferedWriter(new FileWriter(SETTINGS_FILE));
    ) {
      writer.write(newUUID);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return newUUID;
  }
}
