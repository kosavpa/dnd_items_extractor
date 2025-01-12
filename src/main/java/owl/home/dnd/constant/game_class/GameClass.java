package owl.home.dnd.constant.game_class;


import lombok.Getter;

import java.util.Arrays;


@Getter
public enum GameClass {
    Bard("Бард", 0),
    BARBARIAN("Варвар", 1),
    FIGHTER("Воин", 2),
    WIZAR("Волшебник", 3),
    DRUI("Друид", 4),
    CLERI("Жрец", 5),
    ROGU("Плут", 6),
    RANGE("Следопыт", 7),
    ARTIFICE("Изобретатель", 8),
    WARLOC("Колдун", 9),
    SORCERE("Чародей", 10),
    MON("Монах", 11),
    PALADIN("Паладин", 12);

    private final int id;
    private final String name;

    GameClass(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public static GameClass fromId(int id) {
        return Arrays.stream(GameClass.values())
                .filter(constant -> constant.id == id)
                .findFirst()
                .orElse(null);
    }
}