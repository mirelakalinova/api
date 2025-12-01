package softuni.com.api.app.Model.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import softuni.com.api.app.Model.data.dto.SaveModelDto;
import softuni.com.api.app.Model.service.ModelService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("api")
public class ModelController {
	
	private final ModelService modelService;
	
	public ModelController(ModelService modelService) {
		this.modelService = modelService;
	}
	
	@GetMapping("/models/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getAllModelsByMakeId(@PathVariable String id) {
		UUID uuid = UUID.fromString(id);
		Map<String, Object> response = new HashMap<>();
		
		response.put("models", modelService.getAllModelsByMakeId(uuid));
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/models/all")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getAllModelsWithMakes() {
		
		Map<String, Object> response = new HashMap<>();
		
		response.put("models", modelService.getAllModelsWithMakes());
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/save")
	@ResponseBody
	public ResponseEntity<String> createMakeModel(@RequestBody SaveModelDto saveMakeModelDto) {
		String make = saveMakeModelDto.getMakeName();
		String model = saveMakeModelDto.getModelName();
		String result = modelService.saveMakeWithModel(make, model);
		return ResponseEntity.ok(result);
		
	}
	
	@PostMapping("/delete/model/{id}")
	@ResponseBody
	public ResponseEntity<String> deleteModel(@PathVariable String id) {
		UUID uuid = UUID.fromString(id);
		return ResponseEntity.ok(modelService.deleteModel(uuid));
		
	}
	
}
