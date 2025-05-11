package Commonplace.Entities.Block;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.Element;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Stack;
import arc.scene.ui.layout.Table;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.gen.Tex;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;

import java.lang.reflect.Field;

import static mindustry.Vars.*;
import static mindustry.Vars.tilesize;

public class AllocationBlock extends Block {
    public float num = 3.8f;
    public float radius = 80;
    public float reload = 30;

    public AllocationBlock(String name) {
        super(name);

        solid = true;
        update = true;
        hasPower = true;
        configurable = true;
        destructible = true;

        config(Integer.class, (b, p) -> {
            if (b instanceof AllocationBuild a) {
                if (a.input.contains(p)) {
                    a.input.removeValue(p);
                } else if (a.output.contains(p)) {
                    a.output.removeValue(p);
                } else if (a.in) {
                    a.input.add(p);
                } else {
                    a.output.add(p);
                }
            }
        });
    }

    @Override
    public void setBars() {
        super.setBars();

        addBar("reload", (AllocationBuild a) -> new Bar(() ->
                Core.bundle.format("@reload"), () -> Pal.items, () -> a.timer / reload));
    }

    public class AllocationBuild extends Building {
        public IntSeq input = new IntSeq();
        public IntSeq output = new IntSeq();

        protected int indexO = 0;
        protected float value = 0;
        protected float timer = 0;
        protected boolean in = true;

        @Override
        public void updateTile() {
            IntSet remove = new IntSet();
            input.each(p -> invalid(p, remove));
            output.each(p -> invalid(p, remove));
            remove.each(p -> {
                input.removeValue(p);
                output.removeValue(p);
            });

            timer += delta() * efficiency;
            if (timer >= reload && !input.isEmpty() && !output.isEmpty()) {
                value += num;
                int n = (int) value;
                value -= n;
                if (n > 0) {
                    int use = 0;
                    int i = 1;
                    IntSeq[] sets = initInput();
                    outer:
                    for (; i <= output.size; i++) {
                        Building b = world.build(output.get((indexO + i) % output.size));
                        Seq<Item> items = new Seq<>(content.items());
                        items.sort(item -> b.items.get(item));
                        for (Item item : items) {
                            IntSeq seq = sets[item.id];
                            if (seq != null) {
                                seq.shuffle();
                                int idx = 0, total = 1;
                                int[] inputs = seq.toArray();
                                while (b.acceptItem(b, item) && total > 0) {
                                    total = 0;
                                    int ip = inputs[idx++];
                                    Building ib = world.build(ip);
                                    if (ib.items.has(item)) {
                                        use++;
                                        total += ib.items.get(item);
                                        ib.items.remove(item, 1);
                                        b.handleItem(b, item);
                                    }
                                    if (use == n) {
                                        break outer;
                                    }
                                    idx %= inputs.length;
                                }
                            }
                        }
                    }

                    indexO += i;
                    indexO %= output.size;
                    if (use == 0) {
                        value += n;
                        value -= num;
                        timer = reload;
                    } else {
                        timer %= reload;
                    }
                } else {
                    timer %= reload;
                }
            }
        }

        @Override
        public boolean onConfigureBuildTapped(Building other) {
            if (other == this) {
                input.clear();
                output.clear();
                return false;
            } else if (other.within(this, radius + other.block.size * tilesize / 2f + other.block.sizeOffset) && other.isValid() && other.team == team && other.block.hasItems) {
                configure(other.pos());
                return false;
            }
            return true;
        }

        @Override
        public void buildConfiguration(Table table) {
            table.row();
            table.table(t -> {
                Element e = new Element();
                e.setFillParent(true);

                Stack s = new Stack();
                s.setFillParent(true);
                s.addChild(e);
                s.addChild(new Label(() -> in ? "input" : "output"));

                t.add(s);
                t.clicked(() -> in = !in);
                t.setBackground(Tex.windowEmpty);
            }).grow();
        }

        @Override
        public void drawConfigure() {
            float f = (Time.time / 2.5f) % 20;
            Vec2 v = new Vec2();
            Lines.stroke(3);
            Drawf.circles(x, y, tile.block().size * tilesize / 2f + 1f + Mathf.absin(Time.time, 4f, 1f));
            Drawf.circles(x, y, radius);
            IntSet remove = new IntSet();
            input.each(p -> {
                if (!invalid(p, remove)) {
                    Building b = world.build(p);
                    float bx = b.getX(), by = b.getY(), tx = getX(), ty = getY();
                    v.trns(Angles.angle(tx - bx, ty - by), f);

                    Drawf.square(bx, by, b.block.size * tilesize / 2f + 1f, Pal.techBlue);
                    Draw.color(Pal.techBlue);
                    Lines.line(bx, by, tx, ty);
                    float len = Mathf.dst(bx, by, tx, ty);
                    for (float i = f; i < len; i += 20) {
                        v.setLength(i);
                        Drawf.arrow(bx, by, bx + v.x, by + v.y, i, 2, Pal.techBlue);
                    }
                }
            });
            output.each(p -> {
                if (!invalid(p, remove)) {
                    Building b = world.build(p);
                    float bx = b.getX(), by = b.getY(), tx = getX(), ty = getY();
                    v.trns(Angles.angle(bx - tx, by - ty), f);

                    Drawf.square(bx, by, b.block.size * tilesize / 2f + 1f, Pal.redSpark);
                    Draw.color(Pal.redSpark);
                    Lines.line(bx, by, tx, ty);
                    float len = Mathf.dst(bx, by, tx, ty);
                    for (float i = f; i < len; i += 20) {
                        v.setLength(i);
                        Drawf.arrow(tx, ty, tx + v.x, ty + v.y, i, 2, Pal.redSpark);
                    }
                }
            });
            remove.each(p -> {
                input.removeValue(p);
                output.removeValue(p);
            });
        }

        public IntSeq[] initInput() {
            IntSeq[] items = new IntSeq[content.items().size];
            input.each(p -> {
                Building b = Vars.world.build(p);
                if (b.block instanceof GenericCrafter g) {
                    if (g.outputItems != null) {
                        for (ItemStack stack : g.outputItems) {
                            IntSeq set = items[stack.item.id];
                            if (set == null) {
                                items[stack.item.id] = set = new IntSeq();
                            }
                            set.add(p);
                        }
                    }
                } else {
                    try {
                        Field f = b.block.getClass().getField("outputItems");
                        f.setAccessible(true);
                        ItemStack[] stacks = (ItemStack[]) f.get(b.block);
                        for (ItemStack stack : stacks) {
                            IntSeq set = items[stack.item.id];
                            if (set == null) {
                                items[stack.item.id] = set = new IntSeq();
                            }
                            set.add(p);
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        b.items.each((i, a) -> {
                            if (a > 0) {
                                IntSeq set = items[i.id];
                                if (set == null) {
                                    items[i.id] = set = new IntSeq();
                                }
                                set.add(p);
                            }
                        });
                    }
                }
            });
            return items;
        }

        public boolean invalid(int p, IntSet set) {
            boolean invalid = invalid(p);
            if (invalid) {
                set.add(p);
            }
            return invalid;
        }

        public boolean invalid(int p) {
            Building b = world.build(p);
            return b == null || !b.isValid() || b.team != team || !b.block.hasItems;
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(indexO);
            write.f(value);
            write.f(timer);
            write.bool(in);

            write.i(input.size);
            input.each(p -> {
                if (invalid(p)) {
                    write.i(-1);
                } else {
                    write.i(p);
                }
            });
            write.i(output.size);
            output.each(p -> {
                if (invalid(p)) {
                    write.i(-1);
                } else {
                    write.i(p);
                }
            });
        }

        @Override
        public void read(Reads read, byte version) {
            super.read(read, version);
            indexO = read.i();
            value = read.f();
            timer = read.f();
            in = read.bool();
            for (int num = read.i(); num > 0; num--) {
                int p = read.i();
                if (p >= 0) {
                    input.add(p);
                }
            }
            for (int num = read.i(); num > 0; num--) {
                int p = read.i();
                if (p >= 0) {
                    output.add(p);
                }
            }
        }
    }
}
