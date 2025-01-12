package owl.home.dnd.constant.properties.damage;


import lombok.Getter;


@Getter
public enum DamageType {
    CRUSHING("Дробящий"), PICKING("Колющий"), CHOPPING("Рубящий"), NONE("");
    private final String description;

    DamageType(String description) {
        this.description = description;
    }
}