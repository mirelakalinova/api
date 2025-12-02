package softuni.com.api.app.make.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListMakesDto {
	
	
	@JsonProperty("Results")
	private List<MakeDto> results;
	
	public ListMakesDto(List<MakeDto> results) {
		this.results = results;
	}
	
	public List<MakeDto> getResults() {
		return results;
	}
	
	public void setResults(List<MakeDto> results) {
		this.results = results;
	}
}
