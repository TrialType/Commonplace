package Commonplace.Type.Elements;

import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.layout.Table;
import mindustry.core.UI;
import mindustry.type.ItemStack;
import mindustry.type.PayloadStack;
import mindustry.ui.ItemImage;
import mindustry.ui.Styles;

public class ItemImage2 extends ItemImage {
    public ItemImage2(TextureRegion region, int amount, int have) {
        super(region, amount);

        children.remove(children.size - 1);
        if (amount != 0) {
            add(new Table(t -> {
                t.left().bottom();
                t.add(have >= 1000 ? UI.formatAmount(have) : have + "/").color(have < amount ? Color.red : Color.white);
                t.add(amount >= 1000 ? UI.formatAmount(amount) : amount + "").style(Styles.outlineLabel);
                t.pack();
            }));
        }
    }

    public ItemImage2(ItemStack stack, int have) {
        this(stack.item.uiIcon, stack.amount, have);
    }

    public ItemImage2(PayloadStack stack, int have) {
        this(stack.item.uiIcon, stack.amount, have);
    }
}
