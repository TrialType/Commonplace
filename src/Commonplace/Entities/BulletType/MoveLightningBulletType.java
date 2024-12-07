package Commonplace.Entities.BulletType;

import Commonplace.Loader.Special.Effects;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import mindustry.content.Fx;
import mindustry.entities.Damage;
import mindustry.entities.bullet.LightningBulletType;
import mindustry.gen.Bullet;
import mindustry.gen.Healthc;

public class MoveLightningBulletType extends LightningBulletType {
    public int damagePoints = 2;
    public int points = 20;

    public MoveLightningBulletType() {
        super();

        collides = reflectable = absorbable = false;
        pierce = pierceBuilding = true;
        pierceCap = -1;
    }

    @Override
    public void update(Bullet bullet) {
        super.update(bullet);

        //noinspection rawtypes
        if (bullet.data instanceof Seq vs && vs.size > 0) {
            int from, to, index = (int) ((vs.size + damagePoints) * bullet.fin());
            if (index <= damagePoints) {
                from = 0;
                to = index - 1;
            } else if (index >= vs.size) {
                from = index - 1 - damagePoints;
                to = vs.size - 1;
            } else {
                from = index - 1 - damagePoints;
                to = index - 1;
            }

            if (to >= from) {
                Seq<Vec2> show = new Seq<>();
                for (int i = from; i <= to; i++) {
                    show.add((Vec2) vs.get(i));
                }
                Vec2 f, t;
                for (int i = 0; i < show.size - 1; i++) {
                    f = show.get(i);
                    t = show.get(i + 1);
                    Damage.collideLine(bullet, bullet.team, hitEffect, f.x, f.y, f.angleTo(t), f.dst(t), false, false, -1);
                }
                Fx.lightning.at(show.first().x, show.first().y, 0, lightningColor, show);
            }
        }
    }

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

        Seq<Vec2> point = new Seq<>();
        Effects.findLineLightningPoints(b.x, b.y, b.rotation(), 6f, lightningLength, points, point);
        b.data = point;
    }
}
