package acme.features.inventor.nompa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.item.Item;
import acme.entities.moneyExchange.MoneyExchange;
import acme.entities.nompa.Nompa;
import acme.features.authenticated.moneyExchange.AuthenticatedMoneyExchangePerformService;
import acme.features.authenticated.systemConfiguration.AuthenticatedSystemConfigurationRepository;
import acme.framework.components.models.Model;
import acme.framework.controllers.Request;
import acme.framework.datatypes.Money;
import acme.framework.services.AbstractShowService;
import acme.roles.Inventor;

@Service
public class InventorNompaShowService implements AbstractShowService<Inventor, Nompa> {

	@Autowired
	protected InventorNompaRepository repository;
	@Autowired
	protected AuthenticatedSystemConfigurationRepository systemConfigRepository;

	@Override
	public boolean authorise(final Request<Nompa> request) {
		assert request != null;

		boolean result;
		Item component;
		int nompaId;

		nompaId = request.getModel().getInteger("id");
		component = this.repository.findOneComponentByNompaId(nompaId);
		result = component.getInventor().getId() == request.getPrincipal().getActiveRoleId();

		return result;
	}

	@Override
	public Nompa findOne(final Request<Nompa> request) {
		assert request != null;
		Nompa result;
		int nompaId;

		nompaId = request.getModel().getInteger("id");
		result = this.repository.findOneNompaById(nompaId);

		return result;
	}

	@Override
	public void unbind(final Request<Nompa> request, final Nompa entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		final Money newQuantity = this.moneyExchangeNompa(entity);
		model.setAttribute("newQuantity", newQuantity);
		
		model.setAttribute("itemId", this.repository.findOneComponentByNompaId(entity.getId()).getId());

		request.unbind(entity, model, "code", "theme", "statement", "quantity", "creationMoment", "startDate", "endDate", "additionalInfo");
	}
	
	//Método auxiliar cambio de divisa

	public Money moneyExchangeNompa(final Nompa c) {

		final String nompaCurrency = c.getQuantity().getCurrency();
		
		final AuthenticatedMoneyExchangePerformService moneyExchange = new AuthenticatedMoneyExchangePerformService();
		final String systemCurrency = this.systemConfigRepository.findSystemConfiguration().getSystemCurrency();
		final Double conversionAmount;

		if(!systemCurrency.equals(nompaCurrency)) {
			MoneyExchange conversion;
			conversion = moneyExchange.computeMoneyExchange(c.getQuantity(), systemCurrency);
			conversionAmount = conversion.getTarget().getAmount();	
		}
		else {
			conversionAmount = c.getQuantity().getAmount();
		}

		final Money newQuantity = new Money();
		newQuantity.setAmount(conversionAmount);
		newQuantity.setCurrency(systemCurrency);
				
		return newQuantity;
	}

}