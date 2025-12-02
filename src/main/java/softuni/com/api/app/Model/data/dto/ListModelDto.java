package softuni.com.api.app.model.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListModelDto {
	
	@JsonProperty("Results")
	private List<ModelDto> results;
	
	public ListModelDto(List<ModelDto> results) {
		this.results = results;
	}
	
	public List<ModelDto> getResults() {
		return results;
	}
	
	public void setResults(List<ModelDto> results) {
		this.results = results;
	}
}
