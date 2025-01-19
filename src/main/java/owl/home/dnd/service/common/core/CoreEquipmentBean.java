package owl.home.dnd.service.common.core;


import org.jsoup.nodes.Element;
import owl.home.dnd.constant.item_type.ItemType;
import owl.home.dnd.entitys.Equipment;
import owl.home.dnd.service.common.equipment.EquipmentHint;
import owl.home.dnd.service.common.equipment.EquipmentServiceBean;
import owl.home.dnd.util.parse.JsoupUtil;

import java.util.Arrays;
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

        if (hint.isAll()) {
            if (hint.isWithTips()) {
                Set<R> cores = Arrays
                        .stream(hint.getTipsArray())
                        .filter(tip -> !tip.isBlank())
                        .flatMap(tip -> getCoresByTip(tip).stream())
                        .collect(Collectors.toSet());

                if (hint.isExclude()) {
                    excludeCores(cores, hint);
                }

                return cores;
            } else {
                return getCoreAllCores();
            }
        } else {
            return Arrays
                    .stream(hint.getNamesArray())
                    .flatMap(name -> getCoreByName(name).stream())
                    .collect(Collectors.toSet());
        }
    }

    protected abstract void excludeCores(Set<R> cores, EquipmentHint<T> exclude);

    protected abstract Set<R> getCoreByName(String name);

    protected abstract Set<R> getCoresByTip(String tip);

    protected abstract Set<R> getCoreAllCores();
}