package owl.home.dnd.constant.race;


import lombok.Getter;

import java.util.Arrays;


@Getter
public enum Racy {
    AARAKOCRA("Ааракокра", 0),
    AASIMAR("Аасимар", 1),
    AUTOGNOME("Автогном", 2),
    ASTRAL_ELF("Астральный эльф", 3),
    BUGBEAR("Багбир", 4),
    VEDALKEN("Ведалкен", 5),
    VERDAN("Вердан", 6),
    SIMIC_HYBRID("Гибрид Симиков", 7),
    GITH("Гит", 8),
    DWARF("Дварф", 9),
    GRUNG("Грунг", 10),
    GOLIATH("Голиаф", 11),
    GOBLIN("Гоблин", 12),
    GNOME("Гном", 13),
    GENASI("Дженази", 14),
    DRAGONBORN("Драконорождённый", 15),
    HARENGON("Зайцегон", 16),
    KALASHTAR("Калаштар", 17),
    KENDER("Кендер", 19),
    LEONIN("Леонин", 20),
    WARFORGED("Кованый", 21),
    KOBOLD("Кобольд", 22),
    CENTAUR("Кентавр", 23),
    KENKU("Кенку", 24),
    LOCATHAH("Локата", 25),
    LOXODON("Локсодон", 26),
    LIZARDFOLK("Людоящер", 27),
    MINOTAUR("Минотавр", 28),
    ORC("Орк", 29),
    SATYR("Сатир", 30),
    HALF_ELF("Полуэльф", 31),
    HALFLING("Полурослик", 32),
    HALF_ORC("Полуорк", 33),
    PLASMOID("Плазмоид", 34),
    OWLIN("Совлин", 35),
    TABAXI("Табакси", 36),
    TIEFLING("Тифлинг", 37),
    TORTLE("Тортл", 38),
    THRI_KREEN("Три-крин", 39),
    HOBGOBLIN("Хобгоблин", 40),
    HADOZEE("Хадози", 41),
    FAIRY("Фэйри", 42),
    FIRBOLG("Фирболг", 43),
    TRITON("Тритон", 44),
    CHANGELING("Чейнджлинг", 45),
    SHIFTER("Шифтер", 46),
    ELF("Эльф", 47),
    YUAN_TI("Юань-ти", 48);

    private final int id;
    private final String name;

    Racy(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public static Racy fromId(int id) {
        return Arrays.stream(Racy.values())
                .filter(constant -> constant.id == id)
                .findFirst()
                .orElse(null);
    }
}