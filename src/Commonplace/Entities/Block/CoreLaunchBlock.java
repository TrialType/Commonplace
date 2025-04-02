package Commonplace.Entities.Block;

import arc.Core;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.blocks.storage.CoreBlock;

public class CoreLaunchBlock extends Block {
    public Effect startEffect = Fx.none;
    public float reload = 1800;

    public CoreLaunchBlock(String name) {
        super(name);

        itemCapacity = 300;
        hasItems = true;
        hasPower = true;
        connectedPower = true;
        update = true;
        solid = true;
        destructible = true;
        breakable = true;
        canOverdrive = true;
    }

    @Override
    public void setBars() {
        super.setBars();
        addBar("to", (CoreLaunchBuild build) -> new Bar(Core.bundle.get("bar.cold"), Pal.redLight, () -> build.time / reload));
        addBar("items", build -> new Bar(Core.bundle.format("bar.items", build.items.total()),
                Pal.redLight, () -> build.items.total() * 1f / itemCapacity));
    }

    public class CoreLaunchBuild extends Building {
        public float time = 0;

        @Override
        public void updateTile() {
            if (efficiency > 0) {
                time += Time.delta * timeScale * efficiency;
                CoreBlock.CoreBuild core = team.core();
                if (time >= reload && this.items.total() > 0 && core != null) {
                    time = time % reload;
                    items.each((i, n) -> {
                        for (int j = 0; j < n; j++) {
                            core.offload(i);
                        }
                    });
                    items.clear();
                    startEffect.at(x, y, 0, core);
                }
            }
        }

        @Override
        public boolean acceptItem(Building building, Item item) {
            return this.items.total() < itemCapacity;
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(time);
        }

        @Override
        public void read(Reads read, byte version) {
            super.read(read, version);
            time = read.f();
        }
    }
}
