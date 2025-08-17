package Commonplace.Entities.Ability;

import Commonplace.Loader.DefaultContent.StatusEffects2;
import Commonplace.Entities.Unit.BoostUnitEntity;
import arc.Core;
import arc.math.geom.Position;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;


public class EMPAbility extends Ability {
    private float timer = 3600;
    public float reload = 1800;
    public float range = 80;
    public float time = 600;
    public float delay = 60;
    public Effect stopEffect = Fx.lightningShoot;
    public Effect waveEffect = Fx.missileTrailSmoke;

    private final Seq<Position> maps = new Seq<>();

    @Override
    public void update(Unit unit) {
        if (!(unit instanceof BoostUnitEntity eu && eu.first) && !unit.hasEffect(StatusEffects2.superStop)) {
            timer = Math.min(reload, timer + Time.delta);
            if (timer >= reload) {
                maps.clear();
                Units.nearbyEnemies(unit.team, unit.x, unit.y, range, u -> {
                    if (u.type.estimateDps() > 0) {
                        maps.add(u);
                    }
                });
                Units.nearbyBuildings(unit.x, unit.y, range, b -> {
                    if (b.team != unit.team) {
                        maps.add(b);
                    }
                });
                if (maps.size >= 5) {
                    timer = 0;
                    if (delay > 0) {
                        Time.run(delay, () -> {
                            timer = 0;
                            for (Position p : maps) {
                                if (p instanceof Building b) {
                                    waveEffect.at(b.x, b.y);
                                    b.applySlowdown(0, time + 1);
                                } else if (p instanceof Unit u) {
                                    waveEffect.at(u.x, u.y);
                                    u.apply(StatusEffects2.superStop, time + 1);
                                }
                                stopEffect.at(p);
                            }

                            unit.apply(StatusEffects2.superStop, time + 1);
                        });
                    } else {
                        for (Position p : maps) {
                            if (p instanceof Building b) {
                                waveEffect.at(b.x, b.y);
                                b.applySlowdown(0, time + 1);
                            } else if (p instanceof Unit u) {
                                waveEffect.at(u.x, u.y);
                                u.apply(StatusEffects2.superStop, time + 1);
                            }
                            stopEffect.at(p);
                        }

                        unit.apply(StatusEffects2.superStop, time + 1);
                    }
                }
            }
        }
    }

    @Override
    public void addStats(Table t) {
        t.add("[lightgray]" + Stat.range.localized() + ": [white]" + range + " " + StatUnit.seconds.localized());
        t.row();
        t.add("[lightgray]" + Core.bundle.get("stats.continue") + ": [white]" + time / 60 + " " + StatUnit.seconds.localized());
        t.row();
        t.add("[lightgray]" + Stat.cooldownTime.localized() + ": [white]" + reload / 60 + " " + StatUnit.seconds.localized());
        t.row();
    }

    @Override
    public void displayBars(Unit unit, Table bars) {
        bars.add(new Bar(Stat.cooldownTime.localized(), Pal.accent, () -> Math.max(timer / reload, 1))).row();
    }

    @Override
    public String localized() {
        return Core.bundle.get(getBundle() + ".name");
    }

    @Override
    public String getBundle() {
        return "ability.emp";
    }
}
