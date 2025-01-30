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
    protected final Map<Unit, Float> unitTimers = new HashMap<>();
    protected final Map<Building, Float> buildingTimers = new HashMap<>();

    public float damage = 5;
    public float max = 35;
    public float range = -1;
    public float buildingMul = 1;

    public TimeLargeDamageAbility(float damage, float range) {
        this.damage = damage;
        this.range = range;
    }

    public TimeLargeDamageAbility() {
    }

    public void update(Unit unit) {
        updateTimes(unit);

        Team team = unit.team;
        float x = unit.x, y = unit.y;
        float radius = range < 0 ? 42 : range;
        Units.nearbyEnemies(team, x, y, radius, u -> {
            float timer = unitTimers.computeIfAbsent(u, uu -> 0F);
            float damage = (float) Math.pow(this.damage, timer / 120) * this.damage / 4;
            u.damagePierce(max < 0 ? damage : Math.min(damage, max));
        });
        Units.nearbyBuildings(x, y, radius, b -> {
            if (b.team != team) {
                float timer = buildingTimers.computeIfAbsent(b, uu -> 0F);
                float damage = (float) Math.pow(this.damage, timer / 120) * this.damage / 4;
                b.damagePierce((max < 0 ? damage : Math.min(damage, max)) * buildingMul);
            }
        });
    }

    private void updateTimes(Unit unit) {
        float radius = range < 0 ? 42 : range;
        Seq<Unit> units = new Seq<>();
        Seq<Building> buildings = new Seq<>();
        for (Unit u : unitTimers.keySet()) {
            if (u.dead || u.health() <= 0 || !u.within(unit, radius)) {
                units.add(u);
            }
        }
        for (Building b : buildingTimers.keySet()) {
            if (b.dead || b.health() <= 0 || !b.within(unit, radius)) {
                buildings.add(b);
            }
        }
        for (Unit u : units) {
            unitTimers.remove(u);
        }
        for (Building b : buildings) {
            buildingTimers.remove(b);
        }
        buildingTimers.replaceAll((b, f) -> f + Time.delta);
        unitTimers.replaceAll((u, f) -> f + Time.delta);
    }

    @Override
    public void addStats(Table t) {
        t.add("[lightgray]" + Stat.range.localized() + ": [white]" + range);
        t.row();
        t.add("[lightgray]" + Core.bundle.get("stat.base_damage") + ": [white]" + damage);
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
