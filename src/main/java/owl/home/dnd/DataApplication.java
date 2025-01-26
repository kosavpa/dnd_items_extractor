package owl.home.dnd;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import owl.home.dnd.service.armor.ArmorService;
import owl.home.dnd.service.magic_item.MagicItemService;
import owl.home.dnd.service.magic_stick.MagicStickService;
import owl.home.dnd.service.potion.PotionService;
import owl.home.dnd.service.ring.RingService;
import owl.home.dnd.service.scroll.ScrollService;
import owl.home.dnd.service.wand.WandService;
import owl.home.dnd.service.weapon.WeaponService;


@SpringBootApplication
@Slf4j
public class DataApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataApplication.class, args);
    }

    @EventListener(ApplicationStartedEvent.class)
    public void init(ApplicationStartedEvent event) {
        try {
            ConfigurableApplicationContext applicationContext = event.getApplicationContext();

            if (isNotInitialized(applicationContext.getBean(SessionFactory.class))) {
                applicationContext.getBean(ArmorService.class).extractEquipment();
                applicationContext.getBean(WeaponService.class).extractEquipment();
                applicationContext.getBean(MagicItemService.class).extractEquipment();
                applicationContext.getBean(MagicStickService.class).extractEquipment();
                applicationContext.getBean(PotionService.class).extractEquipment();
                applicationContext.getBean(RingService.class).extractEquipment();
                applicationContext.getBean(ScrollService.class).extractEquipment();
                applicationContext.getBean(WandService.class).extractEquipment();

                markIsInitialized(applicationContext.getBean(SessionFactory.class));
            }
        } catch (Exception exception) {
            log.error("Happens exception while initialized:", exception);

            throw exception;
        }
    }

    private void markIsInitialized(SessionFactory factory) {
        try (Session currentSession = factory.openSession()) {
            currentSession.beginTransaction();

            currentSession
                    .createNativeQuery("INSERT INTO SYS_INFO VALUES(TRUE)", Boolean.class)
                    .executeUpdate();
        } catch (Exception exception) {
            log.error("While get init info happens exception:", exception);

            throw exception;
        }
    }

    private boolean isNotInitialized(SessionFactory factory) {
        try (Session currentSession = factory.openSession()) {
            currentSession.beginTransaction();

            return BooleanUtils.isNotTrue(currentSession
                    .createNativeQuery("SELECT INITIALIZED FROM SYS_INFO", Boolean.class)
                    .getSingleResultOrNull());
        } catch (Exception exception) {
            log.error("While get init info happens exception:", exception);

            throw exception;
        }
    }
}