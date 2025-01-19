package owl.home.dnd.service.scroll;


import owl.home.dnd.entitys.Scroll;
import owl.home.dnd.service.common.equipment.EquipmentHint;
import owl.home.dnd.service.common.equipment.EquipmentServiceBean;


public class ScrollService extends EquipmentServiceBean<Scroll> {
    @Override
    protected Scroll buildConcreteEquipment(EquipmentHint<Scroll> equipmentHint) {
        return equipmentHint.getEquipment();
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