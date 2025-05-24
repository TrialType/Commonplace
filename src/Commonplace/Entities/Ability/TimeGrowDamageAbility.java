package Commonplace.Entities.Ability;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.struct.IntMap;
import arc.struct.ObjectMap;
import arc.util.Time;
import mindustry.entities.Units;
import mindustry.entities.abilities.Ability;
import mindustry.gen.*;
import mindustry.graphics.Pal;

public class TimeGrowDamageAbility extends Ability {
    public static IntMap<Float> damages = new IntMap<>();
    protected ObjectMap<Unit, Float> unitTimers;
    protected ObjectMap<Building, Float> buildingTimers;

    public float max = 20;
    public float damage = 5;
    public float range = 42;
    public float allMax = 70;
    public float buildingMul = 1;
    public float armorPierce = 18;
    public boolean pierceArmor = false;

    public TimeGrowDamageAbility(float damage, float range) {
        this.damage = damage;
        this.range = range;
    }

    public TimeGrowDamageAbility() {
    }

    public void update(Unit unit) {
        updateTimes(unit);

        Units.nearbyEnemies(unit.team, unit.x, unit.y, range, u -> {
            if (u.type.targetable) {
                if (unitTimers.containsKey(u)) {
                    applyEntity(u, unitTimers.get(u));
                } else {
                    unitTimers.put(u, 0f);
                    applyEntity(u, 0);
                }
            }
        });
        Units.nearbyBuildings(unit.x, unit.y, range, b -> {
            if (b.team != unit.team && b.block.targetable) {
                if (buildingTimers.containsKey(b)) {
                    applyEntity(b, buildingTimers.get(b));
                } else {
                    buildingTimers.put(b, 0f);
                    applyEntity(b, 0);
                }
            }
        });
    }

    private void applyEntity(Healthc h, float time) {
        float mul = h instanceof Building ? buildingMul : 1;
        float damage = (float) Math.pow(this.damage, time / 120) / 4;
        float used = damages.get(h.id(), 0f);
        damage = mul * (max < 0 ? damage : Math.min(damage, max));
        damage = allMax < 0 ? damage : allMax <= used ? 0 : Math.min(allMax - used, damage);
        damages.put(h.id(), used + damage);
        if (pierceArmor) {
            h.damagePierce(damage * Time.delta);
        } else {
            h.damage(damage * Time.delta + (h instanceof Shieldc s ? Math.min(armorPierce, s.armor()) : 0));
        }
    }

    private void updateTimes(Unit unit) {
        for (Unit u : unitTimers.keys()) {
            if (u == null || !u.isValid() || !unit.within(u, range + u.hitSize / 2)) {
                unitTimers.remove(u);
            } else {
                unitTimers.put(u, unitTimers.get(u) + Time.delta);
            }
        }
        for (Building b : buildingTimers.keys()) {
            if (b == null || !b.isValid() || !unit.within(b, range + b.hitSize() / 2)) {
                buildingTimers.remove(b);
            } else {
                buildingTimers.put(b, buildingTimers.get(b) + Time.delta);
            }
        }
    }

    @Override
    public void draw(Unit u) {
        Draw.color(Pal.redDust);
        Lines.stroke(3f * Mathf.sinDeg(1.5f * Time.time + 17 * u.id));
        Lines.circle(u.x, u.y, range);
    }

    public void init() {
        unitTimers = new ObjectMap<>();
        buildingTimers = new ObjectMap<>();
    }

    public Ability copy() {
        try {
            Ability a = (Ability) clone();
            if (a instanceof TimeGrowDamageAbility t) {
                t.init();
            }
            return a;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("java sucks", e);
        }
    }

    @Override
    public String localized() {
        return Core.bundle.format("ability.time-large-damage.name");
    }
}
