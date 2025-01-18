package owl.home.dnd.entitys;

import owl.home.dnd.constant.armor.ArmorCore;
import owl.home.dnd.constant.weapon.WeaponCore;

import java.util.Set;
import java.util.stream.Collectors;

public class Armor extends Equipment {
    private Set<Integer> armorCores;

    public Set<ArmorCore> getArmorCores() {
        return armorCores
                .stream()
                .map(ArmorCore::fromId)
                .collect(Collectors.toSet());
    }

    public void setArmorCores(Set<ArmorCore> armorCores) {
        this.armorCores = armorCores
                .stream()
                .map(ArmorCore::getId)
                .collect(Collectors.toSet());
    }
}
