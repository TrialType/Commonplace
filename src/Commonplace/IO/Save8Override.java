package Commonplace.IO;

import arc.Core;
import arc.math.geom.Vec2;
import arc.struct.IntSeq;
import arc.struct.StringMap;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.content.Planets;
import mindustry.content.TechTree;
import mindustry.core.Version;
import mindustry.ctype.ContentType;
import mindustry.game.GameStats;
import mindustry.game.Rules;
import mindustry.game.Team;
import mindustry.io.JsonIO;
import mindustry.io.SaveVersion;
import mindustry.maps.Map;
import mindustry.type.MapLocales;
import mindustry.world.WorldContext;
import mindustry.world.meta.Env;

import java.io.DataInput;
import java.io.IOException;

import static mindustry.Vars.*;
import static mindustry.Vars.state;

public class Save8Override extends SaveVersion {
    public Save8Override() {
        super(8);
    }

    @Override
    public void readMeta(DataInput stream, WorldContext context) throws IOException {
        StringMap map = readStringMap(stream);

        state.wave = map.getInt("wave");
        state.wavetime = map.getFloat("wavetime", state.rules.waveSpacing);
        state.tick = map.getFloat("tick");
        state.stats = JsonIO.read(AllGameStats.class, map.get("stats", "{}"));
        state.rules = JsonIO.read(Rules.class, map.get("rules", "{}"));
        state.mapLocales = JsonIO.read(MapLocales.class, map.get("locales", "{}"));
        if (state.rules.spawns.isEmpty()) state.rules.spawns = waves.get();
        lastReadBuild = map.getInt("build", -1);

        if (context.getSector() != null) {
            state.rules.sector = context.getSector();
            if (state.rules.sector != null) {
                state.rules.sector.planet.applyRules(state.rules);
            }
        }

        if (state.rules.planet == Planets.serpulo && state.rules.hasEnv(Env.scorching)) {
            state.rules.planet = Planets.erekir;
        }

        if (!headless) {
            Tmp.v1.tryFromString(map.get("viewpos"));
            Core.camera.position.set(Tmp.v1);
            player.set(Tmp.v1);

            control.input.controlledType = content.getByName(ContentType.unit, map.get("controlledType", "<none>"));
            Team team = Team.get(map.getInt("playerteam", state.rules.defaultTeam.id));
            if (!net.client() && team != Team.derelict) {
                player.team(team);
            }

            var groups = JsonIO.read(IntSeq[].class, map.get("controlGroups", "null"));
            if (groups != null && groups.length == control.input.controlGroups.length) {
                control.input.controlGroups = groups;
            }
        }

        Map worldmap = maps.byName(map.get("mapname", "\\\\\\"));
        state.map = worldmap == null ? new Map(StringMap.of(
                "name", map.get("mapname", "Unknown"),
                "width", 1,
                "height", 1
        )) : worldmap;
    }
}
