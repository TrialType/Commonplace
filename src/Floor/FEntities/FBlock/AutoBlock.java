package Floor.FEntities.FBlock;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Table;
import arc.struct.IntSet;
import arc.struct.ObjectMap;
import arc.util.Eachable;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.units.BuildPlan;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Tex;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.ui.ItemDisplay;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.meta.Env;

public class AutoBlock extends Block {
    private int index = 0;
    private float timer = 0;

    public ObjectMap<ItemStack[], Block> creates = new ObjectMap<>();
    public int behind = 4;
    public int maxLength = 20;
    public float maxRange = 250;
    public float createReload = 15;
    public float createDelay = 120;

    public Effect startEffect = new Effect(180, e -> {
        Draw.color(Pal.coalBlack);
        Lines.stroke(3 * (1 - e.fin()));
        Lines.poly(e.x, e.y, 36, 250 * e.fin(), 360);
    });
    public Effect createEffect = new Effect(90, e -> {
        if (e.data instanceof Object[] o) {
            Building n = (Building) o[0];
            Block b = (Block) o[1];
            Draw.color(Color.white);
            Drawf.square(e.x, e.y, b.size * 2 * (1 - e.fin()), Color.white);
            Lines.stroke(2 * (1 - e.fin()));
            Lines.line(e.x, e.y, n.x, n.y);
        }
    });
    public Effect beginEffect = Fx.healWave;

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
        envEnabled = Env.any;
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
        set.each(p -> Draw.rect(finalDrawer.region, Point2.x(p) * 8, Point2.y(p) * 8));
    }

    public IntSet buildResult(int x, int y, int rotation, int lon, Block type) {
        IntSet set = new IntSet();
        Tile t;
        if (rotation % 2 == 0) {
            for (int i = 0; set.size < maxLength && i * 8 <= maxRange / 2; i += lon) {
                if ((t = Vars.world.tileWorld(x * 8, (y + i) * 8)) != null &&
                        Build.validPlace(type,
                                Vars.player == null ? Team.derelict : Vars.player.team(),
                                x, y + i, rotation, false)) {
                    set.add(t.pos());
                }
                if ((t = Vars.world.tileWorld(x * 8, (y - i) * 8)) != null &&
                        Build.validPlace(type,
                                Vars.player == null ? Team.derelict : Vars.player.team(),
                                x, y - i, rotation, false)) {
                    set.add(t.pos());
                }
            }
        } else {
            for (int i = 0; set.size < maxLength && i * 8 <= maxRange / 2; i += lon) {
                if ((t = Vars.world.tileWorld((x + i) * 8, y * 8)) != null &&
                        Build.validPlace(type,
                                Vars.player == null ? Team.derelict : Vars.player.team(),
                                x + i, y, rotation, false)) {
                    set.add(t.pos());
                }
                if ((t = Vars.world.tileWorld((x - i) * 8, y * 8)) != null &&
                        Build.validPlace(type,
                                Vars.player == null ? Team.derelict : Vars.player.team(),
                                x - i, y, rotation, false)) {
                    set.add(t.pos());
                }
            }
        }
        return set;
    }

    public class AutoBuild extends Building {
        public final IntSet walls = new IntSet();
        public int config = 0;
        public boolean starting = false;
        public boolean beginning = false;
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
                        Block b = creates.values().toSeq().get(config);
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
                createEffect.at(sx, sy, b.size, new Object[]{this, b});
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
            t.table(s -> creates.each((i, b) -> {
                int index = creates.keys().toSeq().indexOf(i);
                s.table(e -> {
                    e.clicked(() -> {
                        config = index;
                        buildConfiguration(t);
                    });
                    e.background(config == index ? Tex.buttonEdge1 : Tex.windowEmpty);
                    Image image = new Image(b.region);
                    e.add(image).size(15);
                    for (ItemStack stack : i) {
                        ItemDisplay display = new ItemDisplay(stack.item, stack.amount);
                        display.sizeBy(1);
                        e.add(display).pad(12);
                    }
                }).growX();
                id[0]++;
                if (id[0] % 2 == 0) {
                    s.row();
                }
            })).grow();
            t.row();
            t.table(b -> b.button(Core.bundle.get("@start"), () -> {
                if (couldStart()) {
                    t.clear();
                    start();
                }
            }).growX()).growX();
        }

        public boolean couldStart() {
            for (ItemStack stack : creates.keys().toSeq().get(config)) {
                if (items.get(stack.item) < stack.amount) {
                    return false;
                }
            }
            return Vars.state.isGame();
        }

        public void start() {
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
        public boolean acceptItem(Building source, Item item) {
            ItemStack[] items = creates.keys().toSeq().get(config);
            ItemStack stack;
            for (ItemStack itemStack : items) {
                stack = itemStack;
                if (stack.item == item) {
                    return this.items.get(item) < itemStack.amount;
                }
            }
            return false;
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(config);
            write.i(step);
            write.f(createTimer);
            write.bool(starting);
            write.bool(beginning);
        }

        @Override
        public void read(Reads read, byte version) {
            super.read(read, version);
            this.config = read.i();
            this.step = read.i();
            this.createTimer = read.f();
            this.starting = read.bool();
            this.beginning = read.bool();
        }
    }
}
