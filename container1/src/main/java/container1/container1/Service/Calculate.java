package container1.container1.Service;

import java.io.File;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import container1.container1.Model.JsonInputDTO;

@Service
public class Calculate {
    private final RestTemplate restTemplate = new RestTemplate();
    public String calculate(JsonInputDTO JsonInput){
        // Create an ObjectMapper
        ObjectMapper mapper = new ObjectMapper();

        // Create a JSON object
        ObjectNode json = mapper.createObjectNode();
		String fileName = JsonInput.getFile();
		String directoryPath = "/AbuKalamBabuji_PV_dir";
		File directory = new File(directoryPath);
		json.put("file", JsonInput.getFile());
		if (fileName == null || fileName.isEmpty()) {
            fileName = null;
			json.put("file", fileName);
            json.put("error", "Invalid JSON input.");
			return json.toString();
        }
		else if (directory.exists() && directory.isDirectory()){
			File file = new File(directory, fileName);
			if (file.exists() && file.isFile()) {
				json.put("product", JsonInput.getProduct());
			}
			else{
				json.put("error", "File not found.");
				return json.toString();
			}
		}
		String targetUrl = "http://"+ System.getenv("COMPUTE_SERVICE_HOST")+ ":"+ System.getenv("COMPUTE_SERVICE_PORT");
		String response = restTemplate.postForObject(targetUrl, json, String.class);
        return response;
    }
}
