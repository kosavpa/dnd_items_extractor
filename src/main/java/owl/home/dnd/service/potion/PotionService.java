package owl.home.dnd.service.potion;


import owl.home.dnd.entitys.Potion;
import owl.home.dnd.service.common.equipment.EquipmentHint;
import owl.home.dnd.service.common.equipment.EquipmentServiceBean;


public class PotionService extends EquipmentServiceBean<Potion> {
    @Override
    protected Potion buildConcreteEquipment(EquipmentHint<Potion> equipmentHint) {
        return equipmentHint.getEquipment();
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