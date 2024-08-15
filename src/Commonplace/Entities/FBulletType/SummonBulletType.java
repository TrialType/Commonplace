package Commonplace.Entities.FBulletType;

import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.game.Team;
import mindustry.gen.Bullet;
import mindustry.gen.Entityc;

public class SummonBulletType extends BasicBulletType {
    public BulletType summonBullet = null;
    public boolean summonHit = true;
    public boolean summonDespawned = true;
    public float summonRange = 150;
    public int summonNumber = 12;
    public float summonRadRandom = 360;
    public float summonDelay = 0;
    public float summonInterval = 0;

    public void hit(Bullet b, float x, float y) {
        super.hit(b, x, y);
        if (summonHit && summonBullet != null) {
            Team team = b.team;
            Entityc owner = b.owner;
            if (summonInterval > 0) {
                for (int i = 1; i < summonNumber + 1; i++) {
                    Time.run(i * summonInterval, () -> {
                        float rotation = Mathf.range(summonRadRandom / 2);
                        float dx = Angles.trnsx(rotation, summonRange);
                        float dy = Angles.trnsy(rotation, summonRange);
                        if (summonDelay > 0 && summonBullet.chargeEffect != null) {
                            summonBullet.chargeEffect.at(dx + x, dy + y, Angles.angle(-dx + x, -dy + y), hitColor);
                        }
                        Time.run(summonDelay, () -> summon(owner, team, dx + x, dy + y, Angles.angle(-dx, -dy)));
                    });
                }
            } else {
                for (int i = 0; i < summonNumber; i++) {
                    float rotation = Mathf.range(summonRadRandom / 2);
                    float dx = Angles.trnsx(rotation, summonRange);
                    float dy = Angles.trnsy(rotation, summonRange);
                    if (summonDelay > 0 && summonBullet.chargeEffect != null) {
                        summonBullet.chargeEffect.at(dx + x, dy + y, Angles.angle(-dx + x, -dy + y), hitColor);
                    }
                    Time.run(summonDelay, () -> summon(owner, team, dx + x, dy + y, Angles.angle(-dx, -dy)));
                }
            }
        }
    }

    public void summon(Entityc owner, Team team, float x, float y, float rotate) {
        Bullet bu = summonBullet.create(owner, team, x, y, rotate);
        bu.vel.trns(rotate, summonBullet.speed);
        bu.rotation(rotate);
    }

    public void despawned(Bullet b) {
        super.despawned(b);
        if (summonDespawned && !b.absorbed) {
            float x = b.x, y = b.y;
            Team team = b.team;
            Entityc owner = b.owner;
            if (summonInterval > 0) {
                for (int i = 1; i < summonNumber + 1; i++) {
                    Time.run(i * summonInterval, () -> {
                        float rotation = Mathf.range(summonRadRandom / 2);
                        float dx = Angles.trnsx(rotation, summonRange);
                        float dy = Angles.trnsy(rotation, summonRange);
                        if (summonDelay > 0 && summonBullet.chargeEffect != null) {
                            summonBullet.chargeEffect.at(dx + x, dy + y, Angles.angle(-dx + x, -dy + y), hitColor);
                        }
                        Time.run(summonDelay, () -> summon(owner, team, dx + x, dy + y, Angles.angle(-dx, -dy)));
                    });
                }
            } else {
                for (int i = 0; i < summonNumber; i++) {
                    float rotation = Mathf.range(summonRadRandom / 2);
                    float dx = Angles.trnsx(rotation, summonRange);
                    float dy = Angles.trnsy(rotation, summonRange);
                    if (summonDelay > 0 && summonBullet.chargeEffect != null) {
                        summonBullet.chargeEffect.at(dx + x, dy + y, Angles.angle(-dx + x, -dy + y), hitColor);
                    }
                    Time.run(summonDelay, () -> summon(owner, team, dx + x, dy + y, Angles.angle(-dx, -dy)));
                }
            }
        }
    }
}
