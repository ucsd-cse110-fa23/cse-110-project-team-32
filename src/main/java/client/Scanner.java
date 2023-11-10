package client;
import java.util.*;
import java.io.*;

public class Scanner {
    public List<String> getResourceFiles(String path) {
        List<String> filenames = new ArrayList<>();
    
        try (
                InputStream in = getResourceAsStream(path);
                BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String resource;
    
            while ((resource = br.readLine()) != null) {
                filenames.add(resource);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return filenames;
    }
    
    private InputStream getResourceAsStream(String resource) {
        final InputStream in
                = getContextClassLoader().getResourceAsStream(resource);
    
        return in == null ? getClass().getResourceAsStream(resource) : in;
    }
    
    private ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
