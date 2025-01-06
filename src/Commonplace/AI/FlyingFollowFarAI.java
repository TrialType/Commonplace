package Commonplace.AI;

import arc.math.Mathf;
import arc.util.Time;
import mindustry.ai.types.FlyingFollowAI;
import mindustry.entities.Sized;
import mindustry.entities.Units;

public class FlyingFollowFarAI extends FlyingFollowAI {
    public static FlyingFollowFarAI create() {
        return new FlyingFollowFarAI();
    }

    @Override
    public void updateMovement() {
        unloadPayloads();

        if (following != null) {
            moveTo(following, (following instanceof Sized s ? s.hitSize() / 2f * 1.1f : 0f) + unit.hitSize / 2f + 15f, 50f);
        } else if (target != null && unit.hasWeapons()) {
            moveTo(target, unit.range() * 0.98f);
        }

        if (shouldFaceTarget()) {
            unit.lookAt(target);
        } else if (following != null) {
            unit.lookAt(following);
        }

        if (timer.get(timerTarget3, 30f)) {
            following = Units.closest(unit.team, unit.x, unit.y, Math.max(unit.type.range, 400f), u -> !u.dead() && u.type != unit.type, (u, tx, ty) -> -u.maxHealth + Mathf.dst2(u.x, u.y, tx, ty) / 6400f);
        }
    }

    @Override
    public void updateVisuals() {
        if (unit.isFlying()) {
            if (following != null || (target != null && !unit.within(target, unit.range() * 0.8f))) {
                wobble(following == null && target != null && unit.within(target, unit.range()) ? 0.1f : 1f);
            }

            if (!shouldFaceTarget()) {
                unit.lookAt(unit.prefRotation());
            }
        }
    }

    public void wobble(float mul) {
        unit.x += mul * Mathf.sin(Time.time + (float) (unit.id % 10 * 12), 25.0F, 0.05F) * Time.delta * unit.elevation;
        unit.y += mul * Mathf.cos(Time.time + (float) (unit.id % 10 * 12), 25.0F, 0.05F) * Time.delta * unit.elevation;
    }
}
