package owl.home.dnd.service.common.equipment;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Element;
import owl.home.dnd.entitys.Armor;
import owl.home.dnd.entitys.Equipment;
import owl.home.dnd.entitys.Weapon;
import owl.home.dnd.util.common_items.CommonUtil;
import owl.home.dnd.util.parse.JsoupUtil;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Getter
@Setter
@Builder
public class EquipmentHint<T extends Equipment> {
    private Element equipmentElement;

    private T equipment;

    private boolean hasAll;

    private boolean withTips;

    private String tips;

    private String names;

    private String exclude;

    private boolean hasExclude;

    private boolean excludeByTip;

    private boolean hasPrepared;

    private boolean hasExcludeRacys;

    private boolean hasExcludeGameClasses;

    private String racys;

    private String gameClasses;

    private String excludeRacys;

    private String excludeGameClasses;

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

    private String normolizeTips(String tips) {
        return tips
                .replace(getAllMatcherString(tips), "")
                .replace("ALL", "")
                .replace("кроме", "")
                .replace("или", ",");
    }

    private String getAllMatcherString(String tips) {
        return Optional
                .of(FOR_CLEAR_FLAG_ALL_PATTERN.matcher(tips))
                .filter(Matcher::find)
                .map(matcher -> matcher.group("all"))
                .orElse("");
    }

    public Set<String> getTipsSet() {
        return getPreparedHitSet(tips);
    }

    public Set<String> getNamesSet() {
        return getPreparedHitSet(names);
    }

    private Set<String> getPreparedHitSet(String hint) {
        return Arrays.stream(hint.toLowerCase().split(","))
                .map(String::strip)
                .filter(tip -> !tip.isBlank())
                .collect(Collectors.toSet());
    }

    public void fillDescription(Element equipmentDescription) {
        String preparedText = JsoupUtil.prepareHtmlTextFromElement(equipmentDescription);

        if (equipment instanceof Weapon) {
            fillWeapon(preparedText);
        } else if (equipment instanceof Armor) {
            fillArmor(preparedText);
        }

        fillCommon(preparedText);
    }

    private void fillCommon(String preparedText) {
        hasPrepared = CommonUtil.extractPrepared(preparedText);

        if (hasPrepared) {
            //todo расы должны разделяться символом, если их несколько, должны проходить проверку имени contains с
            //todo существующими
            racys =;
            //todo тоже что и с рассами
            gameClasses =;

            hasExcludeRacys =;

            if (hasExcludeRacys) {
                //todo тоже что и с рассами
                excludeRacys =;
            }

            hasExcludeGameClasses =;

            if (hasExcludeGameClasses) {
                //todo тоже что и с рассами
                excludeGameClasses =;
            }
        }
    }
    //todo тоже что и с рассами
    public Set<String> getRacys() {
        //todo...
        return null;
    }
    //todo тоже что и с рассами
    public Set<String> getGameClasses() {
        //todo...
        return null;
    }
    //todo тоже что и с рассами
    public Set<String> getExcludeRacys() {
        //todo...
        return null;
    }
    //todo тоже что и с рассами
    public Set<String> getExcludeGameClasses() {
        //todo...
        return null;
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
            hasExclude = true;

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
            hasAll = true;

            if (!stringBuilder.isEmpty()) {
                String normolizedTips = normolizeTips(stringBuilder.toString());

                if (!normolizedTips.isBlank()) {
                    withTips = true;

                    tips = normolizedTips;
                }
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
            hasAll = true;

            String normolizedTips = normolizeTips(joiner.toString());

            if (!normolizedTips.isBlank()) {
                withTips = true;

                tips = normolizedTips;
            }
        } else {
            names = joiner.toString();
        }
    }
}