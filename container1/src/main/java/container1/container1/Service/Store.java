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
    public String store(StoreFileInputDTO StoreFileInput){
        // Create an ObjectMapper
        ObjectMapper mapper = new ObjectMapper();

        // Create a JSON object
        ObjectNode json = mapper.createObjectNode();
		String fileName = StoreFileInput.getFile();
        String fileContents = StoreFileInput.getData();

        String directoryPath = "/AbuKalamBabuji_PV_dir/";
        String filePath = directoryPath + fileName;

        String[] lineContent = fileContents.split("\n");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))){
            for(String line : lineContent){
                writer.write(line+"\n");
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

        json.put("file", fileName);
        json.put("message", "Success.");

        return json.toString();
    }
}
