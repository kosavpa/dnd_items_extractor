package owl.home.dnd.service.ring;


import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import owl.home.dnd.entitys.Ring;
import owl.home.dnd.service.common.equipment.EquipmentServiceBean;


@Service
public class RingService extends EquipmentServiceBean<Ring> {
    @Autowired
    protected RingService(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Ring> getConcreteEquipmentClass() {
        return Ring.class;
    }

    @Override
    protected String getConcreteTypeSvgSuffix() {
        return "ring";
    }
}