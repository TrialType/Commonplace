package Commonplace.Entities.Block;

import Commonplace.Loader.DefaultContent.StatusEffects2;
import arc.Core;
import arc.scene.ui.layout.Table;
import arc.struct.IntIntMap;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.core.GameState;
import mindustry.entities.Effect;
import mindustry.entities.effect.WaveEffect;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.Tile;

import static Commonplace.UI.Dialogs.ProjectDialog.project;
import static mindustry.Vars.player;
import static mindustry.Vars.state;

public class UnitProjectBlock extends Block {
    private final static Seq<Team> builds = new Seq<>();
    public float reload = 1200;
    public float pointReload = 60;
    public float laserReload = 120;
    public Effect applyEffect = Fx.unitDespawn;
    public Effect laserEffect = Fx.none;
    public Effect pointEffect = new WaveEffect() {{
        lifetime = 45;
        sides = 4;
        colorFrom = Pal.redLight;
        lightColor = Pal.redLight;
        colorTo = Pal.redLight;
        sizeFrom = 10;
        sizeTo = 30;
        strokeFrom = 3;
        strokeTo = 0;
    }};

    public UnitProjectBlock(String name) {
        super(name);

        destructible = true;
        hasPower = false;
        hasItems = false;
        solid = true;
        update = true;
        configurable = true;
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation) {
        return !builds.contains(team);
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        super.drawPlace(x, y, rotation, valid);

        Unit u = player.unit();
        Drawf.light(u.x, u.y, u.type.region, u.rotation, Pal.lightOrange, 12);
    }

    @Override
    public void setBars() {
        super.setBars();
        addBar("reload", b -> new Bar(Core.bundle.get("bar.hot"), Pal.powerLight, () -> b instanceof UnitProjectBuild build ? build.timer / reload : 0.5f));
    }

    public class UnitProjectBuild extends Building {
        IntIntMap boostMap = new IntIntMap();
        IntIntMap map = new IntIntMap();
        Unit unit = null;
        float pointTimer = 0;
        float laserTimer = 0;
        float timer = 0;
        boolean applied = false;

        @Override
        public void updateTile() {
            pointTimer += Time.delta;
            laserTimer += Time.delta;
            timer = Math.max(0, timer - Time.delta);

            if (applied) {
                applied = false;
                unit = player.unit();
                project.setMap(map, boostMap);
                project.applyProject(player.unit());
            }

            if (player.unit() == null || !player.unit().isValid()) {
                unit = null;
            } else if (unit != null && unit.team != team) {
                if (!builds.contains(player.unit().team)) {
                    project.clearProject(player.unit());
                }
            } else if (player.unit().spawnedByCore) {
                if (unit == null || unit != player.unit()) {
                    unit = player.unit();
                    project.setMap(map, boostMap);
                    project.applyProject(player.unit());
                    player.unit().apply(StatusEffects2.superStop, 180);
                    applyEffect.at(player.unit().x, player.unit().y, 0, player.unit());
                }

                if (pointTimer >= pointReload) {
                    pointTimer = 0;
                    pointEffect.at(unit.x, unit.y, unit.rotation, unit);
                }
                if (laserTimer >= laserReload) {
                    laserTimer = 0;
                    laserEffect.at(unit.x, unit.y, unit.rotation, this);
                }
            }
        }

        @Override
        public void buildConfiguration(Table table) {
            table.row();
            table.clear();
            table.table(t -> t.button(Icon.units, () -> {
                if (state.isEditor() || timer <= 0) {
                    if (!project.isShown() && player.unit() != null && player.unit().team == this.team) {
                        project.setWrite(m -> this.map = m, m -> this.boostMap = m);
                        project.setMap(map, boostMap);
                        project.show();
                        table.clear();
                        if (!Vars.net.client()) {
                            state.set(GameState.State.paused);
                        }
                        timer = reload;
                    }
                }
            }));
        }

        @Override
        public void created() {
            super.created();
            map.clear();
            boostMap.clear();
            builds.add(this.team);
        }

        @Override
        public void onProximityRemoved() {
            super.onProximityRemoved();
            builds.remove(this.team);
            if (state.isGame() && unit != null) {
                project.clearProject(unit);
            }
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.bool(unit == player.unit());
            write.f(timer);
            write.i(map.size);
            map.forEach(e -> {
                write.i(e.key);
                write.i(e.value);
            });
            write.i(boostMap.size);
            boostMap.forEach(e -> {
                write.i(e.key);
                write.i(e.value);
            });
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read);
            applied = read.bool();
            timer = read.f();
            for (int num = read.i(); num > 0; num--) {
                map.put(read.i(), read.i());
            }
            for (int num = read.i(); num > 0; num--) {
                boostMap.put(read.i(), read.i());
            }
            builds.add(this.team);
        }
    }
}
