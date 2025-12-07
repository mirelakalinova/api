package softuni.com.api.app.model.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import softuni.com.api.app.model.data.dto.SaveModelDto;
import softuni.com.api.app.model.service.ModelService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("api")
@Slf4j
public class ModelController {
	
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
	public ResponseEntity<?> createMakeModel(@Valid @RequestBody SaveModelDto saveMakeModelDto,
	                                                               BindingResult bindingResult) {
		log.info("Attempt to save model {} with make {} ..", saveMakeModelDto.getModelName(), saveMakeModelDto.getMakeName());
		if (bindingResult.hasErrors()) {
			List<String> errors = bindingResult.getFieldErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage)
					.toList();
			
			HashMap<String, Object> body = new HashMap<>();
			body.put("errors", errors);
			
			return ResponseEntity.badRequest().body(body);
			
		}
		HashMap<String, String> result = modelService.saveMakeWithModel(saveMakeModelDto);
		return ResponseEntity.ok(result);
		
		
	}
	
	@PostMapping("/delete/model/{id}")
	@ResponseBody
	public ResponseEntity<HashMap<String, String>> deleteModel(@PathVariable String id) {
		log.info("Attempt to delete model from /api/delete/model/{}", id);
		UUID uuid = UUID.fromString(id);
		return ResponseEntity.ok(modelService.deleteModel(uuid));
		
	}
	
}
