package owl.home.dnd.constant.properties.damage;


import owl.home.dnd.constant.equip.Dice;


public record Damage(Dice dice, int diceCount, DamageType damageType) {
}