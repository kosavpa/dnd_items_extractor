package owl.home.dnd.constant.properties.weapon;


import owl.home.dnd.constant.properties.PropertyHolder;


public record Accessibility() implements PropertyHolder {
    @Override
    public String getInfo() {
        return "Доступность";
    }
}
