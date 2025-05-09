package Commonplace.Loader.DefaultContent;

import Commonplace.Type.PlanetGenerators.ENGSWEISPlanetGenerator;
import arc.graphics.Color;
import mindustry.content.Planets;
import mindustry.game.Team;
import mindustry.graphics.Pal;
import mindustry.graphics.g3d.HexMesh;
import mindustry.graphics.g3d.HexSkyMesh;
import mindustry.graphics.g3d.MultiMesh;
import mindustry.type.Planet;


public class Planets2 {
    public static Planet ENGSWEIS, Adventure;

    public static void load() {
        Planets.sun.children.clear();
        Planets.sun.updateTotalRadius();

        ENGSWEIS = new Planet("engsweis", Planets.sun, 1, 3) {{
            generator = new ENGSWEISPlanetGenerator();
            meshLoader = () -> new HexMesh(this, 6);
            cloudMeshLoader = () -> new MultiMesh(
                    new HexSkyMesh(this, 11, 0.15f, 0.13f, 5, new Color().set(Pal.spore).mul(0.9f).a(0.75f), 2, 0.45f, 0.9f, 0.38f),
                    new HexSkyMesh(this, 1, 0.6f, 0.16f, 5, Color.white.cpy().lerp(Pal.spore, 0.55f).a(0.75f), 2, 0.45f, 1f, 0.41f)
            );

            launchCapacityMultiplier = 0.2f;
            sectorSeed = 15;
            allowWaves = true;
            allowWaveSimulation = true;
            allowSectorInvasion = true;
            enemyCoreSpawnReplace = true;

            tidalLock = true;
            rotateTime = orbitTime;

            prebuildBase = false;
            ruleSetter = r -> {
                r.waveTeam = Team.crux;
                r.placeRangeCheck = false;
                r.showSpawns = false;
            };

            iconColor = Color.valueOf("ed6542");
            landCloudColor = Color.valueOf("ed6542");
            atmosphereColor = Color.valueOf("f07218");

            atmosphereRadIn = 0.02f;
            atmosphereRadOut = 0.3f;

            startSector = 64;
            alwaysUnlocked = true;

            techTree = Planets.serpulo.techTree;
        }};

        Planets.sun.children.add(Planets.erekir);
        Planets.sun.children.add(Planets.serpulo);
        Planets.erekir.orbitRadius = (ENGSWEIS.orbitRadius + Planets.serpulo.orbitRadius) / 2;

        Adventure = new Planet("adventure", Planets.sun, 1, 3) {{
            generator = new ENGSWEISPlanetGenerator();
            meshLoader = () -> new HexMesh(this, 6);
            cloudMeshLoader = () -> new MultiMesh(
                    new HexSkyMesh(this, 11, 0.15f, 0.13f, 5, new Color().set(Pal.spore).mul(0.9f).a(0.75f), 2, 0.45f, 0.9f, 0.38f),
                    new HexSkyMesh(this, 1, 0.6f, 0.16f, 5, Color.white.cpy().lerp(Pal.spore, 0.55f).a(0.75f), 2, 0.45f, 1f, 0.41f)
            );

            alwaysUnlocked = false;
        }};
    }
}
