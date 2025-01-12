package owl.home.dnd.constant.equip;


import lombok.Getter;


@Getter
public enum Dice {
    D4(4), D6(6), D8(8), D10(10), D12(12), NONE(0);

    private final int sideCount;

    Dice(int sideCount) {
        this.sideCount = sideCount;
    }
}