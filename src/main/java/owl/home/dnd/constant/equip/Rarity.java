package owl.home.dnd.constant.equip;


import lombok.Getter;

import java.util.Arrays;


@Getter
public enum Rarity {
    NONE(0, ""),
    BASIC(1, "Обычный"),
    UNUSUAL(2, "Необычный"),
    RARE(3, "Редкий"),
    VERY_RARE(4, "Очень редкий"),
    LEGENDARY(5, "Легендарный"),
    ARTEFACT(6, "Артефакт"),
    VOLATILE(7, "Варьируется");

    private final int id;

    private final String name;

    Rarity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public static Rarity fromId(int id) {
        return Arrays.stream(Rarity.values())
                .filter(constant -> constant.id == id)
                .findFirst()
                .orElse(null);
    }
}