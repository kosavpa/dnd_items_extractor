package owl.home.dnd.service.wand;


import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import owl.home.dnd.entitys.Wand;
import owl.home.dnd.service.common.equipment.EquipmentServiceBean;


@Service
public class WandService extends EquipmentServiceBean<Wand> {
    @Autowired
    protected WandService(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Wand> getConcreteEquipmentClass() {
        return Wand.class;
    }

    @Override
    protected String getConcreteTypeSvgSuffix() {
        return "rod";
    }
}