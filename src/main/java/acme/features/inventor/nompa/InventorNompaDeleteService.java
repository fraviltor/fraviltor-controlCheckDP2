package acme.features.inventor.nompa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.item.Item;
import acme.entities.nompa.Nompa;
import acme.features.inventor.item.InventorItemRepository;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractDeleteService;
import acme.roles.Inventor;

@Service
public class InventorNompaDeleteService  implements AbstractDeleteService<Inventor,Nompa>  {

	@Autowired
	protected InventorNompaRepository repository;
	@Autowired
	protected InventorItemRepository itemRepository;
	
	@Override
	public boolean authorise(final Request<Nompa> request) {
		assert request != null;

		return true;
	}

	@Override
	public void bind(final Request<Nompa> request, final Nompa entity, final Errors errors) {
		request.bind(entity, errors, "code", "theme", "statement", "quantity", "creationMoment", "startDate", "endDate", "additionalInfo");
	}

	@Override
	public void unbind(final Request<Nompa> request, final Nompa entity, final Model model) {
		request.unbind(entity, model, "code", "theme", "statement", "quantity", "creationMoment", "startDate", "endDate", "additionalInfo");
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
	}

	@Override
	public void delete(final Request<Nompa> request, final Nompa entity) {
		assert request != null;
		assert entity != null;
		Item item;
		
		item = this.repository.findOneComponentByNompaId(entity.getId());
		item.setNompa(null);
		this.itemRepository.save(item);
		
		this.repository.delete(entity);
	}
}
