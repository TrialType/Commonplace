package Commonplace.Utils.Classes;

import Commonplace.Entities.Unit.TileMiner;
import arc.math.Mathf;
import arc.struct.IntSeq;
import arc.struct.ObjectIntMap;
import arc.struct.Seq;
import mindustry.type.Item;
import mindustry.world.Tile;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static mindustry.Vars.*;

public abstract class Located {
    public static final Map<Tile, TileMiner> miners = new HashMap<>();
    public static IntSeq[][][] ores;
    public static ObjectIntMap<Item> allOres;
    public static final int quadrantSize = 20;
    public static int quadWidth, quadHeight;
    private static int index = -1;

    public static boolean couldMine(TileMiner unit, Tile tile) {
        update();
        return !miners.containsKey(tile) || miners.get(tile) == unit;
    }

    public static void update() {
        Seq<Tile> remove = new Seq<>();
        for (Tile tile : miners.keySet()) {
            TileMiner miner = miners.get(tile);
            if (miner == null || miner.dead || miner.health() <= 0 || miner.mineTile != tile) {
                remove.add(tile);
            }
        }
        for (Tile t : remove) {
            miners.remove(t);
        }
    }

    public static Tile findOre(TileMiner miner, Item item) {
        init();
        if (ores[item.id] != null) {
            float minDst = 0f;
            Tile closest = null;
            for (int qx = 0; qx < quadWidth; qx++) {
                for (int qy = 0; qy < quadHeight; qy++) {
                    var arr = ores[item.id][qx][qy];
                    if (arr != null && arr.size > 0) {
                        for (int i = 0; i < arr.size; i++) {
                            Tile tile = world.tile(arr.get(i));
                            if (!tile.solid() && couldMine(miner, tile)) {
                                float dst = Mathf.dst2(miner.x, miner.y, tile.worldx(), tile.worldy());
                                if (closest == null || dst < minDst) {
                                    closest = tile;
                                    minDst = dst;
                                }
                            }
                        }
                    }
                }
            }
            if (closest == null) {
                int idx = miner.type.mineItems.indexOf(item);
                if (index < 0) {
                    index = idx;
                }
                if (idx == index - 1 || idx == miner.type.mineItems.size - 1 && index == 0) {
                    index = -1;
                    return closest;
                }
                if (idx == miner.type.mineItems.size - 1) {
                    closest = findOre(miner, miner.type.mineItems.get(0));
                } else {
                    closest = findOre(miner, miner.type.mineItems.get(idx + 1));
                }
            }
            return closest;
        }
        return null;
    }

    public static void removeOre(Tile tile) {
        init();
        Item item = tile.drop();
        if (ores[item.id] != null) {
            int qx = (tile.x / Located.quadrantSize);
            int qy = (tile.y / Located.quadrantSize);
            ores[item.id][qx][qy].removeValue(tile.pos());
            allOres.increment(item, -1);
        }
    }

    @SuppressWarnings("unchecked")
    public static void init() {
        Field field1;
        Field field2;
        Field field3;
        Field field4;
        try {
            field1 = indexer.getClass().getDeclaredField("ores");
            field2 = indexer.getClass().getDeclaredField("allOres");
            field3 = indexer.getClass().getDeclaredField("quadWidth");
            field4 = indexer.getClass().getDeclaredField("quadHeight");
            field1.setAccessible(true);
            field2.setAccessible(true);
            field3.setAccessible(true);
            field4.setAccessible(true);
            ores = (IntSeq[][][]) field1.get(indexer);
            allOres = (ObjectIntMap<Item>) field2.get(indexer);
            quadWidth = (int) field3.get(indexer);
            quadHeight = (int) field4.get(indexer);
        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            throw new RuntimeException(e);
        }
    }
}
