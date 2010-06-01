package org.incf.atlas.server.central.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
public class Output {

	private Long id;
	private String identifier;
	private String title;
	private String _abstract;
	private Format defaultFormat;
	private List<Format> supportedFormats;
	
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

	@OneToOne(targetEntity = Format.class, cascade = {CascadeType.ALL})
	public Format getDefaultFormat() {
		return defaultFormat;
	}
	public void setDefaultFormat(Format defaultFormat) {
		this.defaultFormat = defaultFormat;
	}

	@OneToMany(targetEntity = Format.class, cascade = {CascadeType.ALL})
	public List<Format> getSupportedFormats() {
		return supportedFormats;
	}
	public void setSupportedFormats(List<Format> supportedFormats) {
		this.supportedFormats = supportedFormats;
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
		Output that = (Output) obj;
		return new EqualsBuilder()
				.appendSuper(super.equals(obj))
				.append(this.identifier, that.identifier)
				.append(this.title, that.title)
				.append(this._abstract, that._abstract)
				.append(this.defaultFormat, that.defaultFormat)
				.append(this.supportedFormats, that.supportedFormats)
				.isEquals();
	}
	 
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.appendSuper(17)
				.append(identifier)
				.append(title)
				.append(_abstract)
				.append(defaultFormat)
				.append(supportedFormats)
				.toHashCode();
	}

}
