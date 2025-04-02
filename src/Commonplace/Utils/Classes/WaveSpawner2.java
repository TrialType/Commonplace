package Commonplace.Utils.Classes;

import mindustry.ai.WaveSpawner;

import static mindustry.Vars.state;

public class WaveSpawner2 extends WaveSpawner {
    @Override
    public void spawnEnemies() {
        state.rules.spawns.sort(s -> s.unitAmount + s.items.item.id * s.items.amount + s.effect.id * 9876 + s.type.id * 12345);
    }
}
