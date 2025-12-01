package softuni.com.api.app.Model.data.dto;

import softuni.com.api.app.Make.data.dto.ListMakeDto;

import java.util.UUID;

public class AllModelsDtoByMake {

		
		private UUID id;
		
		private int modelId;
		
		private String name;
		
		private String make;
	
	public AllModelsDtoByMake() {
	}
	
	public UUID getId() {
		return id;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}
	
	public int getModelId() {
			return modelId;
		}
		
		public void setModelId(int modelId) {
			this.modelId = modelId;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
	
	public String getMake() {
		return make;
	}
	
	public void setMake(String make) {
		this.make = make;
	}
}
