package org.incf.atlas.server.central.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
public class Input {

	private Long id;
	private String identifier;
	private String title;
	private String _abstract;
	private String dataType;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAbstract() {
		return _abstract;
	}
	public void setAbstract(String _abstract) {
		this._abstract = _abstract;
	}
	
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Input that = (Input) obj;
		return new EqualsBuilder()
				.appendSuper(super.equals(obj))
				.append(this.identifier, that.identifier)
				.append(this.title, that.title)
				.append(this._abstract, that._abstract)
				.append(this.dataType, that.dataType)
				.isEquals();
	}
	 
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.appendSuper(17)
				.append(identifier)
				.append(title)
				.append(_abstract)
				.append(dataType)
				.toHashCode();
	}

}
