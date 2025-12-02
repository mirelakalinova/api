package softuni.com.api.make;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import softuni.com.api.app.make.data.entity.Make;
import softuni.com.api.app.make.repo.MakeRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class MakeIT {
	
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private MakeRepository makeRepository;
	private Make makeFirst;
	private Make makeSecond;
	
	
	@BeforeEach
	void setUp() {
		
		
		makeFirst = new Make();
		makeFirst.setMakeId(123);
		makeFirst.setName("Test First");
		
		
		makeSecond = new Make();
		makeSecond.setName("Test Second");
		makeSecond.setMakeId(125);
		
	}
	
	@Test
	void getAllMakes_shouldReturnListOfMakes() throws Exception {
		mockMvc.perform(get("/api/makes")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.makes").isArray())
				.andExpect(jsonPath("$.makes.length()").isNotEmpty())
				.andExpect(jsonPath("$.makes[0].name").exists());
	}
	
	
	@Test
	void deleteMake_whenExists_returnsOkAndCallsService() throws Exception {
		Optional<Make> make = makeRepository.findAll().stream().findAny();
		mockMvc.perform(post("/api/delete/make/{id}", make.get().getId().toString())
						.param("id", String.valueOf(make.get().getId()))
						.contentType(MediaType.APPLICATION_JSON)
				
				)
				.andExpect(status().isOk());
		
		
		Make persisted = makeRepository.findById(make.get().getId()).orElse(null);
		assertNotNull(persisted, "Записът трябва да съществува!");
		
		assertNotNull(persisted.getDeletedAt(), "deletedAt не трябва да е null!");
	}
	
	@Test
	void deleteMake_whenExists_returnsNotFound() throws Exception {
		UUID id = UUID.randomUUID();
		mockMvc.perform(post("/api/delete/make/{id}", id.toString())
						.param("id", String.valueOf(id))
						.contentType(MediaType.APPLICATION_JSON)
				
				)
				.andExpect(status().isNotFound());
		
		
		Make persisted = makeRepository.findById(id).orElse(null);
		assertNull(persisted, "Записът НЕ трябва да съществува!");
		
	}
	
}
