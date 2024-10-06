package Commonplace.Entities.FBlock;

import arc.func.Cons;
import arc.struct.IntIntMap;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.entities.bullet.BulletType;
import mindustry.game.Team;
import mindustry.type.Item;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.turrets.Turret;
import mindustry.world.meta.BlockGroup;

import static mindustry.Vars.world;

public class DrillTurret extends Turret {
    public BulletType baseType;
    public ObjectMap<Item, Cons<BulletType>> applier = new ObjectMap<>();

    public DrillTurret(String name) {
        super(name);

        group = BlockGroup.drills;
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
    public void load() {
        super.load();
        baseType.load();
    }

    public class DrillTurretBuild extends TurretBuild {
        BulletType type;
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

        public void inspect() {
            IntIntMap map = new IntIntMap();
            readItems(map);
            if (!map.equals(items)) {
                items = map;
                readBullet(items);
            }
        }

        public void reset() {
            readTiles(tiles);
            readItems(items);
            readBullet(items);
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
                Cons<BulletType> c = applier.get(Vars.content.item(id));
                int num = items.get(id);
                for (int i = 0; i < num; i++) {
                    c.get(type);
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
