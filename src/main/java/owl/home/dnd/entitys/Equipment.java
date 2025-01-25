package owl.home.dnd.entitys;


import lombok.Getter;
import lombok.Setter;
import owl.home.dnd.constant.equip.Currency;
import owl.home.dnd.constant.equip.Rarity;

import java.util.Optional;


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