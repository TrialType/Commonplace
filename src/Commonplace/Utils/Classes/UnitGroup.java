package Commonplace.Utils.Classes;

import Commonplace.Entities.UnitType.GroupUnitType;
import Commonplace.Utils.Interfaces.Groupmate;
import arc.func.Boolf;
import arc.func.Cons;
import arc.struct.Seq;
import mindustry.entities.abilities.Ability;

public class UnitGroup {
    public static final Cons<UnitGroup> none = g -> {
    };

    Cons<UnitGroup> stronger = none;

    public final Seq<Groupmate> groups = new Seq<>();
    public final Seq<Ability> leadAbility = new Seq<>();
    public final Seq<Ability> abilities = new Seq<>();

    public int max = 100, min = 0;

    public float damageAdd = 0, damageMul = 1, damageMax = -1, damageMin = -1;
    public float healthAdd = 0, healthMul = 1, healthMax = -1, healthMin = -1;
    public float reloadAdd = 0, reloadMul = 1, reloadMax = -1, reloadMin = -1;

    public static void create(Groupmate... mates) {
        UnitGroup group = new UnitGroup();
        group.groups.set(mates);
        group.groups.each(g -> g.group(group));
        group.updateLeader();
    }

    public void updateGroup() {
        groups.removeAll(g -> {
            if (!g.isGroupmate()) {
                g.out();
                return true;
            }
            return false;
        });
        updateLeader();
    }

    public void addGroupmate(Groupmate group) {
        if (!groups.contains(group) && groups.size < max) {
            groups.add(group);
            updateLeader();
        }
    }

    public boolean canJoin(GroupUnitType type) {
        return groups.first().canBeMate(type) && groups.size < max;
    }

    private void updateLeader() {
        if (groups.size > 1) {
            groups.first().leader(false);
            groups.sort(g -> g.priority() * 1000 + g.max());
            groups.first().leader(true);
            max = groups.first().max();
            min = groups.first().min();
            stronger = groups.first().stronger();
            stronger.get(this);
        } else if (groups.any()) {
            groups.first().out();
            groups.clear();
            abilities.clear();
            leadAbility.clear();
        }
    }

    public void damage_a(float num) {
        damageAdd += num;
    }

    public void damage_m(float num) {
        damageMul *= num;
    }

    public void damage_t(float min, float max, boolean si, boolean sa, boolean ci, boolean ca) {
        damageMax = sa ? max : ca ? Math.min(max, damageMax) : Math.max(max, damageMax);
        damageMin = si ? min : ci ? Math.max(min, damageMin) : Math.min(min, damageMin);
    }

    public void health_a(float num) {
        healthAdd += num;
    }

    public void health_m(float num) {
        healthMul *= num;
    }

    public void health_t(float min, float max, boolean si, boolean sa, boolean ci, boolean ca) {
        healthMax = sa ? max : ca ? Math.min(max, healthMax) : Math.max(max, healthMax);
        healthMin = si ? min : ci ? Math.max(min, healthMin) : Math.min(min, healthMin);
    }

    public void reload_a(float num) {
        reloadAdd += num;
    }

    public void reload_m(float num) {
        reloadMul *= num;
    }

    public void reload_t(float min, float max, boolean si, boolean sa, boolean ci, boolean ca) {
        reloadMax = sa ? max : ca ? Math.min(max, reloadMax) : Math.max(max, reloadMax);
        reloadMin = si ? min : ci ? Math.max(min, reloadMin) : Math.min(min, reloadMin);
    }

    public void ability_lr(Boolf<Ability> ability) {
        leadAbility.removeAll(ability);
    }

    public void ability_la(Ability ability) {
        if (!leadAbility.contains(ability)) {
            leadAbility.add(ability);
        }
    }

    public void ability_r(Boolf<Ability> ability) {
        abilities.removeAll(ability);
    }

    public void ability_a(Ability ability) {
        if (!abilities.contains(ability)) {
            abilities.add(ability);
        }
    }
}
