package softuni.com.api.app.Make.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class ListMakeDto {
	@JsonProperty("id")
	private UUID id;
	@JsonProperty("makeId")
	private int makeId;
	@JsonProperty("name")
	private String makeName;
	
	public ListMakeDto() {
	}
	
	
	public UUID getId() {
		return id;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}
	
	public int getMakeId() {
		return makeId;
	}
	
	public void setMakeId(int makeId) {
		this.makeId = makeId;
	}
	
	public String getMakeName() {
		return makeName;
	}
	
	public void setMakeName(String makeName) {
		this.makeName = makeName;
	}
}
