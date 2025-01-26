package owl.home.dnd.service.magic_stick;


import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import owl.home.dnd.entitys.MagicStick;
import owl.home.dnd.service.common.equipment.EquipmentServiceBean;


@Service
public class MagicStickService extends EquipmentServiceBean<MagicStick> {
    @Autowired
    protected MagicStickService(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<MagicStick> getConcreteEquipmentClass() {
        return MagicStick.class;
    }

    @Override
    protected String getConcreteTypeSvgSuffix() {
        return "wand";
    }
}