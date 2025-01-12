package owl.home.dnd.entitys;


import lombok.Getter;
import lombok.Setter;
import owl.home.dnd.constant.equip.Currency;
import owl.home.dnd.constant.equip.Rarity;
import owl.home.dnd.constant.game_class.GameClass;
import owl.home.dnd.constant.race.Racy;

import java.util.Optional;

@Getter
@Setter

public class Equipment {
    private String name;

    private String description;

    private Integer minAmount;

    private Integer maxAmount;

    private Integer currency;

    private Integer rarity;

    private boolean isNeedPrepared;

    private Integer preparedClass;

    private Integer preparedRacy;

    public GameClass getPreparedClass() {
        return Optional.ofNullable(this.preparedClass)
                .map(GameClass::fromId)
                .orElse(null);
    }

    public void setPreparedClass(GameClass preparedClass) {
        Optional.ofNullable(preparedClass)
                .ifPresent(c -> this.preparedClass = c.getId());
    }

    public Racy getPreparedRacy() {
        return Optional.ofNullable(this.preparedRacy)
                .map(Racy::fromId)
                .orElse(null);
    }

    public void setPreparedRacy(Racy preparedRacy) {
        Optional.ofNullable(preparedRacy)
                .ifPresent(r -> this.preparedRacy = r.getId());
    }

    public Currency getCurrency() {
        return Optional.ofNullable(this.currency)
                .map(Currency::fromId)
                .orElse(null);
    }

    public void setCurrency(Currency currency) {
        Optional.ofNullable(currency)
                .ifPresent(c -> this.currency = c.getId());
    }

    public Rarity getRarity() {
        return Optional.ofNullable(this.rarity)
                .map(Rarity::fromId)
                .orElse(null);
    }

    public void setRarity(Rarity rarity) {
        Optional.ofNullable(rarity)
                .ifPresent(r -> this.rarity = r.getId());
    }
}