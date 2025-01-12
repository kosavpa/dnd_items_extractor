package owl.home.dnd.constant.properties.weapon;


import owl.home.dnd.constant.properties.PropertyHolder;


public record Fencing() implements PropertyHolder {
    @Override
    public String getInfo() {
        return "Фехтовальное";
    }
}