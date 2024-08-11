package Commonplace.Content.SpecialContent;

import Commonplace.Content.ProjectContent.Abilities;
import Commonplace.Content.ProjectContent.Bullets;
import Commonplace.Content.ProjectContent.Weapons;
import Commonplace.Entities.FAbility.LevelSign;
import Commonplace.Entities.FUnit.Override.*;
import Commonplace.Tools.Interfaces.BuildUpGrade;
import Commonplace.Tools.Interfaces.UnitUpGrade;
import Commonplace.Tools.Interfaces.UpGradeTime;
import Commonplace.FType.FDialog.ProjectDialog;
import Commonplace.FType.FDialog.Old.MoreResearchDialog;
import Commonplace.FType.Extent.CorrosionMist;
import arc.func.Prov;
import arc.struct.ObjectMap;
import arc.util.Time;
import mindustry.Vars;
import mindustry.ai.types.MissileAI;
import mindustry.game.EventType;
import mindustry.gen.*;
import mindustry.type.UnitType;

import java.util.Random;

import static Commonplace.Tools.Classes.UnitUpGrade.getPower;
import static java.lang.Math.*;


public class Events {
    private static final ObjectMap<Class<?>, Prov<? extends Unit>> classes = new ObjectMap<>();
    private static final Random r = new Random();

    static {
        classes.put(MechUnit.class, FMechUnit::create);
        classes.put(LegsUnit.class, FLegsUnit::create);
        classes.put(UnitEntity.class, FUnitEntity::create);
        classes.put(UnitWaterMove.class, FUnitWaterMove::create);
        classes.put(ElevationMoveUnit.class, FElevationMoveUnit::create);
        classes.put(TankUnit.class, FTankUnit::create);
        classes.put(PayloadUnit.class, FPayloadUnit::create);
    }

    public static void load() {
        arc.Events.on(EventType.ClientLoadEvent.class, e -> Time.runTask(10f, () -> Vars.ui.research = new MoreResearchDialog()));
        arc.Events.on(EventType.ClientLoadEvent.class, e -> Time.runTask(10f, ProjectDialog::create));
        arc.Events.on(EventType.ContentInitEvent.class, e -> {
            Bullets.load();
            Weapons.load();
            Abilities.load();
        });
        arc.Events.on(EventType.ContentInitEvent.class, e -> {
            for (UnitType u : Vars.content.units()) {
                if (classes.get(u.constructor.get().getClass()) != null) {
                    u.constructor = classes.get(u.constructor.get().getClass());
                    u.abilities.add(new LevelSign());
                }
            }
        });

        arc.Events.on(EventType.WorldLoadEndEvent.class, e -> CorrosionMist.init());

        arc.Events.on(GetPowerEvent.class, e -> {
            if (e.getter instanceof UnitUpGrade uug) {
                if (e.full) {
                    getPower(uug, 0, false, true);
                } else {
                    int num = min(60 - uug.baseLevel(), e.number);
                    uug.setLevel(uug.getLevel() + num);
                    getPower(uug, num, true, false);
                }
            }
        });

        arc.Events.on(EventType.UnitCreateEvent.class, e -> {
            if (e.unit instanceof UnitUpGrade uug) {
                int n = r.nextInt(6);
                uug.setLevel(uug.getLevel() + n);
                getPower(uug, n, true, false);
            }
        });
        arc.Events.on(EventType.UnitSpawnEvent.class, e -> {
            if (e.unit instanceof UnitUpGrade uug) {
                int extra = e.unit.isBoss() ? Vars.state.wave / 5 : 0;
                if (Vars.state.wave >= 8 && Vars.state.wave < 18) {
                    int n = r.nextInt(6) + extra;
                    uug.setLevel(n);
                    getPower(uug, n, true, false);
                } else if (Vars.state.wave >= 18 && Vars.state.wave < 30) {
                    uug.setLevel(7 + extra);
                    getPower(uug, 7 + extra, true, false);
                } else if (Vars.state.wave >= 30 && Vars.state.wave < 42) {
                    uug.setLevel(13 + extra);
                    getPower(uug, 13 + extra, true, false);
                } else if (Vars.state.wave >= 42 && Vars.state.wave < 55) {
                    uug.setLevel(19 + extra);
                    getPower(uug, 19 + extra, true, false);
                } else if (Vars.state.wave >= 55 && Vars.state.wave < 70) {
                    uug.setLevel(25 + extra);
                    getPower(uug, 25 + extra, true, false);
                } else if (Vars.state.wave >= 70 && Vars.state.wave <= 85) {
                    uug.setLevel(32 + extra);
                    getPower(uug, 32 + extra, true, false);
                } else if (Vars.state.wave >= 85 && Vars.state.wave <= 100) {
                    uug.setLevel(40 + extra);
                    getPower(uug, 40 + extra, true, false);
                } else if (Vars.state.wave >= 100 && Vars.state.wave <= 115) {
                    int lev = 50 + extra;
                    uug.setLevel(lev);
                    if (lev >= 60) {
                        getPower(uug, 0, false, true);
                    } else {
                        getPower(uug, lev, true, false);
                    }
                } else if (Vars.state.wave >= 115 && Vars.state.wave <= 135) {
                    getPower(uug, 0, false, true);
                    uug.setLevel(60 + extra);
                } else if (Vars.state.wave > 135) {
                    getPower(uug, 0, false, true);
                    uug.setLevel(60 + Math.min(Vars.state.wave - 135, 120) + extra);
                }
            }
        });

        arc.Events.on(EventType.UnitBulletDestroyEvent.class, e -> {
            if (e.unit instanceof UnitUpGrade || e.unit.maxHealth() >= 1000) {
                if (e.bullet.owner instanceof UnitUpGrade uug) {
                    uug.addExp(getExp(e.unit.maxHealth, uug.getLevel(),
                            e.unit instanceof UnitUpGrade ku ? ku.getLevel() : 0));
                    int n = uug.number();
                    int min = min(60 - uug.baseLevel(), n);
                    getPower(uug, min, true, false);
                } else if (e.bullet.owner instanceof Unit u && u.controller() instanceof MissileAI ai && ai.shooter instanceof UnitUpGrade uug) {
                    uug.addExp(getExp(e.unit.maxHealth, uug.getLevel(),
                            e.unit instanceof UnitUpGrade ku ? ku.getLevel() : 0));
                    int n = uug.number();
                    int min = min(60 - uug.baseLevel(), n);
                    getPower(uug, min, true, false);
                }
            }
            if (e.bullet.owner instanceof Unit u) {
                if (u.controller() instanceof MissileAI ai && ai.shooter instanceof UpGradeTime ugt) {
                    ugt.add(1);
                }
            }
        });
        arc.Events.on(Events.UnitDestroyOtherEvent.class, e -> {
            if (e.other instanceof UnitUpGrade || e.other.maxHealth() >= 1000) {
                if (e.killer instanceof UnitUpGrade uug) {
                    uug.addExp(getExp(e.other.maxHealth(), uug.getLevel(),
                            e.other instanceof UnitUpGrade ku ? ku.getLevel() : 0));
                    int n = uug.number();
                    int min = min(60 - uug.baseLevel(), n);
                    getPower(uug, min, true, false);
                } else if (e.killer.controller() instanceof MissileAI ai && ai.shooter instanceof UnitUpGrade uug) {
                    uug.addExp(getExp(e.other.maxHealth(), uug.getLevel(),
                            e.other instanceof UnitUpGrade ku ? ku.getLevel() : 0));
                    int n = uug.number();
                    int min = min(60 - uug.baseLevel(), n);
                    getPower(uug, min, true, false);
                }
            }
            if (e.killer.controller() instanceof MissileAI ai && ai.shooter instanceof UpGradeTime ugt) {
                ugt.add(1);
            }
        });

        arc.Events.on(EventType.UnitBulletDestroyEvent.class, e -> {
            if (e.bullet.owner instanceof BuildUpGrade fug) {
                if (e.unit instanceof UnitUpGrade uug) {
                    fug.addExp(e.unit.maxHealth * Math.max(1, uug.getLevel() / 15));
                } else {
                    fug.addExp(e.unit.maxHealth);
                }
            }
        });
        arc.Events.on(EventType.BuildingBulletDestroyEvent.class, e -> {
            if (e.bullet.owner instanceof BuildUpGrade fug) {
                fug.addExp(e.build.maxHealth);
            }
        });
    }

    public static float getExp(float maxHealth, int level1, int level2) {
        float multiplier = 1;
        if (level1 > level2) {
            float high = level1 - level2;
            multiplier -= 2 * high / level1;
        }
        return maxHealth * max(Vars.state.wave / 50, 1) * max(1, level2 / 30) * (multiplier < 0 ? 0 : multiplier);
    }

    public static class UnitDestroyOtherEvent {
        public Unit killer;
        public Healthc other;

        public UnitDestroyOtherEvent(Unit killer, Healthc other) {
            this.killer = killer;
            this.other = other;
        }
    }

    public static class GetPowerEvent {
        Unit getter;
        int number;
        boolean full;

        public GetPowerEvent(Unit u, int n, boolean full) {
            getter = u;
            number = n;
            this.full = full;
        }
    }
}