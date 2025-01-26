package owl.home.dnd.service.magic_item;


import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import owl.home.dnd.entitys.MagicItem;
import owl.home.dnd.service.common.equipment.EquipmentServiceBean;


@Service
public class MagicItemService extends EquipmentServiceBean<MagicItem> {
    @Autowired
    protected MagicItemService(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<MagicItem> getConcreteEquipmentClass() {
        return MagicItem.class;
    }

    @Override
    protected String getConcreteTypeSvgSuffix() {
        return "magic";
    }
}