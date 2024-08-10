package Commonplace.FContent.ProjectContent;

import mindustry.content.Fx;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.type.Weapon;

import static Commonplace.FTools.classes.ProjectUtils.init;

public class Weapons {
    public static Weapon test = new Weapon() {{
        top = false;
        reload = 20f;
        x = 3f;
        y = 0.5f;
        rotate = true;
        ejectEffect = Fx.casing1;
        bullet = new BasicBulletType(3f, 11){{
            width = 7f;
            height = 9f;
            lifetime = 60f;
            shootEffect = Fx.shootSmall;
            smokeEffect = Fx.shootSmallSmoke;
            fragBullets = 0;
        }};
    }};

    public static void load() {
        init(test);
    }
}
