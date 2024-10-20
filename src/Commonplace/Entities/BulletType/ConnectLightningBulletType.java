package Commonplace.Entities.BulletType;

import Commonplace.Utils.Classes.Lightning2;
import arc.math.Mathf;
import mindustry.entities.bullet.LightningBulletType;
import mindustry.gen.Bullet;
import mindustry.gen.Healthc;

public class ConnectLightningBulletType extends LightningBulletType {
    public int hitAdd = 2;

    @Override
    public void init(Bullet b) {
        if (killShooter && b.owner() instanceof Healthc h && !h.dead()) {
            h.kill();
        }

        if (instantDisappear) {
            b.time = lifetime + 1f;
        }

        if (spawnBullets.size > 0) {
            for (var bullet : spawnBullets) {
                bullet.create(b, b.x, b.y, b.rotation());
            }
        }

        Lightning2.createConnect(b, lightningColor, damage, b.x, b.y, b.rotation(), lightningLength + Mathf.random(lightningLengthRand), hitAdd);
    }
}
