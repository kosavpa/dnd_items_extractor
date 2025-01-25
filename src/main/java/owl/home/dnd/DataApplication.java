package owl.home.dnd;

import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import owl.home.dnd.service.weapon.WeaponService;

import java.util.Set;

@SpringBootApplication
public class DataApplication {

    @SneakyThrows
    public static void main(String[] args) {
//		SpringApplication.run(DataApplication.class, args);

        Set weapons = new WeaponService().extractEquipment();

        System.out.println(weapons.size());
    }


}