package client;
import java.io.*;
import java.util.UUID;

// First time users: creates a user id and store it in "user_uuid.txt" file
// If not first time user: gets the uuid previously generated from "user_uuid.txt"
// To retrieve data from sql table

public class UserIdGetter {
    private static final String UUID_FILE = "user_uuid.txt";
    private String userId;

    public UserIdGetter () {
        // Try to read the UUID from the file
        String storedUUID = readUUIDFromFile();

        // If no UUID is stored, generate a new one
        if (storedUUID == null) {
            storedUUID = generateAndStoreUUID();
        }

        userId = storedUUID;
    }

    public String getUserID() {
        return userId;
    }

    private String readUUIDFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(UUID_FILE))) {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String generateAndStoreUUID() {
        String newUUID = "user_" + UUID.randomUUID().toString();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(UUID_FILE))) {
            writer.write(newUUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newUUID;
    }
}
