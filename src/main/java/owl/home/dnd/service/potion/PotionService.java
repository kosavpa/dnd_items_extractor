package owl.home.dnd.service.potion;


import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import owl.home.dnd.entitys.Potion;
import owl.home.dnd.service.common.equipment.EquipmentServiceBean;


@Service
public class PotionService extends EquipmentServiceBean<Potion> {
    @Autowired
    protected PotionService(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Potion> getConcreteEquipmentClass() {
        return Potion.class;
    }

    @Override
    protected String getConcreteTypeSvgSuffix() {
        return "potion";
    }
}