package owl.home.dnd.constant.item_type.armor;


import lombok.Getter;


@Getter
public enum ArmorClass {
    LUNG("Лёгкий доспех"),
    MIDDLE("Средний доспех"),
    HEAVY("Тяжёлый доспех"),
    SHIELD("Щит");

    private final String description;

    ArmorClass(String description) {
        this.description = description;
    }
}