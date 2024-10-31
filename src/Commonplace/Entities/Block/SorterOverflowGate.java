package Commonplace.Entities.Block;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.type.Item;
import mindustry.world.blocks.distribution.Sorter;

import static mindustry.Vars.*;
import static mindustry.Vars.tilesize;

public class SorterOverflowGate extends Sorter {
    public SorterOverflowGate(String name) {
        super(name);

        config(Item.class, (SorterOverflowGateBuild tile, Item item) -> tile.sortItem = item);
        configClear((SorterOverflowGateBuild tile) -> tile.sortItem = null);
    }

    public class SorterOverflowGateBuild extends SorterBuild {

        @Override
        public void configured(Unit player, Object value) {
            super.configured(player, value);

            if (!headless) {
                renderer.minimap.update(tile);
            }
        }

        @Override
        public void draw() {

            if (sortItem == null) {
                Draw.rect(cross, x, y);
            } else {
                Draw.color(sortItem.color);
                Fill.square(x, y, tilesize / 2f - 0.00001f);
                Draw.color();
            }

            super.draw();
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            Building to = getTileTarget(item, source, false);

            return to != null && to.acceptItem(this, item) && to.team == team;
        }

        @Override
        public void handleItem(Building source, Item item) {
            getTileTarget(item, source, true).handleItem(this, item);
        }

        public Building getTileTarget(Item item, Building src, boolean flip) {
            int from = relativeToEdge(src.tile);
            if (from == -1) return null;
            Building to = nearby((from + 2) % 4);

            boolean fromInst = src.block.instantTransfer;
            boolean canForward = to != null && to.team == team && !(fromInst && to.block.instantTransfer) && to.acceptItem(this, item);

            if (item != sortItem) {
                return canForward ? to : null;
            }

            boolean inv = invert == enabled;

            if (!canForward || inv) {
                Building a = nearby(Mathf.mod(from - 1, 4));
                Building b = nearby(Mathf.mod(from + 1, 4));
                boolean ac = a != null && !(fromInst && a.block.instantTransfer) && a.team == team && a.acceptItem(this, item);
                boolean bc = b != null && !(fromInst && b.block.instantTransfer) && b.team == team && b.acceptItem(this, item);

                if (!ac && !bc) {
                    return canForward ? to : null;
                }

                if (ac && !bc) {
                    to = a;
                } else if (!ac) {
                    to = b;
                } else {
                    to = (rotation & (1 << from)) == 0 ? a : b;
                    if (flip) rotation ^= (1 << from);
                }
            }

            return to;
        }
    }
}
