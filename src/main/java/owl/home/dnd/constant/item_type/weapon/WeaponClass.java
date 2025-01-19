package owl.home.dnd.constant.item_type.weapon;


import lombok.Getter;


@Getter
public enum WeaponClass {
    SIMPLE_MELEE("Простое рукопашное оружие"),
    SIMPLE_RANGE("Простое дальнобойное оружие"),
    WARRIOR_MELEE("Воинское рукопашное оружие"),
    WARRIOR_RANGE("Воинское дальнобойное оружие");

    private final String info;

    WeaponClass(String info) {
        this.info = info;
    }
}