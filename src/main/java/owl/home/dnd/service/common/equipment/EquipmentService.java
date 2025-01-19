package owl.home.dnd.service.common.equipment;


import owl.home.dnd.entitys.Equipment;

import java.util.Set;


public interface EquipmentService<T extends Equipment> {
    Set<T> extractEquipment();
}