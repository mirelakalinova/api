package softuni.com.api.app.make.service;

import softuni.com.api.app.make.data.dto.ListMakeDto;
import softuni.com.api.app.make.data.dto.ListMakesDto;
import softuni.com.api.app.make.data.entity.Make;

import java.util.List;
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
