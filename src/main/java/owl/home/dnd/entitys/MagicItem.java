package owl.home.dnd.entitys;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity(name = "owl$MagicItem")
@Table(name = "MAGIC_ITEM")
public class MagicItem extends Equipment {
}