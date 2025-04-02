package Commonplace.Loader.DefaultContent;

import Commonplace.Loader.Special.Effects;
import Commonplace.Entities.Weather.StormDamageWeather;
import arc.graphics.Color;
import arc.util.Time;
import mindustry.gen.Sounds;
import mindustry.type.Weather;
import mindustry.world.meta.Attribute;

public class Weathers2 {
    public static Weather rockStorm, rockStormLarge;

    public static void load() {
        rockStorm = new StormDamageWeather("rock-storm") {{
            color = noiseColor = Color.valueOf("f7cba4");
            particleRegion = "particle";
            drawNoise = true;
            useWindVector = true;
            sizeMax = 140f;
            sizeMin = 70f;
            minAlpha = 0f;
            maxAlpha = 0.2f;
            density = 1500f;
            baseSpeed = 5.4f;
            attrs.set(Attribute.light, -0.1f);
            attrs.set(Attribute.water, -0.1f);
            opacityMultiplier = 0.35f;
            force = 0.1f;
            sound = Sounds.wind;
            soundVol = 0.8f;
            duration = 7f * Time.toMinutes;
        }};
        rockStormLarge = new StormDamageWeather("rock-storm-large") {{
            color = noiseColor = Color.valueOf("f7cba4");
            particleRegion = "particle";
            drawNoise = true;
            useWindVector = true;
            sizeMax = 140f;
            sizeMin = 70f;
            minAlpha = 0f;
            maxAlpha = 0.2f;
            density = 1500f;
            baseSpeed = 5.4f;
            attrs.set(Attribute.light, -0.1f);
            attrs.set(Attribute.water, -0.1f);
            opacityMultiplier = 0.35f;
            force = 0.1f;
            sound = Sounds.wind;
            soundVol = 0.8f;
            duration = 7f * Time.toMinutes;

            damage = 500;
            chance = 1f / 30;
            intensity = 0.7f;
            range = 340;
            shake = 300;
            buildingDamageMultiplier = 0.5f;
            rock = Effects.rockDownLarge;
            wave = Effects.rockDownWaveLarge;
        }};
    }
}
