package Commonplace.Type.Control;

import Commonplace.Loader.DefaultContent.StatusEffects2;
import Commonplace.Utils.Interfaces.RangePure;
import arc.func.Intp;
import arc.math.geom.Position;
import arc.struct.IntMap;
import arc.struct.IntSeq;
import arc.struct.Seq;
import mindustry.content.Fx;
import mindustry.entities.Units;
import mindustry.type.Planet;
import mindustry.world.Tile;

import static arc.util.Time.delta;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static mindustry.Vars.*;

public class CorrosionMist {
    private static Seq<RangePure> tmp;
    private static final BoostWithTime zero = new BoostWithTime(0, 0);

    public static final Seq<String> maps = new Seq<>();
    public static final Seq<Planet> planets = new Seq<>();

    public final static Seq<RangePure> changer = new Seq<>();
    public final static IntMap<Seq<RangePure>> cleaner = new IntMap<>();
    public final static IntMap<Seq<RangePure>> booster = new IntMap<>();
    public final static IntMap<BoostWithTime> timeBoost = new IntMap<>();
    public static boolean update = false;

    public static void update() {
        if (!update) {
            if (maps.contains(state.map.name()) || (state.isCampaign() && planets.contains(state.rules.sector.planet))) {
                update = true;
            } else {
                return;
            }
        }
        if (state.isPaused() || state.isEditor()) {
            return;
        }

        changer.removeAll(r -> !r.couldUse());
        Seq<BoostWithTime> removes = new Seq<>();
        for (BoostWithTime bwt : timeBoost.values()) {
            bwt.go(delta, removes);
        }
        for (BoostWithTime bwt : removes) {
            int index = timeBoost.values().toArray().indexOf(bwt);
            timeBoost.remove(index);
        }
        for (var c : cleaner.values()) {
            c.removeAll(r -> r == null || !r.couldUse());
            c.sort(RangePure::level);
        }
        for (var c : booster.values()) {
            c.removeAll(r -> r == null || !r.couldUse());
            c.sort(RangePure::level);
        }

        Units.nearby(0, 0, world.width() * 8, world.height() * 8, u -> {
            Tile t = u.tileOn();
            if (t != null) {
                BoostWithTime bo = timeBoost.get(t.pos(), zero);
                int tt = Math.max(1, (tmp = booster.get(t.pos())) == null ? 1 : tmp.isEmpty() ? 1 : tmp.get(tmp.size - 1).level());
                int c = ((tmp = cleaner.get(t.pos())) == null ? 0 : tmp.isEmpty() ? 0 : tmp.first().level());
                float boost = max(tt, bo.level);
                for (int i = 0; i < boost + c; i++) {
                    u.apply(StatusEffects2.corrosionI, 60);
                }
            }
        });

        indexer.allBuildings(world.width() * 4, world.height() * 4, max(world.width(), world.height()) * 6, b -> {
            Tile t = b.tile;
            t.getLinkedTiles(ti -> {
                BoostWithTime bo = timeBoost.get(ti.pos(), zero);
                int tt = Math.max(1, (tmp = booster.get(ti.pos())) == null ? 1 : tmp.isEmpty() ? 1 : tmp.get(tmp.size - 1).level());
                int c = ((tmp = cleaner.get(ti.pos())) == null ? 0 : tmp.isEmpty() ? 0 : tmp.first().level());
                float boost = max(tt, bo.level);
                if (c + boost > 0) {
                    b.damage((boost + c) / 30);
                }
            });
            BoostWithTime bo = timeBoost.get(t.pos(), zero);
            int tt = Math.max(1, (tmp = booster.get(t.pos())) == null ? 1 : tmp.isEmpty() ? 1 : tmp.get(tmp.size - 1).level());
            int c = ((tmp = cleaner.get(t.pos())) == null ? 0 : tmp.isEmpty() ? 0 : tmp.first().level());
            float boost = max(tt, bo.level);
            if (c + boost > 0) {
                b.damage((boost + c) / 30);
            }
        });
    }

    public static void addChanger(RangePure change) {
        if (change != null && change.couldUse()) {
            if (change.level() > 0) {
                changer.add(change);
                change.changes().each(pos -> {
                    if (booster.get(pos) == null) {
                        booster.put(pos, new Seq<>());
                    }
                    booster.get(pos).add(change);
                });
            } else if (change.level() < 0) {
                changer.add(change);
                change.changes().each(pos -> {
                    if (cleaner.get(pos) == null) {
                        cleaner.put(pos, new Seq<>());
                    }
                    cleaner.get(pos).add(change);
                });
            }
        }
    }

    public static void removeChanger(RangePure change) {
        if (change != null) {
            change.changes().each(pos -> {
                tmp = cleaner.get(pos);
                if (tmp != null) {
                    tmp.remove(change);
                }
                tmp = booster.get(pos);
                if (tmp != null) {
                    tmp.remove(change);
                }
            });
            changer.remove(change);
        }
    }

    public static void addTimeBoost(int pos, int level, float time) {
        if (timeBoost.containsKey(pos)) {
            timeBoost.get(pos).tryChange(level, time);
        } else {
            timeBoost.put(pos, new BoostWithTime(level, time));
        }
    }

    public static class BoostWithTime {
        public int level;
        public float time;

        public BoostWithTime(int l, float t) {
            level = l;
            time = t;
        }

        public void go(float time, Seq<BoostWithTime> removes) {
            this.time -= time;
            if (this.time <= 0) {
                removes.add(this);
            }
        }

        public void tryChange(int level, float time) {
            if (level > this.level) {
                this.time = time;
                this.level = level;
            } else if (level == this.level) {
                this.time += time;
            }
        }

        public void setTime(float t) {
            this.time = t;
        }
    }
}
