package acme.features.inventor.nompa;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.nompa.Nompa;
import acme.framework.controllers.AbstractController;
import acme.roles.Inventor;

@Controller
public class InventorNompaController extends AbstractController<Inventor, Nompa>{

	@Autowired
	protected InventorNompaListService listService;

	@Autowired
	protected InventorNompaShowService showService;
	
	@Autowired
	protected InventorNompaCreateService createService;
	
	@Autowired
	protected InventorNompaUpdateService updateService;

	@Autowired
	protected InventorNompaDeleteService deleteService;

	@PostConstruct
	protected void initialise() {
		super.addCommand("list", this.listService);
		super.addCommand("show", this.showService);
		super.addCommand("create", this.createService);
		super.addCommand("update", this.updateService);
		super.addCommand("delete", this.deleteService);
	}

}