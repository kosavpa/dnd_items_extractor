package owl.home.dnd.constant.weapon;


import lombok.Getter;
import owl.home.dnd.constant.equip.Dice;
import owl.home.dnd.constant.properties.PropertyHolder;
import owl.home.dnd.constant.properties.damage.Damage;
import owl.home.dnd.constant.properties.damage.DamageType;
import owl.home.dnd.constant.properties.weapon.*;

import java.util.Arrays;
import java.util.Set;

import static owl.home.dnd.constant.equip.Dice.*;
import static owl.home.dnd.constant.properties.damage.DamageType.*;
import static owl.home.dnd.constant.weapon.WeaponClass.*;


@Getter
public enum WeaponCore {
    //Простое рукопашное оружие
    MAGIC_STAFF(
            0,
            "Боевой посох",
            new Damage(D6, 1, CRUSHING),
            Set.of(new Universal(new Damage(D8, 1, CRUSHING))),
            SIMPLE_MELEE),
    MACE(
            1,
            "Булава",
            new Damage(D4, 1, CRUSHING), Set.of(new None()),
            SIMPLE_MELEE),
    CLUB(
            2,
            "Дубинка",
            new Damage(D4, 1, CRUSHING),
            Set.of(new Lung()),
            SIMPLE_MELEE),
    DAGGER(
            3,
            "Кинжал",
            new Damage(D4, 1, PICKING),
            Set.of(new Lung(), new Throwing(20, 60), new Fencing()),
            SIMPLE_MELEE),
    SPEAR(
            4,
            "Копьё",
            new Damage(D6, 1, PICKING),
            Set.of(
                    new Throwing(20, 60),
                    new Universal(new Damage(D8, 1, PICKING))),
            SIMPLE_MELEE),
    LIGHTWEIGHT_HAMMER(
            5,
            "Лёгкий молот",
            new Damage(D4, 1, CRUSHING),
            Set.of(new Lung()),
            SIMPLE_MELEE),
    THROWING_SPEAR(
            6,
            "Метательное копьё",
            new Damage(D6, 1, PICKING),
            Set.of(new Throwing(30, 120)),
            SIMPLE_MELEE),
    MACE_PRO(
            7,
            "Палица",
            new Damage(D8, 1, CRUSHING),
            Set.of(new TwoHands()),
            SIMPLE_MELEE),
    HAND_AXE(
            8,
            "Ручной топор",
            new Damage(D6, 1, CHOPPING),
            Set.of(new Lung(), new Throwing(20, 60)),
            SIMPLE_MELEE),
    SICKLE(
            9,
            "Серп",
            new Damage(D4, 1, CHOPPING),
            Set.of(new Lung()),
            SIMPLE_MELEE),
    //Простое дальнобойное оружие
    LIGHTWEIGHT_CROSSBOW(
            10,
            "Арбалет, легкий",
            new Damage(D8, 1, PICKING),
            Set.of(new Ammunition(80, 320), new TwoHands(), new Reloaded()),
            SIMPLE_RANGE),
    DART(
            11,
            "Дротик",
            new Damage(D4, 1, PICKING),
            Set.of(new Throwing(20, 60), new Fencing()),
            SIMPLE_RANGE),
    SHORT_BOW(
            12,
            "Короткий лук",
            new Damage(D6, 1, PICKING),
            Set.of(new Ammunition(80, 320), new TwoHands()),
            SIMPLE_RANGE),
    SLING(
            13,
            "Праща",
            new Damage(D4, 1, CRUSHING),
            Set.of(new Ammunition(30, 120)),
            SIMPLE_RANGE),
    //Воинское рукопашное оружие
    HELBERD(
            14,
            "Алебарда",
            new Damage(D10, 1, CHOPPING),
            Set.of(new TwoHands(), new Accessibility(), new Heavy()),
            WARRIOR_MELEE),
    COMBAT_PICKAXE(
            15,
            "Боевая кирка",
            new Damage(D8, 1, PICKING),
            Set.of(new None()),
            WARRIOR_MELEE),
    COMBAT_HAMMER(
            17,
            "Боевой молот",
            new Damage(D8, 1, CRUSHING),
            Set.of(new Universal(new Damage(D10, 1, CRUSHING))),
            WARRIOR_MELEE),
    COMBAT_AXE(
            16,
            "Боевой топор",
            new Damage(D8, 1, CHOPPING),
            Set.of(new Universal(new Damage(D10, 1, CHOPPING))),
            WARRIOR_MELEE),
    GLAIVE(
            18,
            "Глефа",
            new Damage(D10, 1, CHOPPING),
            Set.of(new TwoHands(), new Accessibility(), new Heavy()),
            WARRIOR_MELEE),
    GREATSWORD(
            19,
            "Двуручный меч",
            new Damage(D6, 2, CHOPPING),
            Set.of(new TwoHands(), new Heavy()),
            WARRIOR_MELEE),
    GRATESPEAR(
            20,
            "Длинное копьё",
            new Damage(D12, 1, PICKING),
            Set.of(new Accessibility(), new Special()),
            WARRIOR_MELEE),
    LONG_SWORD(
            21,
            "Длинный меч",
            new Damage(D8, 1, CHOPPING),
            Set.of(new Universal(new Damage(D10, 1, CHOPPING))),
            WARRIOR_MELEE),
    WHIP(
            22,
            "Кнут",
            new Damage(D4, 1, CHOPPING),
            Set.of(new Accessibility(), new Fencing()),
            WARRIOR_MELEE),
    SHORT_SWORD(
            23,
            "Короткий меч",
            new Damage(D6, 1, PICKING),
            Set.of(new Lung(), new Fencing()),
            WARRIOR_MELEE),
    HAMMER(
            24,
            "Молот",
            new Damage(D6, 2, CRUSHING),
            Set.of(new TwoHands(), new Heavy()),
            WARRIOR_MELEE),
    MORGENSTERN(
            25,
            "Моргенштерн",
            new Damage(D8, 1, PICKING),
            Set.of(new None()),
            WARRIOR_MELEE),
    PIKA(
            26,
            "Пика",
            new Damage(D10, 1, PICKING),
            Set.of(new TwoHands(), new Accessibility(), new Heavy()),
            WARRIOR_MELEE),
    RAPIER(
            27,
            "Рапира",
            new Damage(D8, 1, PICKING),
            Set.of(new Fencing()),
            WARRIOR_MELEE),
    AX(
            28,
            "Секира",
            new Damage(D12, 1, CHOPPING),
            Set.of(new TwoHands(), new Heavy()),
            WARRIOR_MELEE),
    SCIMITAR(
            29,
            "Скимитар",
            new Damage(D6, 1, CHOPPING),
            Set.of(new Lung(), new Fencing()),
            WARRIOR_MELEE),
    TRIDENT(
            30,
            "Трезубец",
            new Damage(D6, 1, PICKING),
            Set.of(
                    new Throwing(20, 60),
                    new Universal(new Damage(D8, 1, PICKING))),
            WARRIOR_MELEE),
    CHAIN(
            31,
            "Цеп",
            new Damage(D8, 1, CRUSHING),
            Set.of(new None()),
            WARRIOR_MELEE),
    //Воинское дальнобойное оружие
    HAND_CROSSBOW(
            32,
            "Арбалет, ручной",
            new Damage(D6, 1, PICKING),
            Set.of(new Ammunition(20, 60), new Reloaded(), new Lung()),
            WARRIOR_RANGE),
    HEAVY_CROSSBOW(
            33,
            "Арбалет, тяжелый",
            new Damage(D10, 1, PICKING),
            Set.of(
                    new TwoHands(),
                    new Ammunition(100, 400),
                    new Heavy(),
                    new Reloaded()),
            WARRIOR_RANGE),
    LONG_BOW(
            34,
            "Длинный лук",
            new Damage(D8, 1, PICKING),
            Set.of(new Ammunition(150, 600), new Heavy(), new TwoHands()),
            WARRIOR_RANGE),
    BLOWPIPE(
            35,
            "Духовая трубка",
            new Damage(D6, 1, PICKING),
            Set.of(new Ammunition(25, 100), new Reloaded()),
            WARRIOR_RANGE),
    NET(
            36,
            "Сеть",
            new Damage(Dice.NONE, 0, DamageType.NONE),
            Set.of(new Throwing(25, 10), new Special()),
            WARRIOR_RANGE);

    private final int id;

    private final String name;

    private final Damage damage;

    private final Set<PropertyHolder> weaponProperties;

    private final WeaponClass weaponClass;

    WeaponCore(int id,
               String name,
               Damage damage,
               Set<PropertyHolder> weaponProperties,
               WeaponClass weaponClass) {
        this.id = id;
        this.name = name;
        this.damage = damage;
        this.weaponProperties = weaponProperties;
        this.weaponClass = weaponClass;
    }

    public static WeaponCore fromId(int id) {
        return Arrays.stream(WeaponCore.values())
                .filter(constant -> constant.id == id)
                .findFirst()
                .orElse(null);
    }
}