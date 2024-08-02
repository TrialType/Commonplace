package Floor.FEntities.FBulletType;

import arc.Events;
import arc.graphics.Color;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Time;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.game.EventType;
import mindustry.gen.*;
import mindustry.type.StatusEffect;

import java.util.HashMap;
import java.util.Map;

public class AroundBulletType extends BasicBulletType {
    private final static Map<Bullet, Unit> units = new HashMap<>();
    private final static Map<Unit, Boolean> lastStatus = new HashMap<>();

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

    @Override
    public void update(Bullet b) {
        super.update(b);

        updateMaps(b);

        if (b.data instanceof Unit u && b.within(u, circleRange + 5)) {
            b.fdata += Time.delta;
            Vec2 vec2 = new Vec2();
            float bx = b.x, by = b.y, ux = u.x, uy = u.y;
            float angle = Angles.angle(ux, uy, bx, by) + 5;
            vec2.set((float) (ux + circleRange * Math.cos(Math.toRadians(angle)) - bx), (float) (uy + circleRange * Math.sin(Math.toRadians(angle)) - by));
            vec2.setLength(b.vel.len());
            b.vel.set(vec2);

            if (roundIntervalBullet != null && b.fdata > roundIntervalDelay && b.timer(3, roundBulletInterval)) {
                if (roundIntervalCenter) {
                    for (int i = 0; i < roundIntervalBullets; i++) {
                        roundIntervalBullet.create(b.owner, b.team, bx, by, b.angleTo(u),
                                -1, 1, 1, null, null, u.x, u.y);
                    }
                } else {
                    for (int i = 0; i < roundIntervalBullets; i++) {
                        roundIntervalBullet.create(b.owner, b.team, bx, by,
                                b.rotation() + roundIntervalAngle + Mathf.range(roundIntervalRandomSpread) +
                                        roundIntervalSpread * (i - ((roundIntervalBullets - 1) / 2f)),
                                -1, 1, 1, null, null, u.x, u.y);
                    }
                }
            }

            if (!u.hasEffect(statusEffect) && !lastStatus.get(u)) {
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
        } else if (b.data instanceof Unit u) {
            b.fdata = 0;
            Vec2 vec2 = new Vec2();
            vec2.set(u.x - b.x, u.y - b.y);
            vec2.setLength(b.vel.len());
            b.rotation(Angles.angle(u.x - b.x, u.y - b.y));
            b.move(vec2);
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
}
