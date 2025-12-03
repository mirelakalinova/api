package softuni.com.api.app.model.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import softuni.com.api.app.model.data.dto.SaveModelDto;
import softuni.com.api.app.model.service.ModelService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("api")
@Slf4j
public class ModelController {
	
	private static final Logger log = LoggerFactory.getLogger(ModelController.class);
	private final ModelService modelService;
	
	public ModelController(ModelService modelService) {
		this.modelService = modelService;
	}
	
	@GetMapping("/models/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getAllModelsByMakeId(@PathVariable String id) {
		log.info("Attempt to get all models by make  from api/models/{} ..", id);
		UUID uuid = UUID.fromString(id);
		Map<String, Object> response = new HashMap<>();
		
		response.put("models", modelService.getAllModelsByMakeId(uuid));
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/models/all")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getAllModelsWithMakes() {
		log.info("Attempt to get all models from api/models/all..");
		
		Map<String, Object> response = new HashMap<>();
		
		response.put("models", modelService.getAllModelsWithMakes());
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/save")
	@ResponseBody
	public ResponseEntity<HashMap<String,String>> createMakeModel(@RequestBody SaveModelDto saveMakeModelDto) {
		log.info("Attempt to save model {} with make {} ..", saveMakeModelDto.getModelName(), saveMakeModelDto.getMakeName());
		
		String make = saveMakeModelDto.getMakeName();
		String model = saveMakeModelDto.getModelName();
		HashMap<String,String> result = modelService.saveMakeWithModel(make, model);
		return ResponseEntity.ok(result);
		
	}
	
	@PostMapping("/delete/model/{id}")
	@ResponseBody
	public ResponseEntity<String> deleteModel(@PathVariable String id) {
		log.info("Attempt to delete model from /api/delete/model/{}", id);
		UUID uuid = UUID.fromString(id);
		return ResponseEntity.ok(modelService.deleteModel(uuid));
		
	}
	
}
