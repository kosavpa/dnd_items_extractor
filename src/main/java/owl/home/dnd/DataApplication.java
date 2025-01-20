package owl.home.dnd;

import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import owl.home.dnd.entitys.Armor;
import owl.home.dnd.entitys.Scroll;
import owl.home.dnd.entitys.Weapon;
import owl.home.dnd.service.armor.ArmorService;
import owl.home.dnd.service.scroll.ScrollService;
import owl.home.dnd.service.weapon.WeaponService;

import java.util.Set;

@SpringBootApplication
public class DataApplication {

    @SneakyThrows
    public static void main(String[] args) {
//		SpringApplication.run(DataApplication.class, args);

        Set weapons = new ArmorService().extractEquipment();

        System.out.println(weapons.size());
    }









}