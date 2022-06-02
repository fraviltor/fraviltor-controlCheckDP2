package acme.features.authenticated.moneyExchange;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import acme.components.ExchangeRate;
import acme.entities.moneyExchange.MoneyExchange;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.datatypes.Money;
import acme.framework.helpers.StringHelper;
import acme.framework.roles.Authenticated;
import acme.framework.services.AbstractPerformService;

@Service
public class AuthenticatedMoneyExchangePerformService implements AbstractPerformService<Authenticated, MoneyExchange> {

	// AbstractPerformService<Authenticated, ExchangeRecord> interface ---------
	
	@Override
	public boolean authorise(final Request<MoneyExchange> request) {
		assert request != null;

		return true;
	}

	@Override
	public void bind(final Request<MoneyExchange> request, final MoneyExchange entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors, "source", "targetCurrency", "date", "target");
	}

	@Override
	public void unbind(final Request<MoneyExchange> request, final MoneyExchange entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "source", "targetCurrency", "date", "target");
	}

	@Override
	public MoneyExchange instantiate(final Request<MoneyExchange> request) {
		assert request != null;

		MoneyExchange result;

		result = new MoneyExchange();

		return result;
	}

	@Override
	public void validate(final Request<MoneyExchange> request, final MoneyExchange entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
	}

	@Override
	public void perform(final Request<MoneyExchange> request, final MoneyExchange entity, final Errors errors) {
		assert request != null;
		assert entity != null;

		Money sourceMoney, targetMoney;
		String targetCurrency;
		Date date;
		MoneyExchange exchange;

		sourceMoney = request.getModel().getAttribute("source", Money.class);
		targetCurrency = request.getModel().getAttribute("targetCurrency", String.class);
		exchange = this.computeMoneyExchange(sourceMoney, targetCurrency);
		errors.state(request, exchange != null, "*", "authenticated.money-exchange.form.label.api-error");
		if (exchange == null) {
			entity.setTarget(null);
			entity.setDate(null);
		} else {
			targetMoney = exchange.getTarget();
			entity.setTarget(targetMoney);
			date = exchange.getDate();
			entity.setDate(date);
		}
	}


	public MoneyExchange computeMoneyExchange(final Money source, final String targetCurrency) {
		assert source != null;
		assert !StringHelper.isBlank(targetCurrency);

		MoneyExchange res;
		RestTemplate api;
		ExchangeRate rec;
		String sourceCurrency;
		Double sourceAmount, targetAmount, rate;
		Money target;

		try {
			api = new RestTemplate();

			sourceCurrency = source.getCurrency();
			sourceAmount = source.getAmount();

			rec = api.getForObject( 
				"https://api.exchangerate.host/latest?base={0}&symbols={1}", 
				ExchangeRate.class, 
				sourceCurrency, 
				targetCurrency 
			);

			assert rec != null;
			rate = rec.getRates().get(targetCurrency);
			targetAmount = rate * sourceAmount;

			target = new Money();
			target.setAmount(targetAmount);
			target.setCurrency(targetCurrency);

			res = new MoneyExchange();
			res.setSource(source);
			res.setCurrencyTarget(targetCurrency);
			res.setDate(rec.getDate());
			res.setTarget(target);
		} catch (final Throwable oops) {
			res = null;
		}

		return res;
	}
}