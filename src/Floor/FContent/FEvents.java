package Floor.FContent;

import Floor.FTools.interfaces.BuildUpGrade;
import Floor.FTools.interfaces.UnitUpGrade;
import Floor.FTools.interfaces.UpGradeTime;
import Floor.FType.FDialog.New.ProjectDialog;
import Floor.FType.FDialog.Old.MoreResearchDialog;
import Floor.FType.Extent.CorrosionMist;
import arc.Events;
import arc.util.Time;
import mindustry.Vars;
import mindustry.ai.types.MissileAI;
import mindustry.game.EventType;
import mindustry.gen.Healthc;
import mindustry.gen.Unit;

import java.util.Random;

import static java.lang.Math.max;
import static java.lang.Math.min;


public class FEvents {
    private static final Random r = new Random();

    public static void load() {
        Events.on(EventType.ClientLoadEvent.class, e -> Time.runTask(10f, () -> Vars.ui.research = new MoreResearchDialog()));
        Events.on(EventType.ClientLoadEvent.class, e -> Time.runTask(10f, ProjectDialog::create));

        Events.on(EventType.WorldLoadEndEvent.class, e -> CorrosionMist.init());

        Events.on(GetPowerEvent.class, e -> {
            if (e.getter instanceof UnitUpGrade uug) {
                if (e.full) {
                    Floor.FTools.classes.UnitUpGrade.getPower(uug, 0, false, true);
                } else {
                    int num = min(60 - uug.baseLevel(), e.number);
                    uug.setLevel(uug.getLevel() + num);
                    Floor.FTools.classes.UnitUpGrade.getPower(uug, num, true, false);
                }
            }
        });

        Events.on(EventType.UnitCreateEvent.class, e -> {
            if (e.unit instanceof UnitUpGrade uug) {
                int n = r.nextInt(6);
                uug.setLevel(uug.getLevel() + n);
                Floor.FTools.classes.UnitUpGrade.getPower(uug, n, true, false);
            }
        });
        Events.on(EventType.UnitSpawnEvent.class, e -> {
            if (e.unit instanceof UnitUpGrade uug) {
                if (Vars.state.wave >= 8 && Vars.state.wave < 18) {
                    int n = r.nextInt(6);
                    uug.setLevel(n);
                    Floor.FTools.classes.UnitUpGrade.getPower(uug, n, true, false);
                } else if (Vars.state.wave >= 18 && Vars.state.wave < 30) {
                    uug.setLevel(7);
                    Floor.FTools.classes.UnitUpGrade.getPower(uug, 7, true, false);
                } else if (Vars.state.wave >= 30 && Vars.state.wave < 42) {
                    uug.setLevel(13);
                    Floor.FTools.classes.UnitUpGrade.getPower(uug, 13, true, false);
                } else if (Vars.state.wave >= 42 && Vars.state.wave < 55) {
                    uug.setLevel(19);
                    Floor.FTools.classes.UnitUpGrade.getPower(uug, 19, true, false);
                } else if (Vars.state.wave >= 55 && Vars.state.wave < 70) {
                    uug.setLevel(25);
                    Floor.FTools.classes.UnitUpGrade.getPower(uug, 25, true, false);
                } else if (Vars.state.wave >= 70 && Vars.state.wave <= 85) {
                    uug.setLevel(32);
                    Floor.FTools.classes.UnitUpGrade.getPower(uug, 32, true, false);
                } else if (Vars.state.wave >= 85 && Vars.state.wave <= 100) {
                    uug.setLevel(40);
                    Floor.FTools.classes.UnitUpGrade.getPower(uug, 40, true, false);
                } else if (Vars.state.wave >= 100 && Vars.state.wave <= 115) {
                    uug.setLevel(50);
                    Floor.FTools.classes.UnitUpGrade.getPower(uug, 50, true, false);
                } else if (Vars.state.wave >= 115 && Vars.state.wave <= 135) {
                    Floor.FTools.classes.UnitUpGrade.getPower(uug, 0, false, true);
                } else if (Vars.state.wave > 135) {
                    Floor.FTools.classes.UnitUpGrade.getPower(uug, 0, false, true);
                    uug.setLevel(60 + Math.min(Vars.state.wave - 135, 120));
                }
            }
        });

        Events.on(EventType.UnitBulletDestroyEvent.class, e -> {
            if (e.bullet.owner instanceof UnitUpGrade uug && (e.unit instanceof UnitUpGrade || e.unit.maxHealth() >= 1000)) {
                uug.addExp(e.unit.maxHealth * max(Vars.state.wave / 50, 1) *
                        (e.unit instanceof UnitUpGrade ku ? Math.max(1, ku.getLevel() / 30) : 1));
                int n = uug.number();
                int min = min(60 - uug.baseLevel(), n);
                Floor.FTools.classes.UnitUpGrade.getPower(uug, min, true, false);
            } else if (e.bullet.owner instanceof Unit u && u.controller() instanceof MissileAI ai && ai.shooter instanceof UnitUpGrade uug) {
                uug.addExp(e.unit.maxHealth * max(Vars.state.wave / 50, 1) *
                        (e.unit instanceof UnitUpGrade ku ? Math.max(1, ku.getLevel() / 30) : 1));
                int n = uug.number();
                int min = min(60 - uug.baseLevel(), n);
                Floor.FTools.classes.UnitUpGrade.getPower(uug, min, true, false);
            }
            if (e.bullet.owner instanceof Unit u) {
                if (u.controller() instanceof MissileAI ai && ai.shooter instanceof UpGradeTime ugt) {
                    ugt.add(1);
                }
            }
        });
        Events.on(FEvents.UnitDestroyOtherEvent.class, e -> {
            if (e.other instanceof UnitUpGrade || e.other.maxHealth() >= 1000) {
                if (e.killer instanceof UnitUpGrade uug) {
                    uug.addExp(e.other.maxHealth() * max(Vars.state.wave / 50, 1) *
                            (e.other instanceof UnitUpGrade ku ? Math.max(1, ku.getLevel() / 30) : 1));
                    int n = uug.number();
                    int min = min(60 - uug.baseLevel(), n);
                    Floor.FTools.classes.UnitUpGrade.getPower(uug, min, true, false);
                } else if (e.killer.controller() instanceof MissileAI ai && ai.shooter instanceof UnitUpGrade uug) {
                    uug.addExp(e.other.maxHealth() * max(Vars.state.wave / 50, 1) *
                            (e.other instanceof UnitUpGrade ku ? Math.max(1, ku.getLevel() / 30) : 1));
                    int n = uug.number();
                    int min = min(60 - uug.baseLevel(), n);
                    Floor.FTools.classes.UnitUpGrade.getPower(uug, min, true, false);
                }
                if (e.killer.controller() instanceof MissileAI ai && ai.shooter instanceof UpGradeTime ugt) {
                    ugt.add(1);
                }
            }
        });

        Events.on(EventType.UnitBulletDestroyEvent.class, e -> {
            if (e.bullet.owner instanceof BuildUpGrade fug) {
                if (e.unit instanceof UnitUpGrade uug) {
                    fug.addExp(e.unit.maxHealth * Math.max(1, uug.getLevel() / 15));
                } else {
                    fug.addExp(e.unit.maxHealth);
                }
            }
        });
        Events.on(EventType.BuildingBulletDestroyEvent.class, e -> {
            if (e.bullet.owner instanceof BuildUpGrade fug) {
                fug.addExp(e.build.maxHealth);
            }
        });
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