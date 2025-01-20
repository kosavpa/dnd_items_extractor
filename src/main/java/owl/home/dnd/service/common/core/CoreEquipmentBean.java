package owl.home.dnd.service.common.core;


import org.jsoup.nodes.Element;
import owl.home.dnd.constant.item_type.ItemType;
import owl.home.dnd.entitys.Equipment;
import owl.home.dnd.service.common.equipment.EquipmentHint;
import owl.home.dnd.service.common.equipment.EquipmentServiceBean;
import owl.home.dnd.util.parse.JsoupUtil;

import java.util.Set;
import java.util.stream.Collectors;


public abstract class CoreEquipmentBean<T extends Equipment, R extends ItemType> extends EquipmentServiceBean<T> {
    protected Set<R> extractCoresByHint(EquipmentHint<T> hint) {
        Element coreDescriptionElement = JsoupUtil.getElementByClassFromElement(
                hint.getEquipmentElement(),
                "size-type-alignment");

        if (coreDescriptionElement == null) {
            return Set.of();
        }

        hint.fillDescription(coreDescriptionElement);

        if (hint.isHasAll()) {
            if (hint.isWithTips()) {
                Set<R> cores = hint.getTipsSet()
                        .stream()
                        .filter(tip -> !tip.isBlank())
                        .flatMap(tip -> getCoresByTip(tip).stream())
                        .collect(Collectors.toSet());

                if (hint.isHasExclude()) {
                    excludeCores(cores, hint);
                }

                return cores;
            } else {
                return getCoreAllCores();
            }
        } else {
            return hint.getNamesSet()
                    .stream()
                    .flatMap(name -> getCoreByName(name).stream())
                    .collect(Collectors.toSet());
        }
    }

    protected abstract void excludeCores(Set<R> cores, EquipmentHint<T> exclude);

    protected abstract Set<R> getCoreByName(String name);

    protected abstract Set<R> getCoresByTip(String tip);

    protected abstract Set<R> getCoreAllCores();
}