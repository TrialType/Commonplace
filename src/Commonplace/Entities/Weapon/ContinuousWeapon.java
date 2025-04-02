package Commonplace.Entities.Weapon;

import mindustry.entities.units.WeaponMount;
import mindustry.gen.Unit;
import mindustry.type.Weapon;

public class ContinuousWeapon extends Weapon {
    public void update(Unit unit, WeaponMount mount) {
        super.update(unit, mount);

        if (continuous && mount.bullet != null) {
            mount.bullet.aimX = mount.aimX;
            mount.bullet.aimY = mount.aimY;
        }
    }
}
