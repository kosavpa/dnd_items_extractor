package owl.home.dnd.util.constant;


import java.util.regex.Pattern;

public interface Constants {
    Pattern FOR_CLEAR_FLAG_ALL_PATTERN = Pattern.compile("(?<all>люб([а-я]{2}))");

    Pattern WEAPON_MATCHER_PATTERN = Pattern
            .compile("Оружие\\s(?<weapon>(\\([а-я\\sё,]+\\))|([а-я\\sё,]+))");

    Pattern ALL_FROM_MANY_WEAPONS_PATTERN = Pattern.compile(".*(люб([а-я]{2})|или).*");

    Pattern ALL_WEAPONS_PATTERN = Pattern.compile(".*(люб([а-я]{2})).*");

    Pattern ARMOR_MATCHER_PATTERN = Pattern
            .compile("Доспех\\s(?<armor>(\\([а-я\\sё,]+\\))|([а-я\\sё,]+))");

    Pattern ALL_ARMOR_PATTERN_BY_CLASS = Pattern
            .compile("(?<lung>легк[а-я]+)|(?<middle>средн[а-я]+)|(?<heavy>тяжел[а-я]+)");

    Pattern EXCLUDE_FLAG_PATTERN = Pattern.compile("кроме\\s(?<exclude>[а-я]+)");

    String SIZE_TYPE_ALIGNMENT = "size-type-alignment";
}