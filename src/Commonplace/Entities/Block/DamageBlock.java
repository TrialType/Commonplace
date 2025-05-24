package Commonplace.Entities.Block;

import arc.func.Func;
import arc.graphics.Color;
import arc.util.Time;
import mindustry.core.UI;
import mindustry.gen.Building;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.meta.BuildVisibility;

public class DamageBlock extends Block {
    public DamageBlock(String name) {
        super(name);

        size = 2;
        solid = true;
        update = true;
        breakable = true;
        destructible = true;

        requirements(Category.defense, BuildVisibility.sandboxOnly, ItemStack.empty);
    }

    public DamageBlock setSize(int size) {
        this.size = size;
        return this;
    }

    @Override
    public void setBars() {
        addBar("damage", damageBar());
    }

    public Func<Building, Bar> damageBar() {
        return b -> new Bar(() ->
                b instanceof DamageBuild d ? UI.formatAmount((long) d.lastDamage) : "0",
                () -> Color.white, () -> (b instanceof DamageBuild d ? Math.min(d.lastDamage, 1) : 0));
    }

    public class DamageBuild extends Building {
        public float damage = 0;
        public float lastDamage = 0;
        public float damageTimer = 0;

        @Override
        public void updateTile() {
            damageTimer += Time.delta;
            if (damageTimer >= 60) {
                damageTimer = 0;
                lastDamage = damage;
                damage = 0;
            }
        }

        @Override
        public void damage(float damage) {
            this.damage += damage;
        }
    }
}
