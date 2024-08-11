package Commonplace.Content.ProjectContent;

import Commonplace.Tools.Classes.ProjectUtils;
import Commonplace.FType.New.BoostProject;
import Commonplace.FType.New.UnitProject;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;

public class UnitProjects {
    public static UnitProject handTurret;
    public static UnitProject fragsAdd, damageAdder, fragDamageAdder;

    public static void load() {
        handTurret = new UnitProject(1, 2, "hand-turret", u -> UnitProject.weaponApply(Weapons.test, u));
//====================================================================================================================
//====================================================================================================================
//====================================================================================================================
        fragsAdd = new BoostProject(2, 3, "frag-adder") {{
            valve = addPart;
            bullet = new BasicBulletType() {{
                width = height = 5;
                damage = 0;
                speed = 3;
                lifetime = 45;
            }};
            init = () -> ProjectUtils.load(bullet);
            applyW = w -> {
                if (w.bullet.fragBullet == null) {
                    w.bullet.fragBullet = bullet;
                } else {
                    w.bullet.fragBullets += 1;
                }
            };
        }};

        damageAdder = new BoostProject(1, 3, "damage-adder") {{
            int damage = 6;
            applyW = w -> {
                w.bullet.damage += damage;
                w.bullet.splashDamage += damage;
                w.bullet.lightningDamage += damage;
            };
        }};

        fragDamageAdder = new BoostProject(1, 2, "frag-damage-adder") {{
            int damage = 6;
            applyW = w -> {
                if (w.bullet.fragBullet != null) {
                    BulletType b2 = w.bullet.fragBullet.copy();
                    b2.damage += damage;
                    b2.splashDamage += damage;
                    b2.lightningDamage += damage;
                    w.bullet.fragBullet = b2;
                }
            };
        }};
    }
}
