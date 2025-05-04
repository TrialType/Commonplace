package Commonplace.Entities.BulletType;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Bullet;
import mindustry.gen.Healthc;
import mindustry.gen.Shieldc;
import mindustry.gen.Statusc;

public class DamageOwnBulletType extends BasicBulletType {
    public float ownDamage = 0.8f;
    public boolean laser = true;
    public boolean keepDamage = true;
    public boolean damagePriceArmor = true;

    @Override
    public void init(Bullet b) {
        if (b.owner() instanceof Healthc h && !h.dead()) {
            if (killShooter) {
                h.kill();
            } else if (ownDamage > 0) {
                float damage = (keepDamage && b.owner instanceof Statusc s) ? s.healthMultiplier() * ownDamage : ownDamage;
                if (b.owner() instanceof Shieldc s && s.shield() > 0) {
                    float shield = s.shield();
                    s.shield(0);
                    if (damagePriceArmor) {
                        h.damagePierce(damage);
                    } else {
                        h.damage(damage);
                    }
                    s.shield(shield);
                } else {
                    if (damagePriceArmor) {
                        h.damagePierce(damage);
                    } else {
                        h.damage(damage);
                    }
                }
            }
        }

        if (instantDisappear) {
            b.time = lifetime + 1f;
        }

        if (spawnBullets.size > 0) {
            for (var bullet : spawnBullets) {
                bullet.create(b, b.x, b.y, b.rotation());
            }
        }
    }

    @Override
    public void draw(Bullet b) {
        super.draw(b);
        if (laser) {
            Draw.color(backColor);
            Lines.stroke(width);
            Lines.lineAngleCenter(b.x, b.y, b.rotation(), height);
            Draw.color(frontColor);
            Lines.lineAngleCenter(b.x, b.y, b.rotation(), height / 2f);
            Draw.reset();
        }
    }
}
