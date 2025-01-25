package owl.home.dnd.entitys;


import lombok.Getter;
import lombok.Setter;
import owl.home.dnd.constant.equip.Currency;
import owl.home.dnd.constant.equip.Rarity;
import owl.home.dnd.constant.game_class.GameClass;
import owl.home.dnd.constant.race.Racy;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@Setter
public abstract class Equipment {
    private String name;

    private String description;

    private Integer minAmount;

    private Integer maxAmount;

    private Integer currency;

    private Integer rarity;

    private boolean isNeedPrepared;

    private Set<Integer> preparedClass;

    private Set<Integer> preparedRacy;

    public Set<GameClass> getPreparedClass() {
        return Optional
                .ofNullable(this.preparedClass)
                .map(Collection::stream)
                .map(integerStream -> integerStream.map(GameClass::fromId).collect(Collectors.toSet()))
                .orElse(new HashSet<>());
    }

    public void setPreparedClass(Set<GameClass> preparedClass) {
        this.preparedClass = preparedClass
                .stream()
                .map(GameClass::getId)
                .collect(Collectors.toSet());
    }

    public Set<Racy> getPreparedRacy() {
        return Optional
                .ofNullable(this.preparedRacy)
                .map(Collection::stream)
                .map(integerStream -> integerStream.map(Racy::fromId).collect(Collectors.toSet()))
                .orElse(new HashSet<>());
    }

    public void setPreparedRacy(Set<Racy> preparedRacy) {
        this.preparedRacy = preparedRacy
                .stream()
                .map(Racy::getId)
                .collect(Collectors.toSet());
    }

    public Currency getCurrency() {
        return Optional
                .ofNullable(this.currency)
                .map(Currency::fromId)
                .orElse(null);
    }

    public void setCurrency(Currency currency) {
        Optional
                .ofNullable(currency)
                .ifPresent(c -> this.currency = c.getId());
    }

    public Rarity getRarity() {
        return Optional
                .ofNullable(this.rarity)
                .map(Rarity::fromId)
                .orElse(null);
    }

    public void setRarity(Rarity rarity) {
        Optional
                .ofNullable(rarity)
                .ifPresent(r -> this.rarity = r.getId());
    }
}