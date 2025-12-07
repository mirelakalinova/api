package softuni.com.api.app.make.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import softuni.com.api.app.make.data.dto.ListMakeDto;
import softuni.com.api.app.make.service.MakeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("api")
@Slf4j
public class MakeController {
	private final MakeService makeService;
	
	public MakeController(MakeService makeService) {
		this.makeService = makeService;
	}
	
	@GetMapping("/makes")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getAllMakes() {
		log.info("Attempt to get all makes from api/makes..");
		HashMap<String, Object> response = new HashMap<>();
		try {
			
			List<ListMakeDto> makes = makeService.getAllMakes();
			
			response.put("makes", makes);
			log.info("Successfully get {}  makes from api/makes..", makes.size());
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			log.error("Return error 500 status code when get all makes from api/makes..");
			return ResponseEntity.status(500).body(null);
		}
		
	}
	
	@PostMapping("/delete/make/{id}")
	@ResponseBody
	public ResponseEntity<HashMap<String,String>> deleteMake(@PathVariable String id) {
		log.info("Attempt to delete make from /api/delete/make/{}", id);
		UUID uuid = UUID.fromString(id);
		return ResponseEntity.ok(makeService.deleteMake(uuid));
		
	}
}
