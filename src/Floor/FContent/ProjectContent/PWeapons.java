package Floor.FContent.ProjectContent;

import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.type.Weapon;

public class PWeapons {
    public static Weapon test = new Weapon() {{
        reload = 15;
        bullet = new BasicBulletType() {{
            width = height = 12;
            damage = 300;
            lifetime = 30;
            speed = 7;
            fragBullets = 1;
            fragBullet = new BasicBulletType() {{
                width = height = 30;
                damage = 10;
                speed = 3;
                lifetime = 90;
            }};
        }};
    }};

    public static void load() {
        init(test);
    }

    public static void init(Weapon w) {
        w.init();
        w.load();
        init(w.bullet);
    }

    public static void init(BulletType type) {
        type.init();
        type.load();
        if (type.fragBullet != null) {
            init(type.fragBullet);
        }
        if (type.intervalBullet != null) {
            init(type.intervalBullet);
        }
    }
}
