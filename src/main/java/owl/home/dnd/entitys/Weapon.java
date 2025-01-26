package owl.home.dnd.entitys;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import owl.home.dnd.constant.item_type.weapon.WeaponCore;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Entity(name = "owl$Weapon")
@Table(name = "WEAPON")
public class Weapon extends Equipment {
    private Set<Integer> weaponCores;

    public Set<WeaponCore> getWeaponCores() {
        return Optional
                .ofNullable(this.weaponCores)
                .map(Collection::stream)
                .map(integerStream -> integerStream.map(WeaponCore::fromId).collect(Collectors.toSet()))
                .orElse(new HashSet<>());
    }

    public void setWeaponCores(Set<WeaponCore> weaponCores) {
        this.weaponCores = weaponCores
                .stream()
                .map(WeaponCore::getId)
                .collect(Collectors.toSet());
    }
}