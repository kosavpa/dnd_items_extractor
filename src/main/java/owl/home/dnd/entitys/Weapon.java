package owl.home.dnd.entitys;


import owl.home.dnd.constant.item_type.weapon.WeaponCore;

import java.util.Set;
import java.util.stream.Collectors;


public class Weapon extends Equipment {
    private Set<Integer> weaponCores;

    public Set<WeaponCore> getWeaponCores() {
        return weaponCores
                .stream()
                .map(WeaponCore::fromId)
                .collect(Collectors.toSet());
    }

    public void setWeaponCores(Set<WeaponCore> weaponCores) {
        this.weaponCores = weaponCores
                .stream()
                .map(WeaponCore::getId)
                .collect(Collectors.toSet());
    }
}