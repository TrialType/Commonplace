package Commonplace.Entities.Block;

import Commonplace.Utils.Classes.Stats;
import arc.Core;
import arc.func.Boolf;
import arc.func.Cons;
import arc.math.Angles;
import arc.math.Mathf;
import arc.scene.ui.Image;
import arc.struct.IntIntMap;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.entities.Units;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.pattern.ShootPattern;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.ui.Styles;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.turrets.Turret;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatValues;

import static mindustry.Vars.world;

public class DrillTurret extends Turret {
    public BulletType baseType;
    public ObjectMap<Item, Cons<BulletType>> applier = new ObjectMap<>();
    public ObjectMap<Item, Cons<ShootPattern>> shootApplier = new ObjectMap<>();
    public ObjectMap<Boolf<Building>, Cons<BulletType>> specialApplier = new ObjectMap<>();
    public ObjectMap<Boolf<Building>, Cons<ShootPattern>> specialShootApplier = new ObjectMap<>();

    public DrillTurret(String name) {
        super(name);
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation) {
        boolean check = false;
        int tx = tile.x, ty = tile.y;
        Tile t;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                t = world.tile(tx + x + sizeOffset, ty + y + sizeOffset);
                if (t != null && t.overlay().itemDrop != null && applier.containsKey(t.overlay().itemDrop)) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.remove(Stat.targetsAir);
        stats.remove(Stat.targetsGround);
        Seq<Item> showed = new Seq<>();
        stats.add(Stat.ammo, StatValues.ammo(ObjectMap.of(this, baseType)));
        stats.add(Stats.gain, t -> {
            showed.clear();
            t.row();
            t.table(Styles.grayPanel, tex -> {
                for (var e : applier) {
                    if (!showed.contains(e.key)) {
                        showed.add(e.key);
                        tex.row();
                        tex.row();
                        tex.add(new Image(e.key.uiIcon)).left();
                        tex.add(Core.bundle.get(name + "-gain." + e.key.name));
                    }
                }
                for (var e : shootApplier) {
                    if (!showed.contains(e.key)) {
                        showed.add(e.key);
                        tex.row();
                        tex.row();
                        tex.add(new Image(e.key.uiIcon)).left();
                        tex.add(Core.bundle.get(name + "-gain." + e.key.name));
                    }
                }
            }).growX();
            if (!(specialApplier.isEmpty() && specialShootApplier.isEmpty())) {
                t.row();
                t.row();
                t.table(Styles.grayPanel, tex -> tex.add(Core.bundle.get(name + "-special-gain")).growX()).growX();
            }
        });
    }

    @Override
    public void load() {
        super.load();
        baseType.load();
    }

    public class DrillTurretBuild extends TurretBuild {
        BulletType type;
        ShootPattern shootType;
        Seq<Tile> tiles = new Seq<>();
        IntIntMap items = new IntIntMap();

        @Override
        public void updateTile() {
            inspect();
            super.updateTile();
        }

        @Override
        public BulletType peekAmmo() {
            return type;
        }

        @Override
        public BulletType useAmmo() {
            return type;
        }

        public boolean hasAmmo() {
            return true;
        }

        @Override
        protected void updateReload() {
            float num = 0;
            for (int n : items.values().toArray().toArray()) {
                num += n;
            }
            float multiplier = hasAmmo() ? peekAmmo().reloadMultiplier : 1f;
            multiplier *= num / size / size;

            reloadCounter += delta() * multiplier * baseReloadSpeed();

            //cap reload for visual reasons
            reloadCounter = Math.min(reloadCounter, reload);
        }

        @Override
        protected void shoot(BulletType type) {
            float
                    bulletX = x + Angles.trnsx(rotation - 90, shootX, shootY),
                    bulletY = y + Angles.trnsy(rotation - 90, shootX, shootY);

            if (shootType.firstShotDelay > 0) {
                chargeSound.at(bulletX, bulletY, Mathf.random(soundPitchMin, soundPitchMax));
                type.chargeEffect.at(bulletX, bulletY, rotation);
            }

            shootType.shoot(barrelCounter, (xOffset, yOffset, angle, delay, mover) -> {
                queuedBullets++;
                if (delay > 0f) {
                    Time.run(delay, () -> bullet(type, xOffset, yOffset, angle, mover));
                } else {
                    bullet(type, xOffset, yOffset, angle, mover);
                }
            }, () -> barrelCounter++);

            if (consumeAmmoOnce) {
                useAmmo();
            }
        }

        @Override
        protected void findTarget() {
            float range = range();
            boolean targetAir = peekAmmo().collidesAir;
            boolean targetGround = peekAmmo().collidesGround || peekAmmo().collidesTiles;

            if (targetAir && !targetGround) {
                target = Units.bestEnemy(team, x, y, range, e -> !e.dead() && !e.isGrounded() && unitFilter.get(e), unitSort);
            } else {
                target = Units.bestTarget(team, x, y, range, e -> !e.dead() && unitFilter.get(e) && (e.isGrounded() || targetAir) && (!e.isGrounded() || targetGround), b -> targetGround && buildingFilter.get(b), unitSort);
            }

            if (target == null && canHeal()) {
                target = Units.findAllyTile(team, x, y, range, b -> b.damaged() && b != this);
            }
        }

        public void inspect() {
            IntIntMap map = new IntIntMap();
            readItems(map);
            if (!map.equals(items)) {
                items = map;
                readBullet(items);
                readShoot(items);
                readSpecial();
            }
        }

        public void reset() {
            readTiles(tiles);
            readItems(items);
            readBullet(items);
            readShoot(items);
            readSpecial();
        }

        public void readTiles(Seq<Tile> tiles) {
            tiles.clear();
            tile.getLinkedTiles(tiles);
        }

        public void readItems(IntIntMap items) {
            items.clear();

            Item item;
            for (Tile tile : tiles) {
                if ((item = tile.overlay().itemDrop) != null && applier.containsKey(item)) {
                    if (items.containsKey(item.id)) {
                        items.put(item.id, items.get(item.id) + 1);
                    } else {
                        items.put(item.id, 1);
                    }
                }
            }
        }

        public void readBullet(IntIntMap items) {
            type = baseType.copy();
            for (int id : items.keys().toArray().toArray()) {
                Cons<BulletType> c = applier.get(Vars.content.item(id), b -> {
                });
                int num = items.get(id);
                for (int i = 0; i < num; i++) {
                    c.get(type);
                }
            }
        }

        public void readShoot(IntIntMap items) {
            shootType = shoot.copy();
            for (int id : items.keys().toArray().toArray()) {
                Cons<ShootPattern> c = shootApplier.get(Vars.content.item(id), s -> {
                });
                int num = items.get(id);
                for (int i = 0; i < num; i++) {
                    c.get(shootType);
                }
            }
        }

        public void readSpecial() {
            for (var bool : specialApplier.keys()) {
                if (bool.get(this)) {
                    specialApplier.get(bool).get(type);
                }
            }

            for (var bool : specialShootApplier.keys()) {
                if (bool.get(this)) {
                    specialShootApplier.get(bool).get(shootType);
                }
            }
        }

        @Override
        public void add() {
            super.add();
            reset();
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(tiles.size);
            for (Tile tile : tiles) {
                write.i(tile.pos());
            }
        }

        @Override
        public void read(Reads read, byte version) {
            super.read(read, version);
            int num = read.i();
            for (int i = 0; i < num; i++) {
                tiles.add(world.tile(read.i()));
            }
            reset();
        }
    }
}
