package owl.home.dnd.service.wand;


import owl.home.dnd.entitys.Wand;
import owl.home.dnd.service.common.equipment.EquipmentHint;
import owl.home.dnd.service.common.equipment.EquipmentServiceBean;


public class WandService extends EquipmentServiceBean<Wand> {
    @Override
    protected Wand buildConcreteEquipment(EquipmentHint<Wand> equipmentHint) {
        return equipmentHint.getEquipment();
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