package acme.features.inventor.nompa;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.item.Item;
import acme.entities.nompa.Nompa;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface InventorNompaRepository extends AbstractRepository {

	@Query("select i.nompa from Item i where i.inventor.id =:id")
	Collection<Nompa> findNompasByInventorId(int id);
	
	@Query("select n from Nompa n where n.id = :id")
	Nompa findOneNompaById(int id);
	
	@Query("SELECT i FROM Item i WHERE i.nompa.id =:id AND i.itemType = acme.entities.item.ItemType.COMPONENT")
	Item findOneComponentByNompaId(int id);
	
	@Query("SELECT c FROM Nompa c WHERE c.code =:code")
	Nompa findOneNompaByCode(String code);

}