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
import mindustry.input.Binding;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.Tile;

import static Commonplace.Type.Dialogs.ProjectDialog.project;
import static arc.Core.input;
import static mindustry.Vars.player;
import static mindustry.Vars.state;

public class UnitProjectBlock extends Block {
    private final static Seq<Team> builds = new Seq<>();
    public float reload = 1800;
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
        addBar("reload", (UnitProjectBuild build) -> new Bar(Core.bundle.get("bar.hot"), Pal.powerLight, () -> build.timer / reload));
    }

    public class UnitProjectBuild extends Building {
        IntIntMap map = new IntIntMap();
        IntIntMap boostMap = new IntIntMap();
        boolean before = false;
        float pointTimer = 0;
        float laserTimer = 0;
        float timer = 0;
        int lastId = -1;

        @Override
        public void updateTile() {
            pointTimer += Time.delta;
            laserTimer += Time.delta;
            timer = Math.max(0, timer - Time.delta);

            if (player.unit() == null || player.unit().dead || player.unit().health <= 0) {
                lastId = -1;
            } else if (player.unit().team == team) {
                if (before) {
                    project.setMap(map, boostMap);
                    project.applyProject(player.unit());
                    before = false;
                    applyEffect.at(player.unit());
                    lastId = player.unit().id;
                } else if (!player.unit().spawnedByCore || input.keyTap(Binding.respawn)) {
                    lastId = -1;
                } else if (lastId == -1) {
                    project.setMap(map, boostMap);
                    project.applyProject(player.unit());
                    player.unit().apply(StatusEffects2.StrongStop, 180);
                    applyEffect.at(player.unit().x, player.unit().y, 0, player.unit());
                    lastId = player.unit().id;
                }

                if (lastId > 0) {
                    Unit u = player.unit();
                    if (pointTimer >= pointReload) {
                        pointTimer = 0;
                        pointEffect.at(u.x, u.y, u.rotation, u);
                    }
                    if (laserTimer >= laserReload) {
                        laserTimer = 0;
                        laserEffect.at(u.x, u.y, u.rotation, this);
                    }
                }
            } else {
                if (!builds.contains(player.unit().team)) {
                    project.clearProject(player.unit());
                }
                lastId = -1;
            }
        }

        @Override
        public void buildConfiguration(Table table) {
            if (state.isEditor() || timer <= 0) {
                table.row();
                table.table(t -> t.button(Icon.units, () -> {
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
                }));
            } else {
                table.clear();
            }
        }

        @Override
        public void add() {
            super.add();
            map.clear();
            boostMap.clear();
            builds.add(this.team);
            lastId = -1;
        }

        @Override
        public void remove() {
            super.remove();
            builds.remove(this.team);
            if (state.isGame() && lastId > 0) {
                project.clearProject(player.unit());
            }
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(lastId);
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
            lastId = read.i();
            timer = read.f();
            if (lastId > 0) {
                before = true;
            }
            int num = read.i();
            for (; num > 0; num--) {
                map.put(read.i(), read.i());
            }
            num = read.i();
            for (; num > 0; num--) {
                boostMap.put(read.i(), read.i());
            }
            builds.add(this.team);
        }
    }
}
