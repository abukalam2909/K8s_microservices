package container1.container1.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import container1.container1.Model.StoreFileInputDTO;

@Service
public class Store {
    public String store(StoreFileInputDTO storeFileInput) {
        // Create an ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        String NULL = null;
        
        // Validate filename
        String fileName = storeFileInput.getFile();
        if (fileName == null || fileName.trim().isEmpty()) {
            json.put("file", NULL);
            json.put("error", "Invalid JSON input.");
            return json.toString();
        }
        
        String fileContents = storeFileInput.getData();
        String directoryPath = "/AbuKalamBabuji_PV_dir/";
        String filePath = directoryPath + fileName;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            if (fileContents != null) {
                String[] lineContent = fileContents.split("\n");
                for (String line : lineContent) {
                    writer.write(line + "\n");
                }
            }
            
            json.put("file", fileName);
            json.put("message", "Success.");
        } catch (IOException e) {
            json.put("file", fileName);
            json.put("error", "Error while storing the file to the storage.");
        }

        return json.toString();
    }
}