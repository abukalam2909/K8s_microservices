package container2.container2.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import container2.container2.Model.JsonInputDTO;

@Service
public class Calculate {
    public String calculate(JsonInputDTO JsonInput){
        // Create an ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        // Create a JSON object
        ObjectNode json = mapper.createObjectNode();

		String file = JsonInput.getFile();
		String product = JsonInput.getProduct();

		boolean isCsv = true;
		int expectedFieldCount = -1;
		String filePath = "/AbuKalamBabuji_PV_dir/" + file;

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
			json.put("file", file);
			String line;
			int lineNumber = 0;
			int sum = 0;
			while ((line = br.readLine()) != null) {
                lineNumber++;
                String[] fields = line.split("\\s*,\\s*");
                if (lineNumber == 1) {
                    expectedFieldCount = fields.length;
                } else if (fields.length == 1 || line.contains(":") || fields.length != expectedFieldCount) {
                    isCsv = false;
                    break;
                } else if (fields[0].equals(product)) {
                    sum = sum + Integer.parseInt(fields[1]);
                }
            }
			if(lineNumber == 1 && line == null)
				isCsv = false;
			if(isCsv == true){
				json.put("sum", sum);
			}
			else{
				json.put("error", "Input file not in CSV format.");
			}
		}
		catch (IOException e) {}

		return json.toString();
    }
}
