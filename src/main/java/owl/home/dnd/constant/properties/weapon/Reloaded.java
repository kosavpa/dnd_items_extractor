package owl.home.dnd.constant.properties.weapon;


import owl.home.dnd.constant.properties.PropertyHolder;


public record Reloaded() implements PropertyHolder {
    @Override
    public String getInfo() {
        return "Перезарядка";
    }
}