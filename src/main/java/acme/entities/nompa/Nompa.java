package acme.entities.nompa;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.framework.datatypes.Money;
import acme.framework.entities.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Nompa extends AbstractEntity{
	
	protected static final long serialVersionUID = 1L;
	
	@NotBlank
	@Pattern(regexp = "^\\w{2}\\d{2}\\w{2}-[0-9]{6}$")
	protected String code;

	@NotNull
	@Past
    @Temporal(TemporalType.TIMESTAMP)
    protected Date creationMoment;
	
	@NotBlank
	@Length(min=1, max = 100)
	protected String theme;
	
	@NotBlank
	@Length(min=1, max = 255)
	protected String statement;
	
	@NotNull
    @Temporal(TemporalType.TIMESTAMP)
	protected Date startDate;
	 
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date endDate;
	
	@NotNull
	@Valid
	protected Money quantity;

	@URL
	protected String additionalInfo;
	
	//Relationships
//	@NotNull
//	@Valid
//	@OneToOne(optional = false)
//	@JoinColumn(unique = true)
//	protected Item	item;
}
