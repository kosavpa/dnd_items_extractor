package owl.home.dnd.service.common.equipment;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Element;
import owl.home.dnd.constant.equip.Currency;
import owl.home.dnd.constant.equip.Rarity;
import owl.home.dnd.entitys.Armor;
import owl.home.dnd.entitys.Equipment;
import owl.home.dnd.entitys.Weapon;
import owl.home.dnd.util.parse.JsoupUtil;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static owl.home.dnd.util.constant.Constants.*;


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

    private Currency currency;

    private boolean hasNeedPrepared;

    private Rarity rarity;

    private String equipmentDescription;

    private Integer maxAmount;

    private Integer minAmount;

    private String equipmentName;

    private String equipmentLink;

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
        } else {
            throw new IllegalArgumentException();
        }
    }

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
            handleExclude(stringBuilder, excludeMatcher);
        }

        if (ALL_ARMOR_PATTERN_BY_CLASS.matcher(stringBuilder.toString()).find() || stringBuilder.isEmpty()) {
            hasAll = true;

            if (!stringBuilder.isEmpty()) {
                fillTips(stringBuilder);
            }
        } else {
            names = stringBuilder.toString();
        }
    }

    private void handleExclude(StringBuilder stringBuilder, Matcher excludeMatcher) {
        hasExclude = true;

        fillExcludeForArmor(stringBuilder, excludeMatcher);
    }

    private void fillTips(StringBuilder stringBuilder) {
        String normolizedTips = normolizeTips(stringBuilder.toString());

        if (!normolizedTips.isBlank()) {
            withTips = true;

            tips = normolizedTips;
        }
    }

    private void fillExcludeForArmor(StringBuilder stringBuilder, Matcher excludeMatcher) {
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
            handleTipsForWeapon(joiner);
        } else {
            names = joiner.toString();
        }
    }

    private void handleTipsForWeapon(StringJoiner joiner) {
        hasAll = true;

        String normolizedTips = normolizeTips(joiner.toString());

        if (!normolizedTips.isBlank()) {
            withTips = true;

            tips = normolizedTips;
        }
    }

    public void fillCommonDescription() {
        currency = extractCurrency(equipmentElement);

        equipmentName = extractName(equipmentElement);

        minAmount = extractMinAmount(equipmentElement);

        maxAmount = extractMaxAmount(equipmentElement);

        equipmentDescription = extractDescription(equipmentElement);

        rarity = extractRarity(equipmentElement);

        hasNeedPrepared = extractPrepared(equipmentElement);
    }

    public static boolean extractPrepared(Element wrapper) {
        return Optional
                .ofNullable(wrapper)
                .map(element -> JsoupUtil.getElementByClassFromElement(element, SIZE_TYPE_ALIGNMENT))
                .map(JsoupUtil::prepareHtmlTextFromElement)
                .map(rawText -> Pattern
                        .compile("требуется настройка.*")
                        .matcher(rawText))
                .map(Matcher::find)
                .orElse(false);
    }

    private Rarity extractRarity(Element wrapper) {
        return Optional
                .ofNullable(JsoupUtil.getElementByClassFromElement(wrapper, SIZE_TYPE_ALIGNMENT))
                .map(element -> Pattern
                        .compile(
                                "(?<rarity>" +
                                        "(обычн|необычн|редк|очень\\sредк|легендарн|артефакт|редкость\\sварьируется)" +
                                        "(ый|ий|ое)?)")
                        .matcher(element.text()))
                .filter(Matcher::find)
                .map(matcher -> matcher.group("rarity"))
                .map(this::extractRarityFromStr)
                .orElse(null);
    }

    private Rarity extractRarityFromStr(String strWithRarity) {
        if ("редкость варьируется".equalsIgnoreCase(strWithRarity)) {
            return Rarity.VOLATILE;
        } else {
            String preparedRarityStr = strWithRarity.substring(0, strWithRarity.length() - 2).strip();

            return Arrays
                    .stream(Rarity.values())
                    .filter(eRarity -> eRarity
                            .getName()
                            .toLowerCase()
                            .contains(preparedRarityStr))
                    .findFirst()
                    .orElseThrow();
        }
    }

    private Integer extractMaxAmount(Element wrapper) {
        return Optional
                .ofNullable(JsoupUtil.getElementByClassFromElement(wrapper, "price"))
                .map(element -> Pattern
                        .compile("[\\sа-я]:[\\sа-я]*(?<sum>[\\d-\\s]+)[\\sсмэзп]+")
                        .matcher(JsoupUtil.prepareHtmlTextFromElement(element)))
                .filter(Matcher::find)
                .map(matcher -> matcher.group("sum"))
                .filter(strSum -> strSum.split("-").length == 2)
                .map(strSum -> strSum.split("-")[1].replace(" ", ""))
                .map(Integer::valueOf)
                .orElse(null);
    }

    private Integer extractMinAmount(Element wrapper) {
        return Optional
                .ofNullable(JsoupUtil.getElementByClassFromElement(wrapper, "price"))
                .map(element -> Pattern
                        .compile("[\\sа-я]:[\\sа-я]*(?<sum>[\\d-\\s]+)[\\sсмэзп]+")
                        .matcher(JsoupUtil.prepareHtmlTextFromElement(element)))
                .filter(Matcher::find)
                .map(matcher -> matcher.group("sum"))
                .map(strSum -> strSum.split("-")[0].replace(" ", ""))
                .map(Integer::valueOf)
                .orElse(null);
    }

    private String extractDescription(Element wrapper) {
        return Optional
                .ofNullable(JsoupUtil.getElementByClassFromElement(wrapper, "subsection desc"))
                .map(Element::text)
                .orElse(null);
    }

    private String extractName(Element wrapper) {
        return Optional
                .ofNullable(JsoupUtil.getElementByClassFromElement(wrapper, "card-title"))
                .map(Element::text)
                .orElseThrow();
    }

    private Currency extractCurrency(Element wrapper) {
        return Optional
                .ofNullable(JsoupUtil.getElementByClassFromElement(wrapper, "price"))
                .map(element -> Pattern
                        .compile("[\\sа-я]:[\\sа-я]*[\\d-\\s]+(?<currency>[\\sсмэзп]+)?")
                        .matcher(JsoupUtil.prepareHtmlTextFromElement(element)))
                .filter(Matcher::find)
                .map(matcher -> matcher.group("currency"))
                .map(stringCurrency -> Arrays.stream(Currency.values())
                        .filter(strCurrency -> strCurrency.getName().equals(stringCurrency))
                        .findFirst()
                        .orElseThrow())
                .orElse(null);
    }
}