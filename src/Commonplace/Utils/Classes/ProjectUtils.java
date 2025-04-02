package Commonplace.Utils.Classes;

import mindustry.entities.bullet.BulletType;
import mindustry.type.Weapon;

public abstract class ProjectUtils {
    public static void init(Weapon w) {
        w.init();
        w.load();
        load(w.bullet);
    }

    public static void load(BulletType type) {
        type.load();
        if (type.fragBullet != null) {
            load(type.fragBullet);
        }
        if (type.intervalBullet != null) {
            load(type.intervalBullet);
        }
    }

    public static void init(BulletType type) {
        type.init();
        if (type.fragBullet != null) {
            init(type.fragBullet);
        }
        if (type.intervalBullet != null) {
            init(type.intervalBullet);
        }
    }
}
