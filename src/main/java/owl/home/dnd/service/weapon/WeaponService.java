package owl.home.dnd.service.weapon;


import org.apache.logging.log4j.util.Strings;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import owl.home.dnd.constant.weapon.WeaponCore;
import owl.home.dnd.entitys.Weapon;
import owl.home.dnd.util.common_items.CommonUtil;
import owl.home.dnd.util.exception.ExceptionUtils;
import owl.home.dnd.util.parse.JsoupUtil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static owl.home.dnd.constant.weapon.WeaponCore.*;


public class WeaponService {
    private static final Pattern WEAPON_IMG_PATTERN = Pattern.compile(".+item_type_weapon");

    private static final Pattern WEAPON_MATCHER_PATTERN = Pattern
            .compile("Оружие\\s(?<weapon>(\\([а-я\\sё,]+\\))|([а-я\\sё,]+))");

    private static final Pattern FOR_CLEAR_FLAG_ALL_PATTERN = Pattern.compile(".*(?<all>люб([а-я]{2})).*");

    private static final Pattern ALL_FROM_MANY_WEAPONS_PATTERN = Pattern.compile(".*(люб([а-я]{2})|или).*");

    private static final Pattern ALL_WEAPONS_PATTERN = Pattern.compile(".*(люб([а-я]{2})).*");
    //todo мб вынести в общий сервис
    public Set<Weapon> extractWeapons() {
        Document magicItems = JsoupUtil.getDocFromHref("https://dnd.su/items/");

        return JsoupUtil
                .getElementsByClassFromDoc("col list-item__spell for_filter", magicItems)
                .stream()
                .filter(this::isWeapon)
                .map(this::getWeaponElement)
                .filter(Objects::nonNull)
                .map(this::mapToWeapon)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
    //todo мб вынести в общий сервис
    private Element getWeaponElement(Element element) {
        return ExceptionUtils.wrapAndGetResultOrNull(() -> JsoupUtil.getItemFromElementHref(element));
    }
    //todo мб вынести в общий сервис
    private boolean isWeapon(Element itemWrapper) {
        return JsoupUtil
                .getElementsByTag(itemWrapper, "use")
                .stream()
                .map(element -> element.attr("xlink:href"))
                .filter(Strings::isNotBlank)
                .allMatch(attr -> WEAPON_IMG_PATTERN.matcher(attr).matches());

    }
    //todo мб вынести в общий сервис для предмета
    private Weapon mapToWeapon(Element element) {
        return ExceptionUtils
                .wrapAndGetResultOrNull(() -> {
                    Weapon weapon = new Weapon();

                    weapon.setWeaponCores(extractWeaponCores(element));
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
    private Set<WeaponCore> extractWeaponCores(Element wrapper) {
        Element elementByClass = JsoupUtil.getElementByClassFromElement(wrapper, "size-type-alignment");

        if (elementByClass == null) {
            return Set.of();
        }

        String abstractClassName = extractWeaponAbstractClassName(prepareHtmlText(elementByClass.text()));

        if (abstractClassName.contains("ALL")) {
            String clearedAbstractName = clearAllFlag(abstractClassName);

            if (clearedAbstractName.isBlank()) {
                return Arrays.stream(WeaponCore.values()).collect(Collectors.toSet());
            } else {
                return Arrays
                        .stream(clearedAbstractName.split(","))
                        .map(String::strip)
                        .flatMap(preparedAbstractName -> getWeaponCoresByClass(preparedAbstractName).stream())
                        .collect(Collectors.toSet());
            }
        } else {
            return Arrays
                    .stream(abstractClassName.split(","))
                    .map(this::extractWeaponCore)
                    .collect(Collectors.toSet());
        }
    }
    //todo вынести в утиль
    private String prepareHtmlText(String text) {
        return text.replace((char) 8201, (char) 32);
    }
    //todo мб вынести в общий сервис, в абстрактный метод
    private WeaponCore extractWeaponCore(String splitAbstractClassName) {
        String strippedClassName = splitAbstractClassName.strip();

        for (WeaponCore core : WeaponCore.values()) {
            String coreName = core.getName();

            if (coreName.equalsIgnoreCase(strippedClassName)
                    || coreName.replace("ё", "е").equalsIgnoreCase(strippedClassName)) {
                return core;
            }
        }

        throw new IllegalArgumentException("Core class not found!");
    }
    //todo мб вынести в общий сервис, в абстрактный метод
    private String clearAllFlag(String abstractClassName) {
        String stringAllMatcher = Optional
                .of(FOR_CLEAR_FLAG_ALL_PATTERN.matcher(abstractClassName))
                .filter(Matcher::matches)
                .map(matcher -> matcher.group("all"))
                .orElse("");

        return abstractClassName
                .replace(stringAllMatcher, "")
                .replace("ALL", "")
                .replace("или", ",");
    }
    //todo мб вынести в общий сервис, в абстрактный метод
    private Set<WeaponCore> getWeaponCoresByClass(String abstractTypes) {
        return switch (abstractTypes.toLowerCase(Locale.ROOT)) {
            case "арбалет" -> Set.of(LIGHTWEIGHT_CROSSBOW, HAND_CROSSBOW, HEAVY_CROSSBOW);
            case "меч" -> Set.of(GREATSWORD, LONG_SWORD, SHORT_SWORD);
            case "лук" -> Set.of(LONG_BOW, SHORT_BOW);
            case "одноручное рукопашное оружие" -> Set.of(
                    MAGIC_STAFF,
                    MACE,
                    CLUB,
                    DAGGER,
                    SPEAR,
                    LIGHTWEIGHT_HAMMER,
                    THROWING_SPEAR,
                    HAND_AXE,
                    SICKLE, DART, SLING,
                    COMBAT_PICKAXE,
                    COMBAT_HAMMER,
                    COMBAT_AXE,
                    GRATESPEAR,
                    LONG_SWORD,
                    WHIP,
                    SHORT_SWORD,
                    MORGENSTERN,
                    RAPIER,
                    SCIMITAR,
                    TRIDENT,
                    CHAIN);
            case "топор" -> Set.of(HAND_AXE, COMBAT_AXE);
            default -> defaultMatchingWithCoreName(abstractTypes);
        };
    }

    private Set<WeaponCore> defaultMatchingWithCoreName(String abstractTypes) {
        return Arrays
                .stream(WeaponCore.values())
                .filter(weaponCore -> weaponCore.getName().equalsIgnoreCase(abstractTypes))
                .collect(Collectors.toSet());
    }
    //todo изменить имя на hint
    private String extractWeaponAbstractClassName(String text) {
        Matcher weaponMatcher = WEAPON_MATCHER_PATTERN.matcher(text);

        StringJoiner joiner = new StringJoiner(" ");

        if (weaponMatcher.find()) {
            joiner
                    .add(weaponMatcher
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

        if (allMatcher.find()) {
            joiner.add("ALL");
        }

        return joiner.length() == 0 ? "ALL" : joiner.toString();
    }
}