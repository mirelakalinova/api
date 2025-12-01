package softuni.com.api.app.Make.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MakeDto {
	@JsonProperty("MakeId")
	private int makeId;
	@JsonProperty("MakeName")
	private String makeName;
	
	public MakeDto() {
	}

	public MakeDto(int makeId, String makeName) {
		this.makeId = makeId;
		this.makeName = makeName;
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

