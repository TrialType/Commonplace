package Commonplace.Entities.Ability;

import arc.Core;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.entities.Units;
import mindustry.entities.abilities.Ability;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

import java.util.HashMap;
import java.util.Map;

public class TimeLargeDamageAbility extends Ability {
    protected final Map<Unit, Float> unitTimes = new HashMap<>();
    protected final Map<Building, Float> buildingTimes = new HashMap<>();

    public float baseDamage = 5;
    public float maxDamage = 35;
    public float damageRange = -1;
    public float buildingMul = 1;

    public TimeLargeDamageAbility(float damage, float damageRange) {
        baseDamage = damage;
        this.damageRange = damageRange;
    }

    public TimeLargeDamageAbility() {
    }

    public void update(Unit unit) {
        updateTimes(unit);

        Team team = unit.team;
        float x = unit.x, y = unit.y;
        float radius = damageRange < 0 ? 42 : damageRange;
        Units.nearbyEnemies(team, x, y, radius, u -> {
            float timer = unitTimes.computeIfAbsent(u, uu -> 0F);
            float damage = (float) Math.pow(baseDamage, timer / 120) * baseDamage / 4;
            u.damagePierce(maxDamage < 0 ? damage : Math.min(damage, maxDamage));
        });
        Units.nearbyBuildings(x, y, radius, b -> {
            if (b.team != team) {
                float timer = buildingTimes.computeIfAbsent(b, uu -> 0F);
                float damage = (float) Math.pow(baseDamage, timer / 120) * baseDamage / 4;
                b.damagePierce((maxDamage < 0 ? damage : Math.min(damage, maxDamage)) * buildingMul);
            }
        });
    }

    private void updateTimes(Unit unit) {
        float radius = damageRange < 0 ? 42 : damageRange;
        Seq<Unit> units = new Seq<>();
        Seq<Building> buildings = new Seq<>();
        for (Unit u : unitTimes.keySet()) {
            if (u.dead || u.health() <= 0 || !u.within(unit, radius)) {
                units.add(u);
            }
        }
        for (Building b : buildingTimes.keySet()) {
            if (b.dead || b.health() <= 0 || !b.within(unit, radius)) {
                buildings.add(b);
            }
        }
        for (Unit u : units) {
            unitTimes.remove(u);
        }
        for (Building b : buildings) {
            buildingTimes.remove(b);
        }
        buildingTimes.replaceAll((b, f) -> f + Time.delta);
        unitTimes.replaceAll((u, f) -> f + Time.delta);
    }

    @Override
    public void addStats(Table t) {
        t.add("[lightgray]" + Stat.range.localized() + ": [white]" + damageRange);
        t.row();
        t.add("[lightgray]" + Core.bundle.get("stat.base_damage") + ": [white]" + baseDamage);
        t.row();
        t.add("[lightgray]" + Core.bundle.get("stat.formula") + ": [white]" +
                Core.bundle.get("stat.base_damage") + "^ (" + Core.bundle.get("stat.continue") + "/120 + 1) / 4" +
                StatUnit.seconds.localized());
        t.row();
        t.add("[lightgray]" + Core.bundle.get("stat.building_expand") + ": [white]" + buildingMul);
    }

    @Override
    public String localized() {
        return Core.bundle.format("ability.time-large-damage.name");
    }
}
