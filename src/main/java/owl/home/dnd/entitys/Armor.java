package owl.home.dnd.entitys;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import owl.home.dnd.constant.item_type.armor.ArmorCore;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Entity(name = "owl$Armor")
@Table(name = "ARMOR")
public class Armor extends Equipment {
    private Set<Integer> armorCores;

    public Set<ArmorCore> getArmorCores() {
        return Optional
                .ofNullable(this.armorCores)
                .map(Collection::stream)
                .map(integerStream -> integerStream.map(ArmorCore::fromId).collect(Collectors.toSet()))
                .orElse(new HashSet<>());
    }

    public void setArmorCores(Set<ArmorCore> armorCores) {
        this.armorCores = armorCores
                .stream()
                .map(ArmorCore::getId)
                .collect(Collectors.toSet());
    }
}
