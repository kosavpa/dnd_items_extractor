package owl.home.dnd.service.scroll;


import org.apache.logging.log4j.util.Strings;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import owl.home.dnd.entitys.Scroll;
import owl.home.dnd.util.common_items.CommonUtil;
import owl.home.dnd.util.exception.ExceptionUtils;
import owl.home.dnd.util.parse.JsoupUtil;

import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ScrollService {
    private static final Pattern ARMOR_IMG_PATTERN = Pattern.compile(".+item_type_scroll");
    //todo мб вынести в общий сервис
    public Set<Scroll> extractScroll() {
        Document magicItems = JsoupUtil.getDocFromHref("https://dnd.su/items/");

        return JsoupUtil
                .getElementsByClassFromDoc("col list-item__spell for_filter", magicItems)
                .stream()
                .filter(this::isScroll)
                .map(this::getScrolllement)
                .filter(Objects::nonNull)
                .map(this::mapToScroll)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
    //todo мб вынести в общий сервис
    private Element getScrolllement(Element element) {
        return ExceptionUtils.wrapAndGetResultOrNull(() -> JsoupUtil.getItemFromElementHref(element));
    }
    //todo мб вынести в общий сервис
    private boolean isScroll(Element itemWrapper) {
        return JsoupUtil
                .getElementsByTag(itemWrapper, "use")
                .stream()
                .map(element -> element.attr("xlink:href"))
                .filter(Strings::isNotBlank)
                .allMatch(attr -> ARMOR_IMG_PATTERN.matcher(attr).matches());

    }
    //todo мб вынести в общий сервис
    private Scroll mapToScroll(Element element) {
        return ExceptionUtils
                .wrapAndGetResultOrNull(() -> {
                    Scroll scroll = new Scroll();

                    scroll.setCurrency(CommonUtil.extractCurrency(element));
                    scroll.setName(CommonUtil.extractName(element));
                    scroll.setMinAmount(CommonUtil.extractMinAmount(element));
                    scroll.setMaxAmount(CommonUtil.extractMaxAmount(element));
                    scroll.setDescription(CommonUtil.extractDescription(element));
                    scroll.setRarity(CommonUtil.extractRarity(element));
                    scroll.setNeedPrepared(CommonUtil.extractPrepared(element));

                    if (scroll.isNeedPrepared()) {
                        scroll.setPreparedClass(CommonUtil.extractPreparedClass(element));
                        scroll.setPreparedRacy(CommonUtil.extractPreparedRacy(element));
                    }

                    return scroll;
                });
    }
}