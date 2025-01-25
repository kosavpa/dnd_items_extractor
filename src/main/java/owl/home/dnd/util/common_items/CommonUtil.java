package owl.home.dnd.util.common_items;


import org.jsoup.nodes.Element;
import owl.home.dnd.constant.equip.Currency;
import owl.home.dnd.constant.equip.Rarity;
import owl.home.dnd.util.parse.JsoupUtil;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CommonUtil {
    private CommonUtil() {
    }

    public static final String SIZE_TYPE_ALIGNMENT = "size-type-alignment";

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

    public static Rarity extractRarity(Element wrapper) {
        return Optional
                .ofNullable(JsoupUtil.getElementByClassFromElement(wrapper, SIZE_TYPE_ALIGNMENT))
                .map(element -> Pattern
                        .compile("Оружие\\s(\\()?[а-я\\sё,]+(\\))?(?<rarity>,.*)")
                        .matcher(element.text()))
                .filter(Matcher::find)
                .map(matcher -> matcher.group("rarity"))
                .map(CommonUtil::extractRarityFromStr)
                .orElse(null);
    }

    private static Rarity extractRarityFromStr(String strWithRarity) {
        if (strWithRarity.contains("(")) {
            strWithRarity = strWithRarity.substring(0, strWithRarity.indexOf("("));
        }

        String splitRarityString = strWithRarity.replace(",", "");

        if (!splitRarityString.toLowerCase().contains(Rarity.VOLATILE.getName().toLowerCase())) {
            return Arrays
                    .stream(Rarity.values())
                    .filter(rarity -> rarity
                            .getName()
                            .toLowerCase()
                            .contains(splitRarityString
                                    .substring(0, splitRarityString.length() - 3)
                                    .strip()))
                    .findFirst()
                    .orElseThrow();
        } else {
            return Rarity.VOLATILE;
        }
    }

    public static Integer extractMaxAmount(Element wrapper) {
        return Optional
                .ofNullable(JsoupUtil.getElementByClassFromElement(wrapper, "price"))
                .map(element -> Pattern
                        .compile("[\\sа-я]:[\\sа-я]*(?<sum>[\\d-\\s]+)[\\sсмэзп]+")
                        .matcher(prepareHtmlText(element.text())))
                .filter(Matcher::find)
                .map(matcher -> matcher.group("sum"))
                .filter(strSum -> strSum.split("-").length == 2)
                .map(strSum -> strSum.split("-")[1].replace(" ", ""))
                .map(Integer::valueOf)
                .orElse(null);
    }

    public static Integer extractMinAmount(Element wrapper) {
        return Optional
                .ofNullable(JsoupUtil.getElementByClassFromElement(wrapper, "price"))
                .map(element -> Pattern
                        .compile("[\\sа-я]:[\\sа-я]*(?<sum>[\\d-\\s]+)[\\sсмэзп]+")
                        .matcher(prepareHtmlText(element.text())))
                .filter(Matcher::find)
                .map(matcher -> matcher.group("sum"))
                .map(strSum -> strSum.split("-")[0].replace(" ", ""))
                .map(Integer::valueOf)
                .orElse(null);
    }

    private static CharSequence prepareHtmlText(String text) {
        return text.replace((char) 8201, (char) 32);
    }

    public static String extractDescription(Element wrapper) {
        return Optional
                .ofNullable(JsoupUtil.getElementByClassFromElement(wrapper, "subsection desc"))
                .map(Element::text)
                .orElse(null);
    }

    public static String extractName(Element wrapper) {
        return Optional
                .ofNullable(JsoupUtil.getElementByClassFromElement(wrapper, "card-title"))
                .map(Element::text)
                .orElseThrow();
    }

    public static Currency extractCurrency(Element wrapper) {
        return Optional
                .ofNullable(JsoupUtil.getElementByClassFromElement(wrapper, "price"))
                .map(element -> Pattern
                        .compile("[\\sа-я]:[\\sа-я]*[\\d-\\s]+(?<currency>[\\sсмэзп]+)?")
                        .matcher(prepareHtmlText(element.text())))
                .filter(Matcher::find)
                .map(matcher -> matcher.group("currency"))
                .map(stringCurrency -> Arrays.stream(Currency.values())
                        .filter(currency -> currency.getName().equals(stringCurrency))
                        .findFirst()
                        .orElseThrow())
                .orElse(null);
    }
}