package softuni.com.api.app.Make.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import softuni.com.api.app.Make.data.dto.ListMakeDto;
import softuni.com.api.app.Make.service.MakeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("api")
public class MakeController {
	private final MakeService makeService;
	
	public MakeController(MakeService makeService) {
		this.makeService = makeService;
	}
	
	@GetMapping("/makes")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getAllMakes() {
		
		HashMap<String, Object> response = new HashMap<>();
		try {
			
			List<ListMakeDto> makes = makeService.getAllMakes();
			
			response.put("makes", makes);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(500).body(null);
		}
		
	}
	
	@PostMapping("/delete/make/{id}")
	@ResponseBody
	public ResponseEntity<String> deleteMake(@PathVariable String id) {
		UUID uuid = UUID.fromString(id);
		return ResponseEntity.ok(makeService.deleteMake(uuid));
		
	}
}
