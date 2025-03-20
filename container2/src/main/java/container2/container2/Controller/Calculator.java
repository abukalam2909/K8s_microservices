package container2.container2.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import container2.container2.Model.JsonInputDTO;
import container2.container2.Service.Calculate;

@RestController
public class Calculator{
    @Autowired
    Calculate Calculate;
	@PostMapping
	private String calculator(@RequestBody JsonInputDTO JsonInput){
        return Calculate.calculate(JsonInput);
	}
	
}
