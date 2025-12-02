package softuni.com.api.make;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.server.ResponseStatusException;
import softuni.com.api.app.make.data.dto.ListMakeDto;
import softuni.com.api.app.make.data.dto.ListMakesDto;
import softuni.com.api.app.make.data.dto.MakeDto;
import softuni.com.api.app.make.data.entity.Make;
import softuni.com.api.app.make.repo.MakeRepository;
import softuni.com.api.app.make.service.MakeServiceImpl;
import softuni.com.api.app.model.data.entity.CarModel;
import softuni.com.api.app.model.repo.ModelRepository;
import softuni.com.api.config.RestClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MakeUTest {
	
	@Mock
	private MakeRepository makeRepository;
	@Mock
	private ModelRepository modelRepository;
	@Mock
	private RestClient restClient;
	@Mock
	private ModelMapper modelMapper;
	@InjectMocks
	@Spy
	private MakeServiceImpl makeService;
	private Make makeFirst;
	private Make makeSecond;
	
	@BeforeEach
	void setUp() {
		
		makeFirst = new Make();
		makeFirst.setMakeId(123);
		makeFirst.setName("Test");
		
		makeSecond = new Make();
		makeSecond.setName("Test Second");
		makeSecond.setMakeId(125);
		
	}
	
	@Test
	void isInitializedMakes_ReturnTrue() {
		when(makeRepository.count()).thenReturn(2L);
		boolean result = makeService.isInitializedMakes();
		assertTrue(result);
		
	}
	
	@Test
	void isInitializedMakes_ReturnFalse() {
		makeRepository.deleteAll();
		when(makeRepository.count()).thenReturn(0L);
		boolean result = makeService.isInitializedMakes();
		assertFalse(result);
		
	}
	
	@Test
	void updateMakesData_whenMakeDoesNotExist_savesMake() {
		MakeDto dto = new MakeDto(125, "Test Make");
		ListMakesDto input = new ListMakesDto(List.of(dto));
		
		doReturn(false).when(makeService).findByMakeId(125);
		
		makeService.updateMakesData(input);
		
		ArgumentCaptor<Make> captor = ArgumentCaptor.forClass(Make.class);
		verify(makeRepository, times(1)).saveAndFlush(captor.capture());
		
		Make saved = captor.getValue();
		assertEquals(125, saved.getMakeId());
		assertEquals("Test Make", saved.getName());
	}
	
	@Test
	void findByMakeId_ReturnTrue() {
		when(makeRepository.findByMakeId(makeFirst.getMakeId())).thenReturn(Optional.of(makeFirst));
		boolean findMake = makeService.findByMakeId(makeFirst.getMakeId());
		assertTrue(findMake);
		verify(makeRepository).findByMakeId(makeFirst.getMakeId());
	}
	
	@Test
	void findByMakeId_ReturnFalse() {
		when(makeRepository.findByMakeId(150)).thenReturn(Optional.empty());
		boolean findMake = makeService.findByMakeId(150);
		assertFalse(findMake);
		
	}
	
	@Test
	void getAllMakes_ReturnList() {
		makeSecond.setDeletedAt(LocalDateTime.now());
		makeRepository.save(makeSecond);
		List<Make> makes = List.of(makeFirst, makeSecond);
		when(makeRepository.findAllByDeletedAtNull()).thenReturn(List.of(makeFirst));
		when(modelMapper.map(any(Make.class), eq(ListMakeDto.class))).thenReturn(new ListMakeDto());
		List<ListMakeDto> result = makeService.getAllMakes();
		assertEquals(1, result.size());
		assertNotEquals(makes.size(), result.size());
		
	}
	
	@Test
	void getAllMakes_ReturnEmptyList() {
		makeSecond.setDeletedAt(LocalDateTime.now());
		makeFirst.setDeletedAt(LocalDateTime.now());
		makeRepository.save(makeSecond);
		makeRepository.save(makeFirst);
		List<Make> makes = List.of(makeFirst, makeSecond);
		when(makeRepository.findAllByDeletedAtNull()).thenReturn(new ArrayList<>());
		List<ListMakeDto> result = makeService.getAllMakes();
		assertEquals(0, result.size());
		assertNotEquals(makes.size(), result.size());
	}
	
	@Test
	void getMakeByName_MakeNotExist() {
		String name = "NEW MAKE";
		
		Make saved = new Make();
		saved.setName(name);
		
		when(makeRepository.findByName(name)).thenReturn(Optional.empty()).thenReturn(Optional.of(saved));
		when(makeRepository.saveAndFlush(any(Make.class))).thenReturn(saved);
		Make result = makeService.getMakeByName(name);
		assertNotNull(result);
		assertEquals(name, result.getName());
		
		verify(makeRepository, times(2)).findByName(name);
		ArgumentCaptor<Make> captor = ArgumentCaptor.forClass(Make.class);
		verify(makeRepository, times(1)).saveAndFlush(captor.capture());
		Make toSaved = captor.getValue();
		assertEquals(name, toSaved.getName());
	}
	
	
	@Test
	void getMakeByName_MakeExist() {
		when(makeRepository.findByName(makeFirst.getName())).thenReturn(Optional.of(makeFirst));
		Make make = makeService.getMakeByName(makeFirst.getName());
		assertNotNull(make);
		verify(makeRepository, never()).save(make);
	}
	
	@Test
	void deleteMakeById_Success() {
		when(makeRepository.findById(makeFirst.getId())).thenReturn(Optional.of(makeFirst));
		String result = makeService.deleteMake(makeFirst.getId());
		verify(makeRepository).save(makeFirst);
		assertNotNull(makeFirst);
		assertNotNull(makeFirst.getDeletedAt());
		assertEquals("Успешно изтрита марка: Test", result);
	}
	
	@Test
	void deleteMakeById_Throw() {
		UUID uuid = UUID.randomUUID();
		when(makeRepository.findById(uuid)).thenReturn(Optional.empty());
		assertThrows(ResponseStatusException.class, () -> {
			makeService.deleteMake(uuid);
		});
	}
	
	@Test
	void deleteMakeByIdWithModels_Success() {
		UUID uuid = makeFirst.getId();
		CarModel model = new CarModel();
		model.setMake(makeFirst);
		model.setName("Test Model");
		
		when(makeRepository.findById(uuid)).thenReturn(Optional.of(makeFirst));
		when(modelRepository.findAllByMake(makeFirst)).thenReturn(List.of(model));
		String result = makeService.deleteMake(makeFirst.getId());
		verify(makeRepository).save(makeFirst);
		verify(modelRepository).save(model);
		assertNotNull(makeFirst);
		assertNotNull(model);
		assertNotNull(makeFirst.getDeletedAt());
		assertNotNull(model.getDeletedAt());
		assertEquals("Успешно изтрита марка: Test", result);
	}
	
	
}
