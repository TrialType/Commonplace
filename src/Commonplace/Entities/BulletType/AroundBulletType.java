package Commonplace.Entities.BulletType;

import arc.Events;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.type.StatusEffect;

import java.util.HashMap;
import java.util.Map;

public class AroundBulletType extends BasicBulletType {
    private final Map<Bullet, Unit> units = new HashMap<>();
    private final Map<Unit, Boolean> lastStatus = new HashMap<>();

    public float targetRange = 100;
    public float circleRange = 40;
    public float statusTime = 120;
    public StatusEffect statusEffect = StatusEffects.wet;
    public Effect applyEffect = Fx.none;
    public Color applyColor = Color.valueOf("06172699");
    public BulletType roundIntervalBullet = null;
    public float roundBulletInterval = 20;
    public int roundIntervalBullets = 1;
    public boolean roundIntervalCenter = true;
    public float roundIntervalRandomSpread = 360f;
    public float roundIntervalSpread = 0f;
    public float roundIntervalAngle = 0f;
    public float roundIntervalDelay = -1f;
    public float roundBulletDelay = -1f;

    @Override
    public void update(Bullet b) {
        super.update(b);

        updateMaps(b);

        if (b.data instanceof Unit u) {
            if (b.within(u, circleRange * 1.1f)) {
                b.fdata += Time.delta;
            } else {
                b.fdata = 0;
            }

            float bx = b.x, by = b.y, ux = u.x, uy = u.y;
            float len = b.dst(u);
            if (len >= circleRange) {
                float angle = b.angleTo(u) - (float) (Mathf.radDeg * Math.asin(circleRange / len) + 360) % 360;
                float angleDst = Math.abs(b.rotation() - angle);
                float step = (b.rotation() > angle ? -1 : 1) * Math.min(angleDst, 5f);
                if (angleDst <= 30 && angleDst >= 0) {
                    b.initVel(b.rotation() + step, b.vel.len() * 1.05f);
                } else if (angleDst <= 90) {
                    b.initVel(b.rotation() + step, b.vel.len());
                } else if (angleDst <= 180) {
                    b.initVel(b.rotation() + step, Math.max(b.vel.len() * 0.85f, 2f));
                } else if (angleDst <= 270) {
                    b.initVel(b.rotation() - step, Math.max(b.vel.len() * 0.85f, 2f));
                } else if (angleDst <= 330) {
                    b.initVel(b.rotation() - step, b.vel.len());
                } else {
                    b.initVel(b.rotation() - step, b.vel.len() * 1.05f);
                }
            } else {
                b.initVel(b.rotation(), b.vel.len() * 1.05f);
            }

            if(b.fdata > 0 && b.fdata % 60 == 0){
                b.collided.clear();
            }

            if (roundIntervalBullet != null && b.fdata > roundIntervalDelay && b.timer(3, roundBulletInterval)) {
                Entityc owner = b.owner;
                Team team = b.team;
                if (roundIntervalCenter) {
                    float a = b.angleTo(u);
                    for (int i = 0; i < roundIntervalBullets; i++) {
                        if (roundBulletDelay > 0) {
                            if (roundIntervalBullet.chargeEffect != null) {
                                roundIntervalBullet.chargeEffect.at(bx, by, a, hitColor);
                            }
                            Time.run(roundBulletDelay, () -> roundIntervalBullet.create(owner, team, bx, by, a,
                                    -1, 1, 1, null, null, ux, uy));
                        } else {
                            roundIntervalBullet.create(owner, team, bx, by, a,
                                    -1, 1, 1, null, null, ux, uy);
                        }
                    }
                } else {
                    float ro = b.rotation();
                    for (int i = 0; i < roundIntervalBullets; i++) {
                        if (roundBulletDelay > 0) {
                            if (roundIntervalBullet.chargeEffect != null) {
                                roundIntervalBullet.chargeEffect.at(bx, by);
                            }
                            int finalI = i;
                            Time.run(roundBulletDelay, () -> roundIntervalBullet.create(owner, team, bx, by,
                                    ro + roundIntervalAngle + Mathf.range(roundIntervalRandomSpread) +
                                            roundIntervalSpread * (finalI - ((roundIntervalBullets - 1) / 2f)),
                                    -1, 1, 1, null, null, ux, uy));
                        } else {
                            roundIntervalBullet.create(owner, team, bx, by,
                                    ro + roundIntervalAngle + Mathf.range(roundIntervalRandomSpread) +
                                            roundIntervalSpread * (i - ((roundIntervalBullets - 1) / 2f)),
                                    -1, 1, 1, null, null, ux, uy);
                        }
                    }
                }
            }

            if (b.within(u, circleRange * 1.1f) && !u.hasEffect(statusEffect) && !lastStatus.get(u)) {
                Units.nearbyEnemies(b.team, ux, uy, circleRange, unit -> {
                    applyEffect.at(unit.x, unit.y, 0, applyColor, unit);

                    boolean dead = unit.dead;
                    unit.damage(damage);
                    if (!dead && unit.dead) {
                        Events.fire(new EventType.UnitBulletDestroyEvent(unit, b));
                        return;
                    }

                    unit.apply(statusEffect, statusTime);

                    if (lastStatus.containsKey(unit)) {
                        if (!unit.dead) {
                            lastStatus.put(unit, true);
                        } else {
                            lastStatus.remove(unit);
                        }
                    }
                });
                b.time = b.lifetime;
            }
        } else {
            updateTarget(b);
        }
    }

    @Override
    public void removed(Bullet b) {
        super.removed(b);

        Unit u = (Unit) b.data;
        units.remove(b);
        b.data = true;
        units.forEach((bullet, unit) -> {
            if (unit == u) {
                b.data = false;
            }
        });
        if ((boolean) b.data) {
            lastStatus.remove(u);
        }
        b.data = null;
    }

    public void updateTarget(Bullet b) {
        b.data = Units.closestEnemy(b.team, b.x, b.y, targetRange, u -> u.hasWeapons() && !had(u));
        if (b.data == null) {
            b.data = Units.closestEnemy(b.team, b.x, b.y, targetRange, Unitc::hasWeapons);
        }
        if (b.data != null) {
            units.put(b, (Unit) b.data);
            lastStatus.put((Unit) b.data, lastStatus.computeIfAbsent((Unit) b.data, t -> false));
        }
    }

    public boolean had(Unit u) {
        for (Bullet ab : units.keySet()) {
            if (units.get(ab) == u) {
                return true;
            }
        }
        return false;
    }

    public void updateMaps(Bullet b) {
        if (b.data instanceof Unit u) {
            if (u.dead || u.health <= 0 || !u.isAdded()) {
                lastStatus.remove(u);
                units.remove(b);
                b.data = null;
            } else if (u.hasEffect(statusEffect)) {
                lastStatus.put(u, false);
            }
        } else {
            lastStatus.remove(null);
            units.remove(b);
            b.data = null;
        }
    }

    @Override
    public void init() {
        super.init();

        homingDelay = homingPower = 0;
    }
}
