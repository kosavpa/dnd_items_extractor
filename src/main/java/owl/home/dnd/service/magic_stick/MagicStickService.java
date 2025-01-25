package owl.home.dnd.service.magic_stick;


import owl.home.dnd.entitys.MagicStick;
import owl.home.dnd.service.common.equipment.EquipmentHint;
import owl.home.dnd.service.common.equipment.EquipmentServiceBean;


public class MagicStickService extends EquipmentServiceBean<MagicStick> {
    @Override
    protected MagicStick buildConcreteEquipment(EquipmentHint<MagicStick> equipmentHint) {
        return equipmentHint.getEquipment();
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