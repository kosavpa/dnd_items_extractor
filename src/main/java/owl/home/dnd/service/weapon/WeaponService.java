package owl.home.dnd.service.weapon;


import owl.home.dnd.constant.item_type.weapon.WeaponCore;
import owl.home.dnd.entitys.Weapon;
import owl.home.dnd.service.common.core.CoreEquipmentBean;
import owl.home.dnd.service.common.equipment.EquipmentHint;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static owl.home.dnd.constant.item_type.weapon.WeaponCore.*;


public class WeaponService extends CoreEquipmentBean<Weapon, WeaponCore> {
    @Override
    protected Weapon buildConcreteEquipment(EquipmentHint<Weapon> equipmentHint) {
        Weapon weapon = super.buildConcreteEquipment(equipmentHint);

        weapon.setWeaponCores(extractCoresByHint(equipmentHint));

        return weapon;
    }

    @Override
    protected Class<Weapon> getConcreteEquipmentClass() {
        return Weapon.class;
    }

    @Override
    protected String getConcreteTypeSvgSuffix() {
        return "weapon";
    }

    @Override
    protected void excludeCores(Set<WeaponCore> cores, EquipmentHint<Weapon> exclude) {
        //ignore
    }

    @Override
    protected Set<WeaponCore> getCoreByName(String name) {
        Set<WeaponCore> cores = new HashSet<>();

        for (WeaponCore core : WeaponCore.values()) {
            String coreName = core.getName();

            if (coreName.equalsIgnoreCase(name)
                    || coreName.replace("ё", "е").equalsIgnoreCase(name)) {
                cores.add(core);
            }
        }

        if (cores.isEmpty()) {
            throw new IllegalArgumentException("Core class not found!");
        } else {
            return cores;
        }
    }

    @Override
    protected Set<WeaponCore> getCoresByTip(String tip) {
        return switch (tip) {
            case "арбалет" -> Set.of(LIGHTWEIGHT_CROSSBOW, HAND_CROSSBOW, HEAVY_CROSSBOW);
            case "меч" -> Set.of(GREATSWORD, LONG_SWORD, SHORT_SWORD);
            case "лук" -> Set.of(LONG_BOW, SHORT_BOW);
            case "одноручное рукопашное оружие" -> Set.of(
                    MAGIC_STAFF,
                    MACE,
                    CLUB,
                    DAGGER,
                    SPEAR,
                    LIGHTWEIGHT_HAMMER,
                    THROWING_SPEAR,
                    HAND_AXE,
                    SICKLE, DART, SLING,
                    COMBAT_PICKAXE,
                    COMBAT_HAMMER,
                    COMBAT_AXE,
                    GRATESPEAR,
                    LONG_SWORD,
                    WHIP,
                    SHORT_SWORD,
                    MORGENSTERN,
                    RAPIER,
                    SCIMITAR,
                    TRIDENT,
                    CHAIN);
            case "топор" -> Set.of(HAND_AXE, COMBAT_AXE);
            default -> defaultMatchingWithCoreName(tip);
        };
    }

    private Set<WeaponCore> defaultMatchingWithCoreName(String tip) {
        return getCoreAllCores()
                .stream()
                .filter(weaponCore -> weaponCore.getName().equalsIgnoreCase(tip))
                .collect(Collectors.toSet());
    }

    @Override
    protected Set<WeaponCore> getCoreAllCores() {
        return Arrays
                .stream(WeaponCore.values())
                .collect(Collectors.toSet());
    }
}