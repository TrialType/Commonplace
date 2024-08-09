package Commonplace.FEntities.FUnit.F;

import Commonplace.FEntities.FUnit.Override.FLegsUnit;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.struct.Bits;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.pooling.Pools;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.ctype.ContentType;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.EntityCollisions;
import mindustry.entities.Leg;
import mindustry.entities.abilities.Ability;
import mindustry.entities.abilities.ShieldRegenFieldAbility;
import mindustry.entities.units.BuildPlan;
import mindustry.entities.units.StatusEntry;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Sounds;
import mindustry.graphics.InverseKinematics;
import mindustry.input.InputHandler;
import mindustry.type.Item;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;

public class BoostLegsUnit extends FLegsUnit {
    @Override
    public int classId() {
        return 100;
    }

    protected BoostLegsUnit() {
        this.applied = new Bits(Vars.content.getBy(ContentType.status).size);
        this.curMoveOffset = new Vec2();
        this.legs = new Leg[0];
        this.resupplyTime = Mathf.random(10.0F);
        this.statuses = new Seq<>();
    }

    public static BoostLegsUnit create() {
        return new BoostLegsUnit();
    }

    public boolean canPass(int tileX, int tileY) {
        return super.canPass(tileX, tileY);
    }

    public void hitboxTile(Rect rect) {
        super.hitboxTile(rect);
    }

    @Override
    public EntityCollisions.SolidPred solidity() {
        if (elevation > 0.09) {
            return null;
        } else {
            return this.type.allowLegStep ? EntityCollisions::legsSolid : EntityCollisions::solid;
        }
    }

    public boolean canMine() {
        return super.canMine();
    }

    public void drawBuildPlans() {
        super.drawBuildPlans();
    }

    public void drawBuilding() {
        super.drawBuilding();
    }

    public void drawBuildingBeam(float px, float py) {
        super.drawBuildingBeam(px, py);
    }

    public void drawPlan(BuildPlan plan, float alpha) {
        super.drawPlan(plan, alpha);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void updateDrowning() {
        super.updateDrowning();
    }
}
