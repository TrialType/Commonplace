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
    public BulletType summon = null;
    public boolean summonHit = true;
    public boolean summonDespawned = true;
    public float summonRange = 150;
    public int summonNumber = 12;
    public float summonRadRandom = 360;
    public float summonDelay = 0;
    public float everySummonDelay = 0;

    public void hit(Bullet b, float x, float y) {
        super.hit(b, x, y);
        if (summonHit && summon != null) {
            if (everySummonDelay > 0) {
                for (int i = 0; i < summonNumber; i++) {
                    Time.run(summonDelay + i * everySummonDelay, () -> summon(b.owner, b.team, x, y, Mathf.range(summonRadRandom / 2)));
                }
            } else {
                for (int i = 0; i < summonNumber; i++) {
                    Time.run(summonDelay, () -> summon(b.owner, b.team, x, y, Mathf.range(summonRadRandom / 2)));
                }
            }
        }
    }

    public void summon(Entityc owner, Team team, float x, float y, float rotate) {
        float dx = (float) (summonRange * Math.cos(rotate)), dy = (float) (summonRange * Math.sin(rotate));
        Bullet bu = summon.create(owner, team, dx + x, dy + y, -rotate);
        bu.vel.set(-dx, -dy).setLength(summon.speed);
        bu.rotation(Angles.angle(-dx, -dy));
    }

    public void despawned(Bullet b) {
        super.despawned(b);
        if (summonDespawned && !b.absorbed) {
            if (everySummonDelay > 0) {
                for (int i = 0; i < summonNumber; i++) {
                    Time.run(summonDelay + i * everySummonDelay, () -> summon(b.owner, b.team, b.x, b.y, Mathf.range(summonRadRandom / 2)));
                }
            } else {
                for (int i = 0; i < summonNumber; i++) {
                    Time.run(summonDelay, () -> summon(b.owner, b.team, b.x, b.y, Mathf.range(summonRadRandom / 2)));
                }
            }
        }
    }
}
