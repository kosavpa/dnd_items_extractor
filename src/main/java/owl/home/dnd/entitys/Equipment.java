package owl.home.dnd.entitys;


import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import owl.home.dnd.constant.equip.Currency;
import owl.home.dnd.constant.equip.Rarity;

import java.util.Optional;
import java.util.UUID;


@Getter
@Setter
@MappedSuperclass

@EqualsAndHashCode(exclude = {"id"})
public abstract class Equipment {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name = "ID")
    private UUID id;

    @Column(name = "EQ_NAME")
    private String name;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @Column(name = "MIN_AMOUNT")
    private Integer minAmount;

    @Column(name = "MAX_AMOUNT")
    private Integer maxAmount;

    @Column(name = "CURRENCY")
    private Integer currency;

    @Column(name = "RARITY")
    private Integer rarity;

    @Column(name = "IS_NEED_PREPARED")
    private boolean isNeedPrepared;

    @Column(name = "LINK")
    private String link;

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