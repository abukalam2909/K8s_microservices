package container1.container1.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import container1.container1.Model.JsonInputDTO;
import container1.container1.Model.StoreFileInputDTO;
import container1.container1.Service.Calculate;
import container1.container1.Service.Store;

@RestController
@RequestMapping
public class Validator {

	@Autowired
	private Store Store;

	@PostMapping("/store-file")
	public String storeFile(@RequestBody StoreFileInputDTO StoreFileInput){
		return Store.store(StoreFileInput);
	}

	@Autowired
    private Calculate Calculate;

	@PostMapping("/calculate")
	public String validate(@RequestBody JsonInputDTO JsonInput){
		return Calculate.calculate(JsonInput);
	}
}
