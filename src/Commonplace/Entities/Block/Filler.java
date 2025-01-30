package Commonplace.Entities.Block;

import Commonplace.Type.Elements.ItemImage2;
import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.math.geom.Vec2;
import arc.scene.ui.Button;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Table;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.type.ItemStack;
import mindustry.ui.Bar;
import mindustry.ui.Styles;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.Tile;

import static mindustry.Vars.*;

public class Filler extends Block {
    public Block target;
    public float reload = 60;
    public float range = 200;
    public float itemMul = 0.8f;
    public float buildMul = 0.85f;

    private final Vec2 vec = new Vec2();

    public Filler(String name, Block target) {
        super(name);

        this.target = target;
        solid = true;
        update = true;
        hasPower = true;
        destructible = true;
        configurable = true;
        logicConfigurable = true;

        config(IntSet.class, (FillerBuild f, IntSet set) -> {
            f.sets = set;
            f.updateBase();
        });
    }

    public IntSet findPass(int x, int y, Team team) {
        int range = Math.round(this.range / tilesize);
        IntSet result = new IntSet();

        for (int i = -range; i < range; i++) {
            for (int j = -range; j < range; j++) {
                Building b = world.build(x + i, y + j);
                if (b != null && b.team == (team == null ? player.team() : team) && b.block == target && !result.contains(b.pos()) && b.dst(x * tilesize + offset, y * tilesize + offset) < this.range) {
                    result.add(b.pos());
                }
            }
        }
        return result;
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        super.drawPlace(x, y, rotation, valid);

        Draw.color(Pal.accent);
        Lines.stroke(1.0f);
        Drawf.circles(x * tilesize + offset, y * tilesize + offset, range);

        drawTarget(x, y);
    }

    public void drawTarget(int x, int y) {
        Draw.color(Color.orange);
        Draw.alpha(Mathf.sin(Time.time / 10));

        IntSet all = findPass(x, y, null);
        all.each(p -> {
            Tile t = world.tile(p);
            drawChoose(t.getX(), t.getY(), target.size * tilesize);
        });
    }

    public void drawChooseShow(float x, float y, int radius, boolean configured, boolean disable, boolean square) {
        if (configured) {
            int mx = Math.round(Core.input.mouseWorldX() / tilesize), my = Math.round(Core.input.mouseWorldY() / tilesize);
            int cx = (int) ((x - target.offset) / tilesize + sizeOffset), cy = (int) ((y - target.offset) / tilesize + sizeOffset);
            int dx = mx - cx, dy = my - cy, size = target.size;
            if ((square && dx >= 0 && dx < size && dy >= 0 && dy < size) || (!square && Math.abs(dx) < size && Math.abs(dy) < size)) {
                Draw.color(Color.red);
            } else if (disable) {
                Draw.color(Color.gray);
            } else {
                Draw.color(Color.orange);
            }
        }

        drawChoose(x, y, radius);
    }

    public void drawChooseSqr(float x, float y, int radius, boolean delete) {
        Draw.color(delete ? Color.red : Color.orange);
        Draw.alpha(Mathf.sin(Time.time / 10));

        drawChoose(x, y, radius);
    }

    public void drawChoose(float x, float y, int radius) {
        Lines.stroke(0.75f);

        drawBorder(x, y, radius);
        drawCross(x, y, radius);
    }

    public void drawBorder(float x, float y, float radius) {
        float len = radius * 0.15f, off = radius * 0.38f;
        for (Point2 p : Geometry.d8edge) {
            Lines.line(x + off * p.x, y + off * p.y, x + off * p.x - len * p.x, y + off * p.y);
            Lines.line(x + off * p.x, y + off * p.y, x + off * p.x, y + off * p.y - len * p.y);
        }
    }

    public void drawCross(float x, float y, int radius) {
        vec.trns(0, radius * 0.25f);
        Lines.line(x + vec.x, y + vec.y, x - vec.x, y - vec.y);
        vec.trns(90, radius * 0.25f);
        Lines.line(x + vec.x, y + vec.y, x - vec.x, y - vec.y);

        setBars();
    }

    @Override
    public void setBars() {
        super.setBars();

        addBar("progress", (FillerBuild build) -> new Bar(Core.bundle.get("@progress"), Pal.redderDust, () -> build.progress));
    }

    protected static class SqrPos {
        final IntSet adds;
        final IntSet removes;

        SqrPos(IntSet adds, IntSet removes) {
            this.adds = adds;
            this.removes = removes;
        }

        public void clear() {
            adds.clear();
            removes.clear();
        }

        public boolean isEmpty() {
            return adds.isEmpty() && removes.isEmpty();
        }
    }

    public class FillerBuild extends Building {
        public float need;
        public float timer = 0;
        public float progress = 0;
        public IntSet sets = new IntSet();
        public IntSet disable = new IntSet();
        public ItemStack[] itemNeed = ItemStack.empty;

        public boolean sqrChoose = false;
        public final IntSeq sqr = new IntSeq();

        protected final SqrPos sqrResult = new SqrPos(IntSet.with(), IntSet.with());

        private final Label info = new Label(() -> "");
        private final Button out = new Button(Styles.defaultb) {{
            image(Icon.left);
            clicked(() -> {
                stopSqr();
                deselect();
            });
        }}, clear = new Button(Styles.defaultb) {{
            image(Icon.trash);
            clicked(() -> {
                sets.clear();
                disable.clear();
                updateNeed();
                stopSqr();
            });
        }}, auto = new Button(Styles.defaultb) {{
            image(Icon.admin);
            clicked(() -> {
                FillerBuild.this.reset();
                stopSqr();
            });
        }}, square = new Button(Styles.defaultb) {{
            image(Icon.hammer);
            clicked(() -> {
                if (sqrChoose) {
                    stopSqr();
                } else {
                    sqrChoose = true;
                }
            });
        }}, save = new Button(Styles.defaultb) {{
            setDisabled(() -> !sqrChoose);
            image(Icon.save);
            clicked(() -> {
                if (!sqrResult.isEmpty()) {
                    sqr.clear();
                    sets.addAll(sqrResult.adds);
                    sqrResult.removes.each(sets::remove);
                    sqrResult.clear();
                }
            });
        }};

        @Override
        public void updateTile() {
            updateDisable();
            updateNeed();

            if (sets.size > disable.size) {
                timer = Math.min(reload + need, timer + Time.delta * efficiency);
                progress = timer / (reload + need);
            }

            if (canStart()) {
                start();
            }
        }

        @Override
        public void drawConfigure() {
            super.drawConfigure();

            Draw.color(Pal.accent);
            Lines.stroke(1.0f);
            Drawf.circles(x, y, range);

            int mx = Math.round(Core.input.mouseWorldX() / tilesize), my = Math.round(Core.input.mouseWorldY() / tilesize);

            boolean[] drawPoint = {dst(mx * tilesize, my * tilesize) <= range};
            sets.each(p -> {
                int x = Point2.x(p), y = Point2.y(p);
                if (Math.abs(x - mx) < target.size && Math.abs(y - my) < target.size) {
                    drawPoint[0] = false;
                }
                drawChooseShow(target.offset + x * tilesize, target.offset + y * tilesize, target.size * tilesize, true, disable.contains(p), sqrChoose);
            });

            if (sqrChoose) {
                int index = findClosePos(Core.input.mouseWorldX(), Core.input.mouseWorldY());

                for (int i = 1; i < sqr.size; i += 2) {
                    int x1 = Point2.x(sqr.get(i)), y1 = Point2.y(sqr.get(i));
                    int x2 = Point2.x(sqr.get(i - 1)), y2 = Point2.y(sqr.get(i - 1));

                    Draw.color(index == i ? Color.red : Pal.removeBack);
                    Lines.stroke(1.2f);
                    drawBorder(x1 * tilesize, y1 * tilesize, tilesize);
                    drawBorder(x1 * tilesize, y2 * tilesize, tilesize);
                    drawBorder(x2 * tilesize, y1 * tilesize, tilesize);
                    drawBorder(x2 * tilesize, y2 * tilesize, tilesize);
                    Lines.stroke(1.5f);
                    Lines.rect(Math.min(x1, x2) * tilesize - 2f, Math.min(y1, y2) * tilesize - 2f, Math.abs(x1 - x2) * tilesize + 4f, Math.abs(y1 - y2) * tilesize + 4f);
                }

                if (sqr.size % 2 == 1) {
                    Draw.color(Point2.pack(mx, my) == sqr.get(sqr.size - 1) ? Color.red : Pal.removeBack);
                    Lines.stroke(1.2f);
                    drawBorder(Point2.x(sqr.get(sqr.size - 1)) * tilesize, Point2.y(sqr.get(sqr.size - 1)) * tilesize, tilesize);
                }

                if (!sqrResult.isEmpty()) {
                    sqrResult.adds.each(p -> drawChooseSqr(target.offset + Point2.x(p) * tilesize, target.offset + Point2.y(p) * tilesize, target.size * tilesize, false));
                    sqrResult.removes.each(p -> drawChooseSqr(target.offset + Point2.x(p) * tilesize, target.offset + Point2.y(p) * tilesize, target.size * tilesize, true));
                }

                if (index < 0 && dst(mx * tilesize, my * tilesize) <= range) {
                    Lines.stroke(1.2f);
                    Draw.color(Pal.removeBack.cpy());

                    drawBorder(mx * tilesize, my * tilesize, tilesize);
                }
            } else if (drawPoint[0]) {
                Lines.stroke(1.2f);
                Draw.color(Color.green.cpy().mul(1.2f));

                drawBorder(target.offset + mx * tilesize, target.offset + my * tilesize, target.size * tilesize);
            }

        }

        @Override
        public void drawSelect() {
            super.drawSelect();

            Draw.color(Pal.accent);
            Lines.stroke(1.0f);
            Drawf.circles(x, y, range);

            if (Vars.control.input.config.getSelected() != this) {
                sets.each(p -> drawChooseSqr(target.offset + Point2.x(p) * tilesize, target.offset + Point2.y(p) * tilesize, target.size * tilesize, false));
            }
        }

        @Override
        public boolean canConsume() {
            return team.core() != null && team.core().items.has(itemNeed);
        }

        protected boolean canStart() {
            return progress >= 1 && canConsume() && sets.size - finished() - disable.size > 0;
        }

        protected void start() {
            timer = 0;
            team.core().items.remove(itemNeed);

            sets.each(p -> {
                if (!disable.contains(p) && !(world.build(p) != null && world.build(p).tile == world.tile(p))) {
                    world.tile(p).setBlock(target, team);
                }
            });
        }

        protected boolean shouldDisable(int p) {
            Tile t = world.tile(p);
            return (t.build == null || t.team() != team || t.build.block != target || t.build.tile != t) && !Build.validPlace(target, team, Point2.x(p), Point2.y(p), 0);
        }

        protected int finished() {
            final int[] num = {0};
            sets.each(p -> {
                Building b = world.build(p);
                if (b != null && b.team == team && b.block == target && b.tile == world.tile(p)) {
                    num[0]++;
                }
            });
            return num[0];
        }

        protected void reset() {
            sets = findPass(tileX(), tileY(), team);
            updateBase();
        }

        protected int findClosePos(float x, float y) {
            int sx = Math.round(x / tilesize), sy = Math.round(y / tilesize);
            if (sqr.size % 2 == 1) {
                if (Point2.pack(sx, sy) == sqr.get(sqr.size - 1)) {
                    return sqr.size - 1;
                }
            }

            for (int i = 1; i < sqr.size; i += 2) {
                int x1 = Point2.x(sqr.get(i)), y1 = Point2.y(sqr.get(i));
                int x2 = Point2.x(sqr.get(i - 1)), y2 = Point2.y(sqr.get(i - 1));

                if (sx == x1 || sx == x2) {
                    if ((sy - y1) * (sy - y2) <= 0) {
                        return i;
                    }
                } else if (sy == y1 || sy == y2) {
                    if ((sx - x1) * (sx - x2) <= 0) {
                        return i;
                    }
                }
            }

            return -1;
        }

        protected void stopSqr() {
            sqr.clear();
            sqrResult.clear();
            sqrChoose = false;
        }

        protected void updateDisable() {
            disable.clear();
            sets.each(p -> {
                if (shouldDisable(p)) {
                    disable.add(p);
                }
            });
        }

        protected void updateNeed() {
            int size = sets.size - disable.size - finished();
            need = size * target.buildCost / buildMul;
            itemNeed = new ItemStack[target.requirements.length];
            for (int i = 0; i < target.requirements.length; i++) {
                itemNeed[i] = new ItemStack(target.requirements[i].item, (int) (target.requirements[i].amount * size * itemMul));
            }
        }

        protected void updateBase() {
            updateDisable();
            updateNeed();
        }

        protected void updateSqrResult() {
            if (sqrChoose) {
                int size = target.size, off = target.sizeOffset;
                IntSet adds = sqrResult.adds;
                IntSet removes = sqrResult.removes;
                adds.clear();
                removes.clear();
                IntSet base = new IntSet(sets);
                if (base.size * sqr.size >= sqr.size * 2 + base.size) {
                    IntSet live = new IntSet();
                    int lx = world.width() + 1, ly = world.height() + 1, rx = -1, ry = -1, p1, p2;
                    for (int i = 1; i < sqr.size; i += 2) {
                        p1 = sqr.get(i);
                        p2 = sqr.get(i - 1);
                        lx = Math.min(lx, Math.min(Point2.x(p1), Point2.x(p2)));
                        ly = Math.min(ly, Math.min(Point2.y(p1), Point2.y(p2)));
                        rx = Math.max(rx, Math.max(Point2.x(p1), Point2.x(p2)));
                        ry = Math.max(ry, Math.max(Point2.y(p1), Point2.y(p2)));
                    }
                    final int flx = Math.max(1, lx), fly = Math.max(1, ly), frx = Math.min(world.width() - 1, rx), fry = Math.min(world.height() - 1, ry);

                    base.each(p -> {
                        int px = Point2.x(p) + off, py = Point2.y(p) + off;
                        if (flx - px < size && frx >= px && fly - py < size && fry >= py) {
                            live.add(p);
                        }
                    });
                    base = live;
                }

                base.each(p -> {
                    int bx = Point2.x(p) + off, by = Point2.y(p) + off;
                    for (int i = 1; i < sqr.size; i += 2) {
                        int p1 = sqr.get(i), p2 = sqr.get(i - 1);
                        int x1 = Point2.x(p1), y1 = Point2.y(p1);
                        int x2 = Point2.x(p2), y2 = Point2.y(p2);
                        int lx = Math.max(1, Math.min(x1, x2)), ly = Math.max(1, Math.min(y1, y2));
                        int rx = Math.min(world.width() - 1, Math.max(x1, x2)), ry = Math.min(world.height() - 1, Math.max(y1, y2));

                        if (lx - bx < size && rx >= bx && ly - by < size && ry >= by && dst(world.tile(p)) < range) {
                            removes.add(p);
                            break;
                        }
                    }
                });

                for (int i = 1; i < sqr.size; i += 2) {
                    int p;
                    int p1 = sqr.get(i), p2 = sqr.get(i - 1);
                    int x1 = Point2.x(p1), y1 = Point2.y(p1);
                    int x2 = Point2.x(p2), y2 = Point2.y(p2);
                    int lx = Math.max(1, Math.min(x1, x2)), ly = Math.max(1, Math.min(y1, y2));
                    int rx = Math.min(world.width() - 1, Math.max(x1, x2)), ry = Math.min(world.height() - 1, Math.max(y1, y2));

                    for (int j = lx + size - 1; j <= rx; j += size) {
                        for (int k = ly + size - 1; k <= ry; k += size) {
                            p = Point2.pack(j + 1 - size - off, k + 1 - size - off);
                            if (dst(world.tile(p)) < range) {
                                adds.add(p);
                            }
                        }
                    }
                }
            } else {
                sqrResult.clear();
            }
        }

        @Override
        public void buildConfiguration(Table table) {
            table.clear();
            table.table(s -> {
                s.add(out, clear, auto, square, save);
                s.row();
                s.add(info);
            }).growX();
        }

        @Override
        public boolean onConfigureBuildTapped(Building build) {
            return true;
        }

        @Override
        public boolean onConfigureTapped(float x, float y) {
            if (dst(x, y) > range) {
                stopSqr();
                return false;
            }

            int i = Point2.pack(Math.round(x / tilesize), Math.round(y / tilesize));
            if (sqrChoose) {
                int index = findClosePos(x, y);
                if (index >= 0) {
                    if (index % 2 == 0) {
                        sqr.removeIndex(index);
                    } else {
                        sqr.removeRange(index - 1, index);
                        updateSqrResult();
                    }
                } else {
                    sqr.add(i);
                    if (sqr.size % 2 == 0) {
                        updateSqrResult();
                    }
                }
            } else {
//                int dx = Math.round(x / tilesize) - sizeOffset - tileX(), dy = Math.round(y / tilesize) - sizeOffset - tileY();
//                if (dx < size && dx >= 0 && dy < size && dy >= 0) {
//                    if (sets.size > 0) {
//                        disable.clear();
//                        sets.clear();
//                    } else {
//                        reset();
//                    }
//                    return false;
//                }

                if (sets.contains(i)) {
                    sets.remove(i);
                    if (disable.contains(i)) {
                        disable.remove(i);
                    } else {
                        updateNeed();
                    }
                } else {
                    int s = target.size, o = target.sizeOffset, x0 = Point2.x(i), y0 = Point2.y(i);
                    if (x0 + o < 0 || y0 + o < 0 || x0 + o + s > world.width() || y0 + o + s > world.height()) {
                        return false;
                    }

                    final int[] remove = {-1, -1, -1, -1};
                    sets.each(p -> {
                        if (Math.abs(Point2.x(p) - x0) < s && Math.abs(Point2.y(p) - y0) < s) {
                            int index = remove[0] < 0 ? 0 : remove[1] < 0 ? 1 : remove[2] < 0 ? 2 : 3;
                            remove[index] = p;
                        }
                    });
                    for (int p : remove) {
                        if (p >= 0) {
                            sets.remove(p);
                        }
                    }
                    sets.add(i);
                    if (shouldDisable(i)) {
                        disable.add(i);
                    } else {
                        updateNeed();
                    }
                }
                updateNeed();
            }
            return true;
        }

        @Override
        public void displayConsumption(Table table) {
            super.displayConsumption(table);

            table.row();
            table.left();
            table.table(l -> {
                Runnable rebuild = () -> {
                    l.clearChildren();
                    l.left();

                    int i = 0;
                    for (ItemStack stack : itemNeed) {
                        l.add(new ItemImage2(stack.item.uiIcon, stack.amount, core() == null ? 0 : core().items.get(stack.item))).padRight(8);
                        if (++i % 2 == 0) l.row();
                    }
                };

                rebuild.run();
                l.update(rebuild);
            }).left();
        }

        @Override
        public void configured(Unit builder, Object value) {
            super.configured(builder, value);
            if (value instanceof IntSet set) {
                this.sets = set;
                updateBase();
            }
        }

        @Override
        public Object config() {
            return new IntSet(sets);
        }

        @Override
        public void placed() {
            super.placed();
            if (!Vars.net.client()) {
                reset();
            }
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(timer);
            write.i(sets.size);
            sets.each(write::i);
        }

        @Override
        public void read(Reads read, byte version) {
            super.read(read);
            timer = read.f();
            int num = read.i();
            for (int i = 0; i < num; i++) {
                sets.add(read.i());
            }
        }
    }
}
