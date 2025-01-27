package owl.home.dnd.util.parse;


import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class JsoupUtil {
    private JsoupUtil() {
    }

    @SneakyThrows
    public static Document getDocFromHref(String href) {
        return Jsoup.connect(href).get();
    }

    public static Elements getElementsByTag(Element wrapper, String tag) {
        return wrapper.getElementsByTag(tag);
    }

    public static Elements getElementsByClassFromDoc(String className, Document document) {
        return document.getElementsByClass(className);
    }

    public static Element getElementByClassFromDoc(String className, Document document) {
        return document.getElementsByClass(className).stream().findFirst().orElseThrow();
    }

    public static Pair<String, Element> getItemFromElementHref(Element wrapper) {
        return wrapper
                .stream()
                .map(element -> getElementByClassFromElement(element, "list-item-wrapper"))
                .map(element -> Pair.of(
                        element.attr("abs:href"),
                        getDocFromHref(element.attr("abs:href"))))
                .map(pair -> Pair.of(pair.getKey(), getElementByClassFromDoc("cards-wrapper", pair.getValue())))
                .findFirst()
                .orElseThrow();
    }

    public static Element getElementByClassFromElement(Element element, String elementClass) {
        return element.getElementsByClass(elementClass).stream().findFirst().orElse(null);
    }

    public static String prepareHtmlTextFromElement(Element element) {
        return element.text().replace((char) 8201, (char) 32);
    }
}