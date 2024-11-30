package Commonplace.Loader.ProjectContent;

import mindustry.content.Fx;
import mindustry.entities.bullet.BulletType;

public class Bullets {
    public static final BulletType none = new BulletType(0,0){{
        lifetime = 0;
        collides = reflectable = hittable = absorbable = false;
        shootEffect = despawnEffect = hitEffect = Fx.none;
    }};
    public static void load() {
    }
}
