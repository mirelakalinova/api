package softuni.com.api.app.model.data.entity;

import jakarta.persistence.*;
import softuni.com.api.app.make.data.entity.Make;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "models")
public class CarModel {
	
	@Id
	@GeneratedValue
	private UUID id;
	
	@Column(name = "model_id")
	private int modelId;
	@Column(nullable = false)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "make_id", referencedColumnName = "id",nullable = false)
	private Make make;
	
	@Column(name="deleted_at")
	private LocalDateTime deletedAt;
	
	public CarModel() {
	}
	
	public CarModel(String name) {
		this.name = name;
	}
	
	public CarModel(int modelId, String name) {
		this.modelId = modelId;
		this.name = name;
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
	
	public Make getMake() {
		return make;
	}
	
	public void setMake(Make make) {
		this.make = make;
	}
	
	public LocalDateTime getDeletedAt() {
		return deletedAt;
	}
	
	public void setDeletedAt(LocalDateTime deletedAt) {
		this.deletedAt = deletedAt;
	}
}
