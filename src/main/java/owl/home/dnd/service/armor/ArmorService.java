package owl.home.dnd.service.armor;


import owl.home.dnd.constant.item_type.armor.ArmorClass;
import owl.home.dnd.constant.item_type.armor.ArmorCore;
import owl.home.dnd.entitys.Armor;
import owl.home.dnd.service.common.core.CoreEquipmentBean;
import owl.home.dnd.service.common.equipment.EquipmentHint;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;


public class ArmorService extends CoreEquipmentBean<Armor, ArmorCore> {
    @Override
    protected Set<ArmorCore> getCoreByName(String name) {
        return Arrays
                .stream(ArmorCore.values())
                .filter(armorCore -> armorCore.getName().equalsIgnoreCase(name))
                .collect(Collectors.toSet());
    }

    @Override
    protected Set<ArmorCore> getCoresByTip(String tip) {
        return switch (tip) {
            case "лёгкий", "легкий" -> getCoresByClass(ArmorClass.LUNG);
            case "средний" -> getCoresByClass(ArmorClass.MIDDLE);
            case "тяжёлый", "тяжелый" -> getCoresByClass(ArmorClass.HEAVY);
            default -> throw new IllegalArgumentException();
        };
    }

    private Set<ArmorCore> getCoresByClass(ArmorClass armorClass) {
        return Arrays
                .stream(ArmorCore.values())
                .filter(armorCore -> armorClass.equals(armorCore.getArmorClass()))
                .collect(Collectors.toSet());
    }

    @Override
    protected Set<ArmorCore> getCoreAllCores() {
        return Arrays
                .stream(ArmorCore.values())
                .collect(Collectors.toSet());
    }

    @Override
    protected Armor buildConcreteEquipment(EquipmentHint<Armor> equipmentHint) {
        Armor armor = equipmentHint.getEquipment();

        armor.setArmorCores(extractCoresByHint(equipmentHint));

        return armor;
    }

    @Override
    protected Class<Armor> getConcreteEquipmentClass() {
        return Armor.class;
    }

    @Override
    protected String getConcreteTypeSvgSuffix() {
        return "armor";
    }

    @Override
    protected void excludeCores(Set<ArmorCore> cores, EquipmentHint<Armor> hint) {
        if (hint.isExcludeByTip()) {
            cores.removeAll(getCoresByTip(hint.getExclude()));
        } else {
            Set<ArmorCore> excludedCores = getCoreAllCores()
                    .stream()
                    .filter(armorCore -> armorCore.getName().toLowerCase().contains(hint.getExclude()))
                    .collect(Collectors.toSet());

            if (excludedCores.isEmpty()) {
                throw new IllegalArgumentException();
            }

            cores.removeAll(excludedCores);
        }
    }
}