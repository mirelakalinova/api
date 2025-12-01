package softuni.com.api.app.Make.service;

import softuni.com.api.app.Make.data.dto.ListMakeDto;
import softuni.com.api.app.Make.data.dto.ListMakesDto;
import softuni.com.api.app.Make.data.entity.Make;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MakeService {
	boolean isInitializedMakes();
	
	ListMakesDto fetchMakesData();
	
	void updateMakesData(ListMakesDto makeDto);
	
	boolean findByMakeId(int makeId);
	
	List<ListMakeDto> getAllMakes();
	
	 Make getMakeByName(String makeName);
	 
	 String deleteMake(UUID id);
}
