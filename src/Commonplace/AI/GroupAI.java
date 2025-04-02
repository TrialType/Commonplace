package Commonplace.AI;

import Commonplace.Entities.Unit.GroupUnit;
import Commonplace.Utils.Classes.UnitGroup;
import arc.struct.Seq;
import arc.util.Tmp;
import mindustry.entities.Units;
import mindustry.entities.units.AIController;
import mindustry.game.Team;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;

public class GroupAI extends AIController {
    GroupUnit groupUnit;

    @Override
    public void updateUnit() {
        if (groupUnit != null) {
            if (useFallback() && (fallback != null || (fallback = fallback()) != null)) {
                fallback.unit(unit);
                fallback.updateUnit();
                return;
            }

            updateVisuals();
            updateTargeting();
            if (groupUnit.group != null) {
                if (groupUnit.leader && target != null && unit.dst(target) <= groupUnit.groupType.circleRange) {
                    groupUnit.group.groups.each(p -> {
                        if (p instanceof Unit u && u.controller() instanceof GroupAI a) {
                            a.setTarget(this.target);
                        }
                    });
                }
                updateMovement();
            } else if (timer.get(timerTarget4, 30)) {
                joinGroup();
            } else if (unit.team == Team.crux && Team.sharded.core() != null) {
                moveTo(Team.sharded.core(), 15);
            }
        } else {
            init();
        }
    }

    public void setTarget(Teamc target) {
        this.target = target;
    }

    @Override
    public void updateMovement() {
        if (groupUnit.group != null) {
            if (groupUnit.leader) {
                moveTo(target == null ? unit : target, 10);
            } else {
                float fin = (float) groupUnit.group.groups.indexOf(groupUnit) / (groupUnit.group.groups.size - 1);
                Tmp.v1.trns(fin * 360, groupUnit.hitSize + 10).add(groupUnit.group.groups.first());
                moveTo(Tmp.v1, 1);
            }
        }
    }

    @Override
    public Teamc target(float x, float y, float range, boolean air, boolean ground) {
        Teamc result = Units.closestTarget(unit.team, x, y, range, u -> u.checkTarget(air, ground), t -> ground);
        return result == null && unit.team == Team.crux ? Team.sharded.core() : result;
    }

    public void joinGroup() {
        GroupUnit gu = (GroupUnit) Units.closest(groupUnit.team, groupUnit.x, groupUnit.y, groupUnit.groupType.searchRange,
                unit -> unit instanceof GroupUnit g && g.group != null && groupUnit.canJoin(g.group) && g.group.canJoin(groupUnit.groupType));
        if (gu != null) {
            gu.group.addGroupmate(groupUnit);
        } else {
            GroupUnit[] leader = {groupUnit};
            Seq<GroupUnit> groups = new Seq<>();
            int[] value = {groupUnit.groupType.priority * 1000 + groupUnit.groupType.max};
            Units.nearby(groupUnit.team, groupUnit.x, groupUnit.y, groupUnit.groupType.searchRange, u -> {
                if (groups.size < leader[0].max() && u instanceof GroupUnit g && g.group == null) {
                    int va = g.groupType.priority * 1000 + g.groupType.max;
                    if (va > value[0]) {
                        value[0] = va;
                        leader[0] = g;
                    }
                }
            });
            if (groups.size >= leader[0].min()) {
                UnitGroup.create(groups.items);
            }
        }
    }

    @Override
    public void init() {
        if (unit instanceof GroupUnit g) {
            groupUnit = g;
        }
    }
}
