package acme.features.inventor.nompa;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.item.Item;
import acme.entities.nompa.Nompa;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.features.authenticated.systemConfiguration.AuthenticatedSystemConfigurationRepository;
import acme.features.inventor.item.InventorItemRepository;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractUpdateService;
import acme.roles.Inventor;
import spamDetector.SpamDetector;

@Service
public class InventorNompaUpdateService implements AbstractUpdateService<Inventor,Nompa>{
	
	@Autowired
	protected InventorNompaRepository repository;
	@Autowired
	protected InventorItemRepository itemRepository;
	@Autowired
	protected AuthenticatedSystemConfigurationRepository systemConfigRepository;

	@Override
	public boolean authorise(final Request<Nompa> request) {
		assert request != null;
		boolean result;
		
		int chimpumId;
		Item item;

		chimpumId = request.getModel().getInteger("id");
		item = this.repository.findOneComponentByNompaId(chimpumId);
		result = item.getInventor().getId() == request.getPrincipal().getActiveRoleId();
		
		return result;
	}

	@Override
	public void bind(final Request<Nompa> request, final Nompa entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		request.bind(entity, errors, "code", "theme", "statement","creationMoment", "quantity", "startDate", "endDate", "additionalInfo");
	}

	@Override
	public void unbind(final Request<Nompa> request, final Nompa entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		request.unbind(entity, model, "code", "theme", "statement","creationMoment", "quantity", "startDate", "endDate", "additionalInfo");	
	}

	@Override
	public Nompa findOne(final Request<Nompa> request) {
		assert request != null;
		Nompa result;
		int id;
		
		id = request.getModel().getInteger("id");
		result = this.repository.findOneNompaById(id);
		
		return result;
	}

	@Override
	public void validate(final Request<Nompa> request, final Nompa entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

        final SystemConfiguration systemConfig = this.systemConfigRepository.findSystemConfiguration();
        final String StrongEN = systemConfig.getStrongSpamTermsEn();
        final String StrongES = systemConfig.getStrongSpamTermsEs();
        final String WeakEN = systemConfig.getWeakSpamTermsEn();
        final String WeakES = systemConfig.getWeakSpamTermsEs();

        final double StrongThreshold = systemConfig.getStrongThreshold();
        final double WeakThreshold = systemConfig.getWeakThreshold();
        
        if(!errors.hasErrors("code")) {
        	final String inmutable = entity.getCode().substring(7,13);
        	final LocalDate cm =  entity.getCreationMoment().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        	final String originalCode = this.codeGenerator(cm);
    		
        	errors.state(request, inmutable.equals(originalCode), "code", "inventor.nompa.form.error.inmutable-date-code");
        	
    		Nompa existing;
    		existing = this.repository.findOneNompaByCode(entity.getCode());
    		
    		if(existing!=null) {
    			errors.state(request, existing.getId()==entity.getId(), "code", "inventor.nompa.form.error.duplicated-code");
    		}
    	}
        
        if(!errors.hasErrors("theme")) {
            final boolean res;
            res = SpamDetector.spamDetector(entity.getTheme(),StrongEN,StrongES,WeakEN,WeakES,StrongThreshold,WeakThreshold);
            errors.state(request, res, "theme", "alert-message.form.spam");
        }
        
        if(!errors.hasErrors("statement")) {
            final boolean res;
            res = SpamDetector.spamDetector(entity.getStatement(),StrongEN,StrongES,WeakEN,WeakES,StrongThreshold,WeakThreshold);
            errors.state(request, res, "statement", "alert-message.form.spam");
        }
        
        if(!errors.hasErrors("additionalInfo")) {
            final boolean res;
            res = SpamDetector.spamDetector(entity.getAdditionalInfo(),StrongEN,StrongES,WeakEN,WeakES,StrongThreshold,WeakThreshold);
            errors.state(request, res, "additionalInfo", "alert-message.form.spam");
        }
		
		if(!errors.hasErrors("quantity")) {
			final List<String> currencies = new ArrayList<>();
			String currency;
			Double amount;
			
			for(final String c: this.systemConfigRepository.acceptedCurrencies().split(",")) {
				currencies.add(c.trim());
			}
			
			currency = entity.getQuantity().getCurrency();
			
			amount = entity.getQuantity().getAmount();
			
			errors.state(request, currencies.contains(currency) , "budget","inventor.nompa.form.error.currency");
			errors.state(request, amount>=0.00 , "budget","inventor.nompa.form.error.amount-negative");
		}
		
		if(entity.getStartDate()!=null) {
			if(!errors.hasErrors("startDate")) {
				Date startDate;
				startDate = entity.getStartDate();

				final long diff = startDate.getTime() - entity.getCreationMoment().getTime();
				final TimeUnit time = TimeUnit.DAYS; 
		        final long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
		        
		        errors.state(request, diffrence>=30 , "startDate","inventor.chimpum.form.error.startDate");
			}
			
			if(!errors.hasErrors("endDate")) {
				Date endDate;
				endDate = entity.getEndDate();
				
				final long diff = endDate.getTime() - entity.getStartDate().getTime();
			    final TimeUnit time = TimeUnit.DAYS; 
			    final long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
			        
			    errors.state(request, diffrence>=7 , "endDate","inventor.chimpum.form.error.endDate");
			}
		}
	}

	@Override
	public void update(final Request<Nompa> request, final Nompa entity) {
		assert request != null;
		assert entity != null;

		this.repository.save(entity);
	}
	
	//Método auxiliar que genera automáticamente el código
	
	public String codeGenerator(final LocalDate creationMoment) {
		String result = "";
		
		final Integer day = creationMoment.getDayOfMonth();
		final Integer month = creationMoment.getMonthValue();
		final Integer year = creationMoment.getYear();

		final String yearCode = year.toString().substring(2, 4);
		String monthCode= "";
		String dayCode= "";
		
		if(month.toString().length()==1) {
			monthCode = "0" + month.toString();
		}else{
			monthCode = month.toString();
		}
			
		if(day.toString().length()==1) {
			dayCode = "0" + day.toString();
		}else {
			dayCode = day.toString();
		}
				
		result = monthCode + dayCode + yearCode;
			
		return result;
	}
}
