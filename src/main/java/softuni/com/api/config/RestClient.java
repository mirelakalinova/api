package softuni.com.api.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import softuni.com.api.app.make.data.dto.ListMakesDto;
import softuni.com.api.app.model.data.dto.ListModelDto;


@FeignClient(name = "apiClient", url = "${api.base}")
public interface RestClient {
	@GetMapping(value = "/GetMakesForVehicleType/car?format=json", produces = "application/json")
	ListMakesDto getAllMakes();
	@GetMapping(value = "/getmodelsformake/*?format=json", produces = "application/json")
	ListModelDto getAllModels();
	

}