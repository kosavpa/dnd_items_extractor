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
                        .compile(
                                "(?<rarity>" +
                                        "(обычн|необычн|редк|очень\\sредк|легендарн|артефакт|редкость\\sварьируется)" +
                                        "(ый|ий|ое)?)")
                        .matcher(element.text()))
                .filter(Matcher::find)
                .map(matcher -> matcher.group("rarity"))
                .map(CommonUtil::extractRarityFromStr)
                .orElse(null);
    }

    private static Rarity extractRarityFromStr(String strWithRarity) {
        if ("редкость варьируется".equalsIgnoreCase(strWithRarity)) {
            return Rarity.VOLATILE;
        } else {
            String preparedRarityStr = strWithRarity.substring(0, strWithRarity.length() - 2).strip();

            return Arrays
                    .stream(Rarity.values())
                    .filter(rarity -> rarity
                            .getName()
                            .toLowerCase()
                            .contains(preparedRarityStr))
                    .findFirst()
                    .orElseThrow();
        }
    }

    public static Integer extractMaxAmount(Element wrapper) {
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

    public static Integer extractMinAmount(Element wrapper) {
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
                        .matcher(JsoupUtil.prepareHtmlTextFromElement(element)))
                .filter(Matcher::find)
                .map(matcher -> matcher.group("currency"))
                .map(stringCurrency -> Arrays.stream(Currency.values())
                        .filter(currency -> currency.getName().equals(stringCurrency))
                        .findFirst()
                        .orElseThrow())
                .orElse(null);
    }
}