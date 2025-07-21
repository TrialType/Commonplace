package Commonplace.IO;

import arc.util.serialization.Json;
import arc.util.serialization.JsonValue;
import mindustry.game.GameStats;
import mindustry.game.Team;

public class AllGameStats extends GameStats {
    public LowGameStats[] all;

    public AllGameStats() {
        all = new LowGameStats[Team.all.length];
        all[Team.sharded.id] = new LowGameStats();
    }

    public AllGameStats(GameStats stats) {
        LowGameStats stat = new LowGameStats();

        wavesLasted = stats.wavesLasted;
        coreItemCount = stats.coreItemCount;
        buildingsBuilt = stats.buildingsBuilt;
        placedBlockCount = stats.placedBlockCount;
        buildingsDestroyed = stats.buildingsDestroyed;
        buildingsDeconstructed = stats.buildingsDeconstructed;
        unitsCreated = stats.unitsCreated;
        enemyUnitsDestroyed = stats.enemyUnitsDestroyed;

        all = new LowGameStats[Team.all.length];
        all[Team.sharded.id] = stat;
    }

    public LowGameStats get(Team team) {
        LowGameStats stats = all[team.id];
        if (stats == null) {
            stats = new LowGameStats();
            all[team.id] = stats;
        }
        return stats;
    }

    public static class LowGameStats implements Json.JsonSerializable {
        public float unitsCreatedHealth = 0;
        public float enemyUnitsDestroyedHealth = 0;

        @Override
        public void write(Json json) {
            json.writeValue("unitsCreatedHealth", unitsCreatedHealth, float.class);
            json.writeValue("enemyUnitsDestroyedHealth", enemyUnitsDestroyedHealth, float.class);
        }

        @Override
        public void read(Json json, JsonValue jsonData) {
            unitsCreatedHealth = jsonData.getFloat("unitsCreatedHealth",0);
            enemyUnitsDestroyedHealth = jsonData.getFloat("enemyUnitsDestroyedHealth",0);
        }
    }
}
