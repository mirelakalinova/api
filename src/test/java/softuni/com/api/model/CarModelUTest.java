package softuni.com.api.model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import softuni.com.api.app.exception.NoSuchResourceException;
import softuni.com.api.app.make.data.entity.Make;
import softuni.com.api.app.make.repo.MakeRepository;
import softuni.com.api.app.model.data.dto.AllModelsDtoByMake;
import softuni.com.api.app.model.data.entity.CarModel;
import softuni.com.api.app.model.repo.ModelRepository;
import softuni.com.api.app.model.service.ModelServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarModelUTest {
	
	@Mock
	private MakeRepository makeRepository;
	@Mock
	private ModelRepository modelRepository;
	@Mock
	private ModelMapper modelMapper;
	@InjectMocks
	private ModelServiceImpl modelService;
	private CarModel modelFirst;
	private CarModel modelSecond;
	
	@BeforeEach
	void setUp() {
		
		modelFirst = new CarModel();
		modelFirst.setModelId(123);
		modelFirst.setName("Test");
		
		modelSecond = new CarModel();
		modelSecond.setName("Test Second");
		modelSecond.setModelId(125);
		
	}
	
	@Test
	void isInitializedMakes_ReturnTrue() {
		when(modelRepository.count()).thenReturn(2L);
		boolean result = modelService.isInitializeModels();
		assertTrue(result);
		
	}
	
	@Test
	void isInitializedModels_ReturnFalse() {
		modelRepository.deleteAll();
		when(modelRepository.count()).thenReturn(0L);
		boolean result = modelService.isInitializeModels();
		assertFalse(result);
		
	}
	
	@Test
	void getAllModelsByMakeId_ReturnList() {
		UUID makeId = UUID.randomUUID();
		Make make = new Make();
		
		List<CarModel> rawModels = List.of(modelFirst, modelSecond);
		List<CarModel> models = List.of(modelFirst);
		make.setName("Test");
		make.setId(makeId);
		make.setModels(models);
		modelFirst.setMake(make);
		when(modelRepository.findAllByDeletedAtNull()).thenReturn(models);
		when(modelMapper.map(any(CarModel.class), eq(AllModelsDtoByMake.class))).thenReturn(new AllModelsDtoByMake());
		List<AllModelsDtoByMake> modelsResult = modelService.getAllModelsWithMakes();
		
		assertEquals(1, modelsResult.size());
		
	}
	
	//	@Test
//	void findByMakeId_ReturnFalse(){
//		when(makeRepository.findByMakeId(150)).thenReturn(Optional.empty());
//		boolean findMake = modelService.findByMakeId(150);
//		assertFalse(findMake);
//
//	}
//
//	@Test
//	void getAllMakes_ReturnList(){
//		modelSecond.setDeletedAt(LocalDateTime.now());
//		makeRepository.save(modelSecond);
//		List<Make> makes = List.of(modelFirst, modelSecond);
//		when(makeRepository.findAllByDeletedAtNull()).thenReturn(List.of(modelFirst));
//		when(modelMapper.map(any(Make.class), eq(ListMakeDto.class))).thenReturn(new ListMakeDto());
//		List<ListMakeDto> result = modelService.getAllMakes();
//		assertEquals(1, result.size());
//		assertNotEquals(makes.size(), result.size());
//
//	}
//	@Test
//	void getAllMakes_ReturnEmptyList(){
//		modelSecond.setDeletedAt(LocalDateTime.now());
//		modelFirst.setDeletedAt(LocalDateTime.now());
//		makeRepository.save(modelSecond);
//		makeRepository.save(modelFirst);
//		List<Make> makes = List.of(modelFirst, modelSecond);
//		when(makeRepository.findAllByDeletedAtNull()).thenReturn(new ArrayList<>());
//		List<ListMakeDto> result = modelService.get();
//		assertEquals(0, result.size());
//		assertNotEquals(makes.size(), result.size());
//	}
//
//	@Test
//	void getMakeByName_MakeExist(){
//		when(makeRepository.findByName(modelFirst.getName())).thenReturn(Optional.of(modelFirst));
//		Make make = modelService.find(modelFirst.getName());
//		assertNotNull(make);
//		verify(makeRepository,never()).save(make);
//	}
//
//	@Test
//	void deleteMakeById_Success(){
//		when(makeRepository.findById(modelFirst.getId())).thenReturn(Optional.of(modelFirst));
//		String result = modelService.deleteMake(modelFirst.getId());
//		verify(makeRepository).save(modelFirst);
//		assertNotNull(modelFirst);
//		assertNotNull(modelFirst.getDeletedAt());
//		assertEquals("Успешно изтрита марка: Test", result);
//	}
//
	@Test
	void deleteModelById_Throw() {
		UUID uuid = UUID.randomUUID();
		when(modelRepository.findById(uuid)).thenReturn(Optional.empty());
		assertThrows(NoSuchResourceException.class, () -> {
			modelService.deleteModel(uuid);
		});
	}
	
	@Test
	void deleteModelById_Success() {
		UUID uuid = modelFirst.getId();
		
		
		when(modelRepository.findById(uuid)).thenReturn(Optional.of(modelFirst));
		
		HashMap<String, String> result = modelService.deleteModel(modelFirst.getId());
		
		verify(modelRepository).save(modelFirst);
		
		assertNotNull(modelFirst);
		
		assertNotNull(modelFirst.getDeletedAt());
		assertEquals("Успешно изтрит модел: Test", result.get("message"));
	}
	
	
}
