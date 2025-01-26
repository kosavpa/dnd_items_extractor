package owl.home.dnd.service.ring;


import owl.home.dnd.entitys.Ring;
import owl.home.dnd.service.common.equipment.EquipmentServiceBean;


public class RingService extends EquipmentServiceBean<Ring> {
    @Override
    protected Class<Ring> getConcreteEquipmentClass() {
        return Ring.class;
    }

    @Override
    protected String getConcreteTypeSvgSuffix() {
        return "ring";
    }
}