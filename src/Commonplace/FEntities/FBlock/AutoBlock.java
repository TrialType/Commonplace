package Commonplace.FEntities.FBlock;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Point2;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Table;
import arc.struct.IntSet;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.entities.Effect;
import mindustry.entities.units.BuildPlan;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.logic.LAccess;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.ui.Bar;
import mindustry.ui.ItemDisplay;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.meta.Env;

import java.util.Comparator;

public class AutoBlock extends Block {
    private int index = 0;
    private float timer = 0;

    public ObjectMap<ItemStack[], Block> creates = new ObjectMap<>();
    public int behind = 7;
    public int maxLength = 30;
    public float maxRange = 350;
    public float createReload = 5;
    public float createDelay = 120;

    public Effect startEffect = new Effect(75, e -> {
        Draw.color(Pal.lighterOrange);
        Lines.stroke(8 * (1 - e.fin()));
        Lines.poly(e.x, e.y, 36, maxRange / 2 * e.fin(), 360);
    });
    public Effect createEffect = new Effect(90, e -> {
        if (e.data instanceof Object[] o) {
            Block b = (Block) o[1];
            Draw.color(Pal.bulletYellow);
            Fill.square(e.x, e.y, b.size * 5 * (1 - e.fin()), e.rotation);
        }
    });
    public Effect beginEffect = new Effect(createDelay, e -> {
        Rand rand = new Rand(e.id);
        Draw.color(Color.valueOf("AAFFAAFF"));
        float fin = e.fin() % 0.25f;
        Lines.stroke(3 * (0.25f - fin));
        Lines.poly(e.x, e.y, 36, maxRange * 2 * (0.25f - fin));
        for (int i = 0; i < 54; i++) {
            float angle = rand.range(360);
            float len = rand.range(maxRange / 2);
            Fill.circle(e.x + Angles.trnsx(angle, len * (1 - e.fin())),
                    e.y + Angles.trnsy(angle, len * (1 - e.fin())), 6 * (1 - e.fin()));
        }
    });

    public AutoBlock(String name) {
        super(name);

        update = true;
        destructible = true;
        solid = true;
        rotate = true;
        hasItems = true;
        hasPower = true;
        configurable = true;
        breakable = true;
        canOverdrive = false;
        logicConfigurable = true;
        envEnabled = Env.any;
    }

    public boolean configSenseable() {
        return true;
    }

    @Override
    public void drawPlanConfig(BuildPlan plan, Eachable<BuildPlan> list) {
        Block drawer = Blocks.copperWall;
        int rotation = plan.rotation * 90;
        int lon = 1;
        if (!creates.isEmpty()) {
            if (!Vars.state.isPaused()) {
                timer += Time.delta;
            }
            if (index == creates.size) {
                index = 0;
            }
            drawer = creates.values().toSeq().get(index);
            lon = drawer.size;
            if (timer > 60) {
                index++;
                timer = 0;
            }
        }
        int x = plan.x + (lon + behind) * (int) Mathf.cosDeg(rotation),
                y = plan.y + (lon + behind) * (int) Mathf.sinDeg(rotation);
        IntSet set = buildResult(x, y, plan.rotation, lon, drawer);

        Block finalDrawer = drawer;
        if (plan.block.size % 2 == 0) {
            set.each(p -> Draw.rect(finalDrawer.region, Point2.x(p) * 8, Point2.y(p) * 8));
        } else {
            set.each(p -> Draw.rect(finalDrawer.region, Point2.x(p) * 8 + 4, Point2.y(p) * 8 + 4));
        }

        Draw.color(Pal.range);
        Lines.stroke(3);
        Lines.poly(plan.x * 8, plan.y * 8, 36, (float) Math.sqrt(maxRange * maxRange / 4 + behind * behind));
    }

    public IntSet buildResult(int x, int y, int rotation, int lon, Block type) {
        IntSet set = new IntSet();
        Tile t;
        float max = maxRange / 16;
        if (rotation % 2 == 0) {
            for (int step1 = 0, step2 = 0; set.size < maxLength && (step1 <= max || step2 <= max); ) {
                if (step1 <= max && (t = Vars.world.tileWorld(x * 8, (y + step1) * 8)) != null &&
                        Build.validPlace(type, Vars.player == null ? Team.derelict : Vars.player.team(),
                                x, y + step1, rotation, false)) {
                    set.add(t.pos());
                    step1 += lon;
                } else if (step1 <= max) {
                    step1 += 1;
                }
                if (step2 <= max && (t = Vars.world.tileWorld(x * 8, (y - step2) * 8)) != null &&
                        Build.validPlace(type, Vars.player == null ? Team.derelict : Vars.player.team(),
                                x, y - step2, rotation, false)) {
                    set.add(t.pos());
                    step2 += lon;
                } else if (step2 <= max) {
                    step2 += 1;
                }
            }
        } else {
            for (int step1 = 0, step2 = 0; set.size < maxLength && (step1 <= max || step2 <= max); ) {
                if (step1 <= max && (t = Vars.world.tileWorld((x + step1) * 8, y * 8)) != null &&
                        Build.validPlace(type, Vars.player == null ? Team.derelict : Vars.player.team(),
                                x + step1, y, rotation, false)) {
                    set.add(t.pos());
                    step1 += lon;
                } else if (step1 <= max) {
                    step1 += 1;
                }
                if (step2 <= max && (t = Vars.world.tileWorld((x - step2) * 8, y * 8)) != null &&
                        Build.validPlace(type, Vars.player == null ? Team.derelict : Vars.player.team(),
                                x - step2, y, rotation, false)) {
                    set.add(t.pos());
                    step2 += lon;
                } else if (step2 <= max) {
                    step2 += 1;
                }
            }
        }
        return set;
    }

    @Override
    public void setBars() {
        super.setBars();

        addBar("step", (AutoBuild b) -> new Bar(Core.bundle.get("@step"), Pal.range, () -> (float) b.walls.size / maxLength));
    }

    public class AutoBuild extends Building {
        public final IntSet walls = new IntSet();
        public int config = 0;
        public boolean starting = false;
        public boolean beginning = false;
        //        public boolean auto = true;
        public float createTimer = 0;
        public int step = 0;

        @Override
        public void updateTile() {
            if (creates.isEmpty()) {
                kill();
            }
            if (starting) {
                if (walls.size >= maxLength || step * 8 >= maxRange / 2) {
                    kill();
                } else {
                    createTimer += Time.delta;
                    walls.each(bu -> {
                        Building b = Vars.world.build(bu);
                        if (b == null || b.team != this.team) {
                            kill();
                        }
                    });
                    if (createReload <= 0 || createTimer >= createReload) {
                        Seq<Block> blocks = creates.values().toSeq().sort(Comparator.comparingInt((Block o) -> o.id));
                        Block b = blocks.get(config);
                        float cx = tileX() + (b.size + behind) * Mathf.cosDeg(rotation * 90);
                        float cy = tileY() + (b.size + behind) * Mathf.sinDeg(rotation * 90);
                        if (rotation % 2 == 0) {
                            createBuild(b, cx * 8, (cy + step) * 8, 8, false, step * 8);
                            if (walls.size < maxLength) {
                                createBuild(b, cx * 8, (cy - step) * 8, -8, false, step * 8);
                            }
                        } else {
                            createBuild(b, (cx + step) * 8, cy * 8, 8, true, step * 8);
                            if (walls.size < maxLength) {
                                createBuild(b, (cx - step) * 8, cy * 8, -8, true, step * 8);
                            }
                        }
                        step = (int) Math.min(step + b.size, maxRange / 16 + 1);
                        createTimer %= createReload;
                    }
                }
            }
        }

        public void createBuild(Block b, float sx, float sy, int step, boolean tx, int all) {
            if (all >= maxRange / 2) {
                return;
            }
            Tile t = Vars.world.tileWorld(sx, sy);
            if (t != null && Build.validPlace(b, team, t.x, t.y, rotation, false)) {
                t.setBlock(b, team, rotation * 90);
                walls.add(t.pos());
                createEffect.at(sx, sy, rotation * 90, new Object[]{this, b});
            } else {
                if (tx) {
                    createBuild(b, sx + step, sy, step, true, all + Math.abs(step));
                } else {
                    createBuild(b, sx, sy + step, step, false, all + Math.abs(step));
                }
            }
        }

        public void add() {
            if (!this.added) {
                this.index__all = Groups.all.addIndex(this);
                this.index__build = Groups.build.addIndex(this);
                if (this.power != null) {
                    this.power.graph.checkAdd();
                }

                this.added = true;

                config = step = 0;
                createTimer = 0;
                beginning = starting = false;
                walls.clear();
            }
        }

        @Override
        public void buildConfiguration(Table t) {
            t.clear();
            if (starting || beginning) {
                return;
            }
            final int[] id = {0};
            t.table(s -> {
                Seq<Block> blocks = creates.values().toSeq().sort(Comparator.comparingInt((Block o) -> o.id));
                for (int i = 0; i < blocks.size; i++) {
                    int index = i;
                    Block b = blocks.get(i);
                    s.table(e -> {
                        e.clicked(() -> {
                            config = index;
                            buildConfiguration(t);
                        });
                        e.background(config == index ? Tex.buttonEdge1 : Tex.windowEmpty);
                        Image image = new Image(b.region);
                        e.add(image).size(15);
                        for (ItemStack stack : creates.findKey(b, true)) {
                            ItemDisplay display = new ItemDisplay(stack.item, stack.amount);
                            display.sizeBy(1);
                            e.add(display).pad(12);
                        }
                    }).growX();
                    id[0]++;
                    if (id[0] % 2 == 0) {
                        s.row();
                    }
                }
            }).grow();
            t.row();
            t.table(b -> b.button(Core.bundle.get("@start"), () -> {
                if (couldStart()) {
                    t.clear();
                    start();
                }
            }).growX()).growX();
        }

        public boolean couldStart() {
            Seq<Block> blocks = creates.values().toSeq().sort(Comparator.comparingInt((Block o) -> o.id));
            for (ItemStack stack : creates.findKey(blocks.get(config), true)) {
                if (items.get(stack.item) < stack.amount) {
                    return false;
                }
            }
            return Vars.state.isGame();
        }

        public void start() {
            items.remove(creates.findKey(creates.values().toSeq().sort(Comparator.comparingInt((Block o) -> o.id)).get(config), true));
            beginning = true;
            if (createDelay > 0) {
                beginEffect.at(this);
                Time.run(createDelay, () -> {
                    startEffect.at(this);
                    starting = true;
                    step = 0;
                    createTimer = 0;
                });
            } else {
                startEffect.at(this);
                starting = true;
                step = 0;
                createTimer = 0;
            }
        }

        @Override
        public int getMaximumAccepted(Item item) {
            Seq<Block> blocks = creates.values().toSeq().sort(Comparator.comparingInt((Block o) -> o.id));
            ItemStack[] items = creates.findKey(blocks.get(config), true);
            for (ItemStack stack : items) {
                if (stack.item == item) {
                    return stack.amount;
                }
            }
            return 0;
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            if (beginning || starting) {
                return false;
            }
            Seq<Block> blocks = creates.values().toSeq().sort(Comparator.comparingInt((Block o) -> o.id));
            ItemStack[] items = creates.findKey(blocks.get(config), true);
            ItemStack stack;
            for (ItemStack itemStack : items) {
                stack = itemStack;
                if (stack.item == item) {
                    return this.items.get(item) < itemStack.amount;
                }
            }
//            if (auto) {
//                final ItemStack[][] items2 = {null};
//                creates.each((i, b) -> {
//                    if (items2[0] == null) {
//                        for (ItemStack s : items) {
//                            if (s.item == item) {
//                                items2[0] = i;
//                                return;
//                            }
//                        }
//                    }
//                });
//                Seq<Item> different = new Seq<>(items.length);
//                if (items2[0] != null) {
//                    for (ItemStack stack : items) {
//                        for (ItemStack itemStack : items2[0]) {
//                            if (stack.item == itemStack.item) {
//                                break;
//                            }
//                            different.add(stack.item);
//                        }
//                    }
//                }
//            }
            return false;
        }

        @Override
        public Object config() {
            return (int) config;
        }

        @Override
        public void control(LAccess type, double p1, double p2, double p3, double p4) {
            if (type == LAccess.enabled) {
                this.enabled = !Mathf.zero((float) p1);
                if (p1 == 2 && couldStart()) {
                    start();
                }
            } else if (type == LAccess.config) {
                this.config = Math.min(creates.size - 1, (int) p1);
            }
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(config);
            write.i(step);
            write.f(createTimer);
            write.bool(starting);
            write.bool(beginning);
            write.i(walls.size);
            walls.each(write::i);
        }

        @Override
        public void read(Reads read, byte version) {
            super.read(read, version);
            this.config = read.i();
            this.step = read.i();
            this.createTimer = read.f();
            this.starting = read.bool();
            this.beginning = read.bool();
            int num = read.i();
            for (int i = 0; i < num; i++) {
                walls.add(read.i());
            }
        }
    }
}
