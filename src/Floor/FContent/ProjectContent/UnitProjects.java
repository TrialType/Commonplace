package Floor.FContent.ProjectContent;

import Floor.FTools.Lists.BoostProject;
import Floor.FTools.Lists.UnitProject;
import mindustry.entities.bullet.BulletType;

public class UnitProjects {
    public static UnitProject handTurret, fragsAdd;

    public static void load() {
        handTurret = new UnitProject(1, "hand-turret",
                 u -> UnitProject.weaponApply(PWeapons.test, u));
        fragsAdd = new BoostProject(0, "frag-adder") {{
            frags = 1;
            applyW = w -> {
                BulletType b = w.bullet.copy();
                if (b.fragBullet == null) {
                    b.fragBullet = defFrag;
                }
                b.fragBullets += frags;
                w.bullet = b;
            };
        }};
    }
}
