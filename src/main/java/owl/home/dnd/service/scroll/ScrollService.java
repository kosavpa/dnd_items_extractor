package owl.home.dnd.service.scroll;


import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import owl.home.dnd.entitys.Scroll;
import owl.home.dnd.service.common.equipment.EquipmentServiceBean;


@Service
public class ScrollService extends EquipmentServiceBean<Scroll> {
    @Autowired
    protected ScrollService(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Scroll> getConcreteEquipmentClass() {
        return Scroll.class;
    }

    @Override
    protected String getConcreteTypeSvgSuffix() {
        return "scroll";
    }
}