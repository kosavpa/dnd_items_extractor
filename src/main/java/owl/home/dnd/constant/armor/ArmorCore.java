package owl.home.dnd.constant.armor;


import lombok.Getter;
import owl.home.dnd.constant.properties.kd.AgilityModifier;
import owl.home.dnd.constant.properties.kd.ForceLimit;
import owl.home.dnd.constant.properties.kd.Kd;

import java.util.Arrays;


@Getter
public enum ArmorCore {
    //Лёгкий доспех
    QUILTED(
            1,
            "Стеганный",
            ArmorClass.LUNG,
            new Kd(11, new AgilityModifier(-1)),
            new ForceLimit(0),
            true),
    LEATHER(
            2,
            "Кожаный",
            ArmorClass.LUNG,
            new Kd(11, new AgilityModifier(-1)),
            new ForceLimit(0),
            false),
    STUDDED_LEATHER(
            3,
            "Проклепанный кожаный",
            ArmorClass.LUNG,
            new Kd(12, new AgilityModifier(-1)),
            new ForceLimit(0),
            false),
    //Средний доспех
    SELFISH(
            4,
            "Шкурный",
            ArmorClass.MIDDLE,
            new Kd(12, new AgilityModifier(-1)),
            new ForceLimit(0),
            false),
    CHAINMAIL_SHIRT(
            5,
            "Кольчужная рубаха",
            ArmorClass.MIDDLE,
            new Kd(13, new AgilityModifier(2)),
            new ForceLimit(0),
            false),
    SCALY(
            6,
            "Чешуйчатый",
            ArmorClass.MIDDLE,
            new Kd(14, new AgilityModifier(2)),
            new ForceLimit(0),
            true),
    BREASTPLATE(
            7,
            "Кираса",
            ArmorClass.MIDDLE,
            new Kd(14, new AgilityModifier(2)),
            new ForceLimit(0),
            false),
    HALF_PLATES(
            8,
            "Полулаты",
            ArmorClass.MIDDLE,
            new Kd(15, new AgilityModifier(2)),
            new ForceLimit(0),
            true),
    //Тяжёлый доспех
    RING(
            9,
            "Колечный",
            ArmorClass.HEAVY,
            new Kd(14, new AgilityModifier(2)),
            new ForceLimit(0),
            true),
    CHAIN_MASK(
            10,
            "Кольчуга",
            ArmorClass.HEAVY,
            new Kd(16, new AgilityModifier(2)),
            new ForceLimit(0),
            true),
    SET(
            11,
            "Набоный",
            ArmorClass.HEAVY,
            new Kd(17, new AgilityModifier(2)),
            new ForceLimit(0),
            true),
    PLATES(
            12,
            "Латы",
            ArmorClass.HEAVY,
            new Kd(18, new AgilityModifier(2)),
            new ForceLimit(0),
            true),
    //Щит
    SHIELD(
            13,
            "Щит",
            ArmorClass.SHIELD,
            new Kd(12, new AgilityModifier(2)),
            new ForceLimit(0),
            false);

    private final int id;

    private final String name;

    private final ArmorClass armorClass;

    private final Kd kd;

    private final ForceLimit forceLimit;

    private final boolean stealthHindrance;

    ArmorCore(int id, String name, ArmorClass armorClass, Kd kd, ForceLimit forceLimit, boolean stealthHindrance) {
        this.id = id;
        this.name = name;
        this.armorClass = armorClass;
        this.kd = kd;
        this.forceLimit = forceLimit;
        this.stealthHindrance = stealthHindrance;
    }

    public static ArmorCore fromId(int id) {
        return Arrays.stream(ArmorCore.values())
                .filter(constant -> constant.id == id)
                .findFirst()
                .orElse(null);
    }
}