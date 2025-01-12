package owl.home.dnd.constant.equip;


import lombok.Getter;

import java.util.Arrays;


@Getter
public enum Currency {
    GOLD(0, "зм"),
    PLATINUM(1, "пм"),
    ELECTRUM(2, "эм"),
    SILVER(3, "см"),
    COPPER(4, "мм");

    private final int id;

    private final String name;

    Currency(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Currency fromId(int id) {
        return Arrays.stream(Currency.values())
                .filter(constant -> constant.id == id)
                .findFirst()
                .orElse(null);
    }
}