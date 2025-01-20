package owl.home.dnd.service.common.equipment;


import lombok.SneakyThrows;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import owl.home.dnd.entitys.Equipment;
import owl.home.dnd.util.common_items.CommonUtil;
import owl.home.dnd.util.exception.ExceptionUtils;
import owl.home.dnd.util.parse.JsoupUtil;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public abstract class EquipmentServiceBean<T extends Equipment> implements EquipmentService<T> {
    private static final String DND_SOURCE_URI = "https://dnd.su/items/";

    private static final String EQUIPMENT_HTML_CLASS = "col list-item__spell for_filter";

    @Override
    public Set<T> extractEquipment() {
        Document magicItems = JsoupUtil.getDocFromHref(DND_SOURCE_URI);

        return JsoupUtil
                .getElementsByClassFromDoc(EQUIPMENT_HTML_CLASS, magicItems)
                .stream()
                .filter(this::isNeededEquipment)
                .map(this::getEquipmentElement)
                .filter(Objects::nonNull)
                .map(this::mapToConcreteEquipment)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    protected boolean isNeededEquipment(Element itemWrapper) {
        return JsoupUtil
                .getElementsByTag(itemWrapper, "use")
                .stream()
                .map(element -> element.attr("xlink:href"))
                .filter(Strings::isNotBlank)
                .allMatch(attr -> getConcretTypePattern().matcher(attr).matches());
    }

    protected Element getEquipmentElement(Element element) {
        return ExceptionUtils.wrapAndGetResultOrNull(() -> JsoupUtil.getItemFromElementHref(element));
    }

    protected Pattern getConcretTypePattern() {
        String concreteTypeSvg = ".+item_type_%s".formatted(getConcreteTypeSvgSuffix());

        return Pattern.compile(concreteTypeSvg);
    }

    private T mapToConcreteEquipment(Element element) {
        return ExceptionUtils.wrapAndGetResultOrNull(() -> buildConcreteEquipment(getEquipmentHint(element)));
    }

    @SuppressWarnings("unchecked")
    protected EquipmentHint<T> getEquipmentHint(Element element) {
        return (EquipmentHint<T>) EquipmentHint
                .builder()
                .equipmentElement(element)
                .equipment(getAbstractEquipment(element))
                .build();
    }

    private T getAbstractEquipment(Element element) {
        T abstractEquipment = getNewConcreteTypeInstance();

        abstractEquipment.setCurrency(CommonUtil.extractCurrency(element));
        abstractEquipment.setName(CommonUtil.extractName(element));
        abstractEquipment.setMinAmount(CommonUtil.extractMinAmount(element));
        abstractEquipment.setMaxAmount(CommonUtil.extractMaxAmount(element));
        abstractEquipment.setDescription(CommonUtil.extractDescription(element));
        abstractEquipment.setRarity(CommonUtil.extractRarity(element));
        abstractEquipment.setNeedPrepared(CommonUtil.extractPrepared(element));

        if (abstractEquipment.isNeedPrepared()) {
            abstractEquipment.setPreparedClass(CommonUtil.extractPreparedClass(element));
            abstractEquipment.setPreparedRacy(CommonUtil.extractPreparedRacy(element));
        }

        return abstractEquipment;
    }

    @SneakyThrows
    private T getNewConcreteTypeInstance() {
        return getConcreteEquipmentClass().getConstructor().newInstance();
    }

    protected abstract T buildConcreteEquipment(EquipmentHint<T> equipmentHint);

    protected abstract Class<T> getConcreteEquipmentClass();

    protected abstract String getConcreteTypeSvgSuffix();
}