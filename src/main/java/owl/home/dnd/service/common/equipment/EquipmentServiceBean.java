package owl.home.dnd.service.common.equipment;


import jakarta.persistence.EntityManager;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.transaction.annotation.Transactional;
import owl.home.dnd.entitys.Equipment;
import owl.home.dnd.util.exception.ExceptionUtils;
import owl.home.dnd.util.parse.JsoupUtil;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static owl.home.dnd.util.constant.Constants.DND_SOURCE_URI;
import static owl.home.dnd.util.constant.Constants.EQUIPMENT_HTML_CLASS;


public abstract class EquipmentServiceBean<T extends Equipment> implements EquipmentService<T> {
    protected EntityManager entityManager;

    protected EquipmentServiceBean(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Set<T> extractEquipment() {
        Document magicItems = JsoupUtil.getDocFromHref(DND_SOURCE_URI);

        Set<T> result = new HashSet<>();

        int round = 4;

        while (round != 0) {
            round--;

            Set<T> equipments = JsoupUtil
                    .getElementsByClassFromDoc(EQUIPMENT_HTML_CLASS, magicItems)
                    .stream()
                    .filter(this::isNeededEquipment)
                    .map(this::getEquipmentElement)
                    .filter(Objects::nonNull)
                    .map(this::mapToConcreteEquipment)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            if (equipments.size() > result.size()) {
                result.addAll(equipments);
            }
        }

        result.forEach(entityManager::persist);

        return result;
    }

    protected boolean isNeededEquipment(Element itemWrapper) {
        return JsoupUtil
                .getElementsByTag(itemWrapper, "use")
                .stream()
                .map(element -> element.attr("xlink:href"))
                .filter(Strings::isNotBlank)
                .allMatch(attr -> getConcretTypePattern().matcher(attr).matches());
    }

    protected Pair<String, Element> getEquipmentElement(Element element) {
        return ExceptionUtils.wrapAndGetResultOrNull(() -> JsoupUtil.getItemFromElementHref(element));
    }

    protected Pattern getConcretTypePattern() {
        String concreteTypeSvg = ".+item_type_%s".formatted(getConcreteTypeSvgSuffix());

        return Pattern.compile(concreteTypeSvg);
    }

    private T mapToConcreteEquipment(Pair<String, Element> stringElementPair) {
        return ExceptionUtils.wrapAndGetResultOrNull(() -> buildConcreteEquipment(getEquipmentHint(stringElementPair)));
    }

    @SuppressWarnings("unchecked")
    protected EquipmentHint<T> getEquipmentHint(Pair<String, Element> stringElementPair) {
        return (EquipmentHint<T>) EquipmentHint
                .builder()
                .equipmentElement(stringElementPair.getValue())
                .equipmentLink(stringElementPair.getKey())
                .equipment(getNewConcreteTypeInstance())
                .build();
    }

    @SneakyThrows
    private T getNewConcreteTypeInstance() {
        return getConcreteEquipmentClass().getConstructor().newInstance();
    }

    protected T buildConcreteEquipment(EquipmentHint<T> equipmentHint) {
        equipmentHint.fillCommonDescription();

        T equipment = equipmentHint.getEquipment();

        equipment.setNeedPrepared(equipmentHint.isHasNeedPrepared());
        equipment.setCurrency(equipmentHint.getCurrency());
        equipment.setName(equipmentHint.getEquipmentName());
        equipment.setDescription(equipmentHint.getEquipmentDescription());
        equipment.setRarity(equipmentHint.getRarity());
        equipment.setMaxAmount(equipmentHint.getMaxAmount());
        equipment.setMinAmount(equipmentHint.getMinAmount());
        equipment.setLink(equipmentHint.getEquipmentLink());

        return equipment;
    }

    protected abstract Class<T> getConcreteEquipmentClass();

    protected abstract String getConcreteTypeSvgSuffix();
}