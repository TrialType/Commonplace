package Commonplace.Tools.Classes;

import arc.struct.Seq;
import mindustry.world.Tile;

import static mindustry.Vars.world;

public abstract class PathFinder {
    static final Seq<Entry> path = new Seq<>();
    static final Seq<Tile> steps = new Seq<>();
    static int cost = 0;

    public static Tile getPath(Tile from, Tile to, int passType) {
        path.clear();
        steps.clear();
        addTile(from, to, passType);

        while (!path.isEmpty()) {
            path.sort(Entry::allCost);
            Entry e = path.first();
            path.remove(0);
            cost = e.passCost;
            addTile(e.tile, to, passType);

            if (e.tile == to) {
                break;
            }
        }

        steps.remove(from);
        if (!steps.isEmpty()) {
            steps.sort(t -> t.dst(from));
        }

        return steps.isEmpty() ? from : steps.first();
    }

    public static void addTile(Tile t, Tile to, int passType) {
        steps.add(t);
        Tile[] tiles = new Tile[]{
                world.tile(t.x + 1, t.y),
                world.tile(t.x - 1, t.y),
                world.tile(t.x, t.y + 1),
                world.tile(t.x, t.y - 1)
        };

        for (Tile tile : tiles) {
            if (!steps.contains(tile) && tile != null && canPass(tile, passType)) {
                Entry e;
                if((e = path.find(en -> en.tile == tile)) != null){
                    if (e.passCost > cost + 2) {
                        path.find(en -> en.tile == tile).passCost = cost + 2;
                    }
                } else {
                    path.add(new Entry(tile, cost + 2, findValue(tile, to)));
                }
            }
        }
    }

    public static int findValue(Tile from, Tile to) {
        return (int) (Math.abs(from.getX() - to.getX()) + Math.abs(from.getY() - to.getY())) * 2;
    }

    public static boolean canPass(Tile at, int passType) {
        if (passType == 0) {
            return !at.block().isStatic() && !at.solid();
        } else if (passType == 1) {
            return true;
        } else if (passType == 2) {
            return !at.block().isStatic();
        }
        return false;
    }

    static class Entry {
        Tile tile;
        int passCost;
        int lastCost;

        public Entry(Tile t, int passCost, int lastCost) {
            this.tile = t;
            this.passCost = passCost;
            this.lastCost = lastCost;
        }

        public int allCost() {
            return passCost + lastCost;
        }
    }
}
