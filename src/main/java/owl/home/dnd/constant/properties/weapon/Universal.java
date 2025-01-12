package owl.home.dnd.constant.properties.weapon;


import owl.home.dnd.constant.properties.damage.Damage;
import owl.home.dnd.constant.properties.PropertyHolder;


public record Universal(Damage twoHandDamage) implements PropertyHolder {

    @Override
    public String getInfo() {
        return "(%s)".formatted(twoHandDamage);
    }
}