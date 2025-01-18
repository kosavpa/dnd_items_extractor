package owl.home.dnd.service.armor;


import org.apache.logging.log4j.util.Strings;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import owl.home.dnd.constant.armor.ArmorClass;
import owl.home.dnd.constant.armor.ArmorCore;
import owl.home.dnd.entitys.Armor;
import owl.home.dnd.util.common_items.CommonUtil;
import owl.home.dnd.util.exception.ExceptionUtils;
import owl.home.dnd.util.parse.JsoupUtil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class ArmorService {
    private static final Pattern ARMOR_IMG_PATTERN = Pattern.compile(".+item_type_armor");

    private static final Pattern ARMOR_MATCHER_PATTERN = Pattern
            .compile("Доспех\\s(?<armor>(\\([а-я\\sё,]+\\))|([а-я\\sё,]+))");

    private static final Pattern FOR_CLEAR_FLAG_ALL_PATTERN = Pattern.compile("(?<all>люб([а-я]{2}))");

    private static final Pattern ALL_ARMOR_PATTERN_BY_CLASS = Pattern
            .compile("(?<lung>легк[а-я]+)|(?<middle>средн[а-я]+)|(?<heavy>тяжел[а-я]+)");

    private static final Pattern EXCLUDE_FLAG_PATTERN = Pattern.compile("кроме\\s(?<exclude>[а-я]+)");
    //todo мб вынести в общий сервис
    public Set<Armor> extractArmor() {
        Document magicItems = JsoupUtil.getDocFromHref("https://dnd.su/items/");

        return JsoupUtil
                .getElementsByClassFromDoc("col list-item__spell for_filter", magicItems)
                .stream()
                .filter(this::isArmor)
                .map(this::getArmorElement)
                .filter(Objects::nonNull)
                .map(this::mapToArmor)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
    //todo мб вынести в общий сервис
    private Element getArmorElement(Element element) {
        return ExceptionUtils.wrapAndGetResultOrNull(() -> JsoupUtil.getItemFromElementHref(element));
    }
    //todo мб вынести в общий сервис
    private boolean isArmor(Element itemWrapper) {
        return JsoupUtil
                .getElementsByTag(itemWrapper, "use")
                .stream()
                .map(element -> element.attr("xlink:href"))
                .filter(Strings::isNotBlank)
                .allMatch(attr -> ARMOR_IMG_PATTERN.matcher(attr).matches());
    }
    //todo мб вынести в общий сервис
    private Armor mapToArmor(Element element) {
        return ExceptionUtils
                .wrapAndGetResultOrNull(() -> {
                    Armor weapon = new Armor();

                    weapon.setArmorCores(extractArmorCores(element));
                    weapon.setCurrency(CommonUtil.extractCurrency(element));
                    weapon.setName(CommonUtil.extractName(element));
                    weapon.setMinAmount(CommonUtil.extractMinAmount(element));
                    weapon.setMaxAmount(CommonUtil.extractMaxAmount(element));
                    weapon.setDescription(CommonUtil.extractDescription(element));
                    weapon.setRarity(CommonUtil.extractRarity(element));
                    weapon.setNeedPrepared(CommonUtil.extractPrepared(element));

                    if (weapon.isNeedPrepared()) {
                        weapon.setPreparedClass(CommonUtil.extractPreparedClass(element));
                        weapon.setPreparedRacy(CommonUtil.extractPreparedRacy(element));
                    }

                    return weapon;
                });
    }
    //todo мб вынести в общий сервис, в абстрактный метод
    private Set<ArmorCore> extractArmorCores(Element wrapper) {
        Element elementByClass = JsoupUtil.getElementByClassFromElement(wrapper, "size-type-alignment");

        if (elementByClass == null) {
            return Set.of();
        }

        String coreHint = extractArmorCoreHint(elementByClass.text());

        if (coreHint.contains("ALL")) {
            String cleanedCoreHint = cleanHint(coreHint);

            if (cleanedCoreHint.isBlank()) {
                return Arrays.stream(ArmorCore.values()).collect(Collectors.toSet());
            } else {
                return extractArmorCoresByHint(cleanedCoreHint);
            }
        } else {
            return Arrays
                    .stream(ArmorCore.values())
                    .filter(armorCore -> armorCore.getName().equalsIgnoreCase(coreHint))
                    .collect(Collectors.toSet());
        }
    }
    //todo мб вынести в общий сервис, в абстрактный метод
    private Set<ArmorCore> extractArmorCoresByHint(String hint) {
        String exclude = null;

        if (hint.contains("EXCLUDE")) {
            exclude = hint.substring(hint.indexOf("EXCLUDE"));

            hint = hint.replace(exclude, "");
        }

        Set<ArmorCore> cores = Arrays
                .stream(hint.split("\\s"))
                .map(String::strip)
                .filter(singleHint -> !singleHint.isBlank())
                .flatMap(coreHint -> getWeaponArmorCoreByHint(coreHint).stream())
                .collect(Collectors.toSet());

        if (exclude != null) {
            excludeFromCores(cores, exclude);
        }

        return cores;
    }
    //todo мб вынести в общий сервис, в абстрактный метод
    private void excludeFromCores(Set<ArmorCore> cores, String exclude) {
        String excludeHint = exclude.replace("EXCLUDE", "");

        Matcher matcher = ALL_ARMOR_PATTERN_BY_CLASS.matcher(excludeHint);

        if (matcher.find()) {
            String hint;

            if (matcher.group("lung") != null) {
                hint = "легкий";
            } else if (matcher.group("middle") != null) {
                hint = "средний";
            } else if (matcher.group("heavy") != null) {
                hint = "тяжёлый";
            } else {
                throw new IllegalArgumentException();
            }

            cores.removeAll(getWeaponArmorCoreByHint(hint));
        } else {
            String preparedExcludeHint = excludeHint.substring(0, excludeHint.length() - 4).toLowerCase();

            Set<ArmorCore> excludedCores = Arrays
                    .stream(ArmorCore.values())
                    .filter(armorCore -> armorCore.getName().toLowerCase().contains(preparedExcludeHint))
                    .collect(Collectors.toSet());

            if (excludedCores.isEmpty()) {
                throw new IllegalArgumentException();
            }

            cores.removeAll(excludedCores);
        }
    }
    //todo мб вынести в общий сервис
    private String cleanHint(String abstractClassName) {
        String stringAllMatcher = Optional
                .of(FOR_CLEAR_FLAG_ALL_PATTERN.matcher(abstractClassName))
                .filter(Matcher::find)
                .map(matcher -> matcher.group("all"))
                .orElse("");

        return abstractClassName
                .replace(stringAllMatcher, "")
                .replace("ALL", "")
                .replace("кроме", "")
                .replace("или", "")
                .replace(",", "");
    }
    //todo мб вынести в общий сервис, в абстрактный метод
    private Set<ArmorCore> getWeaponArmorCoreByHint(String hint) {
        return switch (hint.toLowerCase(Locale.ROOT)) {
            case "лёгкий", "легкий" -> getCoresByClass(ArmorClass.LUNG);
            case "средний" -> getCoresByClass(ArmorClass.MIDDLE);
            case "тяжёлый", "тяжелый" -> getCoresByClass(ArmorClass.HEAVY);
            default -> throw new IllegalArgumentException();
        };
    }

    private Set<ArmorCore> getCoresByClass(ArmorClass armorClass) {
        return Arrays
                .stream(ArmorCore.values())
                .filter(armorCore -> armorClass.equals(armorCore.getArmorClass()))
                .collect(Collectors.toSet());
    }
    //todo мб вынести в общий сервис, в абстрактный метод
    private String extractArmorCoreHint(String text) {
        Matcher weaponMatcher = ARMOR_MATCHER_PATTERN.matcher(text);

        StringBuilder stringBuilder = new StringBuilder();

        if (weaponMatcher.find()) {
            stringBuilder
                    .append(weaponMatcher
                            .group("armor")
                            .replace("(", "")
                            .replace(")", ""));
        }

        if (ALL_ARMOR_PATTERN_BY_CLASS.matcher(stringBuilder.toString()).find()) {
            stringBuilder.append("ALL");
        }

        Matcher excludeMatcher = EXCLUDE_FLAG_PATTERN.matcher(stringBuilder.toString());

        if (excludeMatcher.find()) {
            String exclude = excludeMatcher.group("exclude");

            int indexOfExclude = stringBuilder.indexOf(exclude);

            stringBuilder
                    .replace(indexOfExclude, indexOfExclude + exclude.length(), "")
                    .append("EXCLUDE")
                    .append(exclude);
        }

        return stringBuilder.isEmpty() ? "ALL" : stringBuilder.toString();
    }
}