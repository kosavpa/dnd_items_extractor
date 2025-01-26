package owl.home.dnd.service.magic_item;


import owl.home.dnd.entitys.MagicItem;
import owl.home.dnd.service.common.equipment.EquipmentServiceBean;


public class MagicItemService extends EquipmentServiceBean<MagicItem> {
    @Override
    protected Class<MagicItem> getConcreteEquipmentClass() {
        return MagicItem.class;
    }

    @Override
    protected String getConcreteTypeSvgSuffix() {
        return "magic";
    }
}