package Commonplace.Entities.Weather;

import Commonplace.Loader.Special.Effects;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;
import mindustry.gen.WeatherState;
import mindustry.type.weather.ParticleWeather;

import static arc.util.Time.delta;
import static mindustry.Vars.tilesize;
import static mindustry.Vars.world;

public class StormDamageWeather extends ParticleWeather {
    private Bullet building;

    public int time = 1;
    public float damage = 120;
    public float chance = 1f / 20;
    public float intensity = 0.3f;
    public float range = 120;
    public float shake = 120;
    public float buildingDamageMultiplier = 0.1f;
    public Effect rock = Effects.rockDown;
    public Effect wave = Effects.rockDownWave;
    public TextureRegion damageRegion = null;

    public StormDamageWeather(String name) {
        super(name);
    }

    @Override
    public void load() {
        super.load();

        building = Bullet.create();
        building.team = null;
        building.owner = null;
        building.type = new BulletType() {{
            buildingDamageMultiplier = StormDamageWeather.this.buildingDamageMultiplier;
        }};
    }

    @Override
    public void update(WeatherState state) {
        super.update(state);
        if (delta > 0 && state.intensity > intensity) {
            int num = (int) (time * state.intensity / intensity) * (int) (delta < 1 ? 1 : delta);
            float x, y, angle;
            if (useWindVector) {
                float speed = baseSpeed * state.intensity;
                x = state.windVector.x * speed;
                y = state.windVector.y * speed;
            } else {
                x = this.xspeed;
                y = this.yspeed;
            }
            angle = (float) Math.atan(y / x) * Mathf.radiansToDegrees;
            for (int i = 0; i < num; i++) {
                if (Mathf.chance(chance)) {
                    x = Mathf.random(world.width() * tilesize);
                    y = Mathf.random(world.height() * tilesize);
                    rock.at(x, y, angle, damageRegion);
                    float finalX = x;
                    float finalY = y;
                    Time.run(rock.lifetime, () -> {
                        Damage.damage(null, finalX, finalY, range, damage,
                                false, false, true, false, building);
                        this.wave.at(finalX, finalY);
                        Effect.shake(shake, wave.lifetime, finalX, finalY);
                    });
                }
            }
        }
    }
}
