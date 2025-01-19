package owl.home.dnd.service.common.equipment;


import lombok.Builder;
import lombok.Data;
import org.jsoup.nodes.Element;
import owl.home.dnd.entitys.Armor;
import owl.home.dnd.entitys.Equipment;
import owl.home.dnd.entitys.Weapon;
import owl.home.dnd.util.parse.JsoupUtil;

import java.util.Optional;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Data
@Builder
public class EquipmentHint<T extends Equipment> {
    private Element equipmentElement;

    private T equipment;

    private boolean isAll;

    private boolean withTips;

    private String tips;

    private String names;

    private String exclude;

    private boolean isExclude;

    private boolean excludeByTip;

    private static final Pattern FOR_CLEAR_FLAG_ALL_PATTERN = Pattern.compile("(?<all>люб([а-я]{2}))");

    private static final Pattern WEAPON_MATCHER_PATTERN = Pattern
            .compile("Оружие\\s(?<weapon>(\\([а-я\\sё,]+\\))|([а-я\\sё,]+))");

    private static final Pattern ALL_FROM_MANY_WEAPONS_PATTERN = Pattern.compile(".*(люб([а-я]{2})|или).*");

    private static final Pattern ALL_WEAPONS_PATTERN = Pattern.compile(".*(люб([а-я]{2})).*");

    private static final Pattern ARMOR_MATCHER_PATTERN = Pattern
            .compile("Доспех\\s(?<armor>(\\([а-я\\sё,]+\\))|([а-я\\sё,]+))");

    private static final Pattern ALL_ARMOR_PATTERN_BY_CLASS = Pattern
            .compile("(?<lung>легк[а-я]+)|(?<middle>средн[а-я]+)|(?<heavy>тяжел[а-я]+)");

    private static final Pattern EXCLUDE_FLAG_PATTERN = Pattern.compile("кроме\\s(?<exclude>[а-я]+)");

    //todo мыжет быть убирать пробелы здесь
    private void normolizeTips() {
        tips = tips
                .replace(getAllMatcherString(), "")
                .replace("ALL", "")
                .replace("кроме", "")
                .replace("или", ",");
    }

    private String getAllMatcherString() {
        return Optional
                .of(FOR_CLEAR_FLAG_ALL_PATTERN.matcher(tips))
                .filter(Matcher::find)
                .map(matcher -> matcher.group("all"))
                .orElse("");
    }

    public String[] getTipsArray() {
        normolizeTips();

        return tips.replace(" ", "").toLowerCase().split(",");
    }

    public String[] getNamesArray() {
        return names.replace(" ", "").toLowerCase().split(",");
    }

    public void fillDescription(Element equipmentDescription) {
        if (equipment instanceof Weapon) {
            fillWeapon(JsoupUtil.prepareHtmlTextFromElement(equipmentDescription));
        } else if (equipment instanceof Armor) {
            fillArmor(JsoupUtil.prepareHtmlTextFromElement(equipmentDescription));
        } else {
            throw new IllegalArgumentException();
        }
    }
    //todo рефакторинг метода
    private void fillArmor(String text) {
        Matcher weaponMatcher = ARMOR_MATCHER_PATTERN.matcher(text);

        StringBuilder stringBuilder = new StringBuilder();

        if (weaponMatcher.find()) {
            stringBuilder
                    .append(weaponMatcher
                            .group("armor")
                            .replace("(", "")
                            .replace(")", ""));
        }

        Matcher excludeMatcher = EXCLUDE_FLAG_PATTERN.matcher(stringBuilder.toString());

        if (excludeMatcher.find()) {
            isExclude = true;

            String excludeFromGroup = getExcludeFromGroupAndCleanSBuilder(stringBuilder, excludeMatcher);

            Matcher excludeTipMatcher = ALL_ARMOR_PATTERN_BY_CLASS.matcher(excludeFromGroup);

            if (excludeTipMatcher.find()) {
                excludeByTip = true;

                if (excludeTipMatcher.group("lung") != null) {
                    exclude = "легкий";
                } else if (excludeTipMatcher.group("middle") != null) {
                    exclude = "средний";
                } else if (excludeTipMatcher.group("heavy") != null) {
                    exclude = "тяжёлый";
                } else {
                    throw new IllegalArgumentException();
                }
            } else {
                exclude = excludeFromGroup.substring(0, excludeFromGroup.length() - 4).toLowerCase();
            }
        }

        if (ALL_ARMOR_PATTERN_BY_CLASS.matcher(stringBuilder.toString()).find() || stringBuilder.isEmpty()) {
            isAll = true;

            if (!stringBuilder.isEmpty()) {
                withTips = true;

                tips = stringBuilder.toString();
            }
        } else {
            names = stringBuilder.toString();
        }
    }

    private String getExcludeFromGroupAndCleanSBuilder(StringBuilder stringBuilder, Matcher excludeMatcher) {
        String excludeFromGroup = excludeMatcher.group("exclude");

        int indexOfExclude = stringBuilder.indexOf(excludeFromGroup);

        stringBuilder.replace(indexOfExclude, indexOfExclude + excludeFromGroup.length(), "");

        return excludeFromGroup;
    }

    private void fillWeapon(String text) {
        Matcher weaponMatcher = WEAPON_MATCHER_PATTERN.matcher(text);

        StringJoiner joiner = new StringJoiner("");

        if (weaponMatcher.find()) {
            joiner.add(weaponMatcher
                    .group("weapon")
                    .replace("(", "")
                    .replace(")", ""));
        }

        Matcher allMatcher;

        if (joiner.length() != 0) {
            allMatcher = ALL_FROM_MANY_WEAPONS_PATTERN.matcher(joiner.toString());
        } else {
            allMatcher = ALL_WEAPONS_PATTERN.matcher(text);
        }

        if (allMatcher.find() || joiner.length() == 0) {
            isAll = true;

            if (joiner.length() != 0) {
                withTips = true;

                tips = joiner.toString();
            }
        } else {
            names = joiner.toString();
        }
    }
}