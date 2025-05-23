package Commonplace.Entities.Block;

import arc.Core;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Scaling;
import arc.util.Strings;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;
import mindustry.gen.Tex;
import mindustry.logic.LAccess;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

public class StackCrafter extends GenericCrafter {
    public Seq<ProductStack> switchStack = new Seq<>();
    public boolean outPutItem = true;

    public StackCrafter(String name) {
        super(name);

        configurable = true;
        logicConfigurable = true;
        hasLiquids = hasItems = hasPower = true;
    }

    public boolean configSenseable() {
        return true;
    }

    @Override
    public boolean outputsItems() {
        return outPutItem;
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(Stat.output, table -> {
            table.row();
            for (ProductStack ps : switchStack) {
                table.row();
                display(ps.itemsIn, ps.liquidsIn, ps.progress, table);
                table.label(() -> "--->");
                display(ps.itemsOut, ps.liquidsOut, ps.progress, table);
            }
        });
    }

    public static void display(ItemStack[] s, LiquidStack[] l, float progress, Table t) {
        String ps = " " + StatUnit.perSecond.localized();
        for (ItemStack i : s) {
            t.image(i.item.uiIcon).scaling(Scaling.fit);
            t.label(() -> Strings.fixed(60 * i.amount / progress, 2) + ps);
        }
        for (LiquidStack li : l) {
            t.image(li.liquid.uiIcon).scaling(Scaling.fit);
            t.label(() -> Strings.fixed(60 * li.amount / progress, 2) + ps);
        }
    }

    public static class ProductStack {
        public final ItemStack[] itemsIn;
        public final LiquidStack[] liquidsIn;
        public final ItemStack[] itemsOut;
        public final LiquidStack[] liquidsOut;
        public float progress;

        public ProductStack(ItemStack[] ini, LiquidStack[] inl, ItemStack[] oui, LiquidStack[] oul, float p) {
            this.itemsIn = ini;
            this.liquidsIn = inl;
            this.itemsOut = oui;
            this.liquidsOut = oul;
            this.progress = p;
        }

        public void addTo(Building build) {
            if (build.block.hasItems) {
                for (ItemStack it : itemsOut) {
                    Item item = it.item;
                    for (int i = 0; i < it.amount; i++) {
                        build.offload(item);
                    }
                }
            }
            if (build.block.hasLiquids) {
                for (LiquidStack ls : liquidsOut) {
                    build.liquids.add(ls.liquid, ls.amount);
                }
            }
        }

        public void removeFrom(Building build) {
            if (build.block.hasItems) {
                for (ItemStack it : itemsIn) {
                    build.items.remove(it.item, it.amount);
                }
            }
            if (build.block.hasLiquids) {
                for (LiquidStack ls : liquidsIn) {
                    build.liquids.remove(ls.liquid, ls.amount);
                }
            }
        }
    }

    public class StackCrafterBuild extends GenericCrafterBuild {
        public int chance = -1;

        @Override
        public void buildConfiguration(Table table) {
            super.buildConfiguration(table);
            Seq<Table> tables = new Seq<>();

            table.row();
            for (int i = 0; i < switchStack.size; i++) {
                ProductStack ps = switchStack.get(i);

                table.row();
                table.table(chance == i ? Tex.buttonDown : Tex.paneSolid, t -> {
                    tables.add(t);
                    StackCrafter.display(ps.itemsIn, ps.liquidsIn, ps.progress, t);
                    t.label(() -> "--->");
                    StackCrafter.display(ps.itemsOut, ps.liquidsOut, ps.progress, t);
                    t.clicked(() -> {
                        if (chance == switchStack.indexOf(ps)) {
                            t.setBackground(Tex.paneSolid);
                            chance = -1;
                        } else {
                            t.setBackground(Tex.buttonDown);
                            if (chance >= 0) {
                                tables.get(chance).setBackground(Tex.paneSolid);
                            }
                            chance = switchStack.indexOf(ps);
                        }
                    });
                }).grow().row();
            }
        }

        @Override
        public void updateTile() {
            if (chance < 0 || !shouldConsume() || !couldProduct()) {
                efficiency = 0;
            }

            if (efficiency > 0) {
                craftTime = switchStack.get(chance).progress;

                progress += getProgressIncrease(craftTime);
                warmup = Mathf.approachDelta(warmup, warmupTarget(), warmupSpeed);

                if (outputLiquids != null) {
                    float inc = getProgressIncrease(1f);
                    for (var output : outputLiquids) {
                        handleLiquid(this, output.liquid, Math.min(output.amount * inc, liquidCapacity - liquids.get(output.liquid)));
                    }
                }

                if (wasVisible && Mathf.chanceDelta(updateEffectChance)) {
                    updateEffect.at(x + Mathf.range(size * 4f), y + Mathf.range(size * 4));
                }
            } else {
                warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
            }

            totalProgress += warmup * Time.delta;

            if (progress >= 1f) {
                craft();
            }

            dumpOutputs();
        }

        public boolean couldProduct() {
            ItemStack[] iss = switchStack.get(chance).itemsOut;
            for (ItemStack is : iss) {
                if (is.amount * 10 <= this.items.get(is.item)) {
                    return false;
                }
            }
            LiquidStack[] lss = switchStack.get(chance).liquidsOut;
            for (LiquidStack ls : lss) {
                if (ls.amount * 10 <= this.liquids.get(ls.liquid)) {
                    return false;
                }
            }
            return true;
        }

        public void craft() {
            if (chance > -1) {
                consume();

                ProductStack ps = switchStack.get(chance);
                ps.removeFrom(this);
                ps.addTo(this);

                if (wasVisible) {
                    craftEffect.at(x, y);
                }
                progress %= 1f;
            }
        }

        public void dumpOutputs() {
            if (chance > -1 && timer(timerDump, dumpTime / timeScale)) {
                for (ItemStack output : switchStack.get(chance).itemsOut) {
                    for (int i = 0; i < timeScale; i++) {
                        dump(output.item);
                    }
                }
            }

            if (chance > -1) {
                LiquidStack[] out = switchStack.get(chance).liquidsOut;
                for (int i = 0; i < out.length; i++) {
                    int dir = liquidOutputDirections.length > i ? liquidOutputDirections[i] : -1;

                    dumpLiquid(out[i].liquid, 2f, dir);
                }
            }
        }

        @Override
        public boolean shouldConsume() {
            if (chance > -1) {
                ProductStack ps = switchStack.get(chance);
                for (ItemStack is : ps.itemsIn) {
                    if (items.get(is.item) < is.amount) {
                        return false;
                    }
                }

                for (LiquidStack ls : ps.liquidsIn) {
                    if (liquids.get(ls.liquid) < ls.amount) {
                        return false;
                    }
                }

                return true;
            }
            return false;
        }

        @Override
        public int getMaximumAccepted(Item item) {
            if (chance >= 0) {
                ItemStack[] iss = switchStack.get(chance).itemsIn;
                for (ItemStack is : iss) {
                    if (is.item == item) {
                        return is.amount * 10;
                    }
                }
            }
            return -1;
        }

        public boolean use(Liquid liquid) {
            if (chance > -1) {
                LiquidStack[] lss = switchStack.get(chance).liquidsIn;
                for (LiquidStack ls : lss) {
                    if (ls.liquid == liquid) {
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean acceptItem(Building source, Item item) {
            return this.items.get(item) < this.getMaximumAccepted(item);
        }

        public boolean acceptLiquid(Building source, Liquid liquid) {
            return this.block.hasLiquids && use(liquid);
        }

        @Override
        public Object config() {
            return (int) chance;
        }

        @Override
        public void control(LAccess type, double p1, double p2, double p3, double p4) {
            if (type == LAccess.enabled) {
                this.enabled = !Mathf.zero((float) p1);
            } else if (type == LAccess.config) {
                this.chance = Math.min(switchStack.size - 1, (int) p1);
            }
        }

        @Override
        public void remove() {
            super.remove();
            this.chance = -1;
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            chance = read.i();
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(chance);
        }
    }
}
