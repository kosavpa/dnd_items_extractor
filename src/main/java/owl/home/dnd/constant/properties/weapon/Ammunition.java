package owl.home.dnd.constant.properties.weapon;


import owl.home.dnd.constant.properties.PropertyHolder;


public record Ammunition(int effectiveDistance, int nonEffectiveDistance) implements PropertyHolder {
    @Override
    public String getInfo() {
        return "%d/%d".formatted(effectiveDistance, nonEffectiveDistance);
    }
}
