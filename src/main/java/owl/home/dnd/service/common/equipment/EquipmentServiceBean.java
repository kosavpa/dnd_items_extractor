package owl.home.dnd.service.common.equipment;


import lombok.SneakyThrows;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import owl.home.dnd.constant.game_class.GameClass;
import owl.home.dnd.constant.race.Racy;
import owl.home.dnd.entitys.Equipment;
import owl.home.dnd.util.common_items.CommonUtil;
import owl.home.dnd.util.exception.ExceptionUtils;
import owl.home.dnd.util.parse.JsoupUtil;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static owl.home.dnd.util.common_items.CommonUtil.SIZE_TYPE_ALIGNMENT;


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

        return abstractEquipment;
    }

    @SneakyThrows
    private T getNewConcreteTypeInstance() {
        return getConcreteEquipmentClass().getConstructor().newInstance();
    }

    protected T buildConcreteEquipment(EquipmentHint<T> equipmentHint) {
        T abstractEquipment = equipmentHint.getEquipment();

        equipmentHint.fillDescription(JsoupUtil
                .getElementByClassFromElement(equipmentHint.getEquipmentElement(), SIZE_TYPE_ALIGNMENT));

        abstractEquipment.setNeedPrepared(equipmentHint.isHasPrepared());
        if (abstractEquipment.isNeedPrepared()) {
            setGameClasses(equipmentHint);

            setRacys(equipmentHint);
        }

        return abstractEquipment;
    }

    protected void setRacys(EquipmentHint<T> equipmentHint) {
        Set<Racy> racys;

        if (equipmentHint.getRacys().isEmpty()) {
            racys = getAllRacys();
        } else {
            racys = equipmentHint
                    .getRacys()
                    .stream()
                    .map(this::getCurrentRacy)
                    .collect(Collectors.toSet());
        }

        excludeRacy(racys, equipmentHint);

        equipmentHint.getEquipment().setPreparedRacy(racys);
    }

    private Racy getCurrentRacy(String racyStr) {
        return getAllRacys()
                .stream()
                .filter(racy -> racy.getName().toLowerCase().contains(racyStr))
                .findFirst()
                .orElseThrow();
    }

    protected Set<Racy> getAllRacys() {
        return Arrays.stream(Racy.values()).collect(Collectors.toSet());
    }

    protected void excludeRacy(Set<Racy> racys, EquipmentHint<T> hint) {
        if (hint.isHasExcludeGameClasses()) {
            Set<Racy> excluded = hint
                    .getExcludeRacys()
                    .stream()
                    .map(this::getCurrentRacy)
                    .collect(Collectors.toSet());

            racys.removeAll(excluded);
        }
    }

    protected void setGameClasses(EquipmentHint<T> equipmentHint) {
        Set<GameClass> gameClasses;

        if (equipmentHint.getGameClasses().isEmpty()) {
            gameClasses = getAllGameClass();
        } else {
            gameClasses = equipmentHint
                    .getGameClasses()
                    .stream()
                    .map(this::getCurrentGameClass)
                    .collect(Collectors.toSet());
        }

        excludeGameClass(gameClasses, equipmentHint);

        equipmentHint.getEquipment().setPreparedClass(gameClasses);
    }

    private GameClass getCurrentGameClass(String gameClassStr) {
        return getAllGameClass()
                .stream()
                .filter(gameClass -> gameClass.getName().toLowerCase().contains(gameClassStr))
                .findFirst()
                .orElseThrow();
    }

    protected Set<GameClass> getAllGameClass() {
        return Arrays.stream(GameClass.values()).collect(Collectors.toSet());
    }

    protected void excludeGameClass(Set<GameClass> gameClasses, EquipmentHint<T> hint) {
        if (hint.isHasExcludeGameClasses()) {
            Set<GameClass> excluded = hint
                    .getExcludeGameClasses()
                    .stream()
                    .map(this::getCurrentGameClass)
                    .collect(Collectors.toSet());

            gameClasses.removeAll(excluded);
        }
    }

    protected abstract Class<T> getConcreteEquipmentClass();

    protected abstract String getConcreteTypeSvgSuffix();
}