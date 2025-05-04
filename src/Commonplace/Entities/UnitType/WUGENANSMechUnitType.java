package Commonplace.Entities.UnitType;

import Commonplace.Entities.Unit.UnderLandMechUnit;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.math.Scaled;
import arc.math.geom.Vec2;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.entities.abilities.Ability;
import mindustry.entities.part.DrawPart;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.*;
import mindustry.graphics.Layer;
import mindustry.type.UnitType;

import static arc.graphics.g2d.Draw.xscl;
import static mindustry.Vars.player;


public class WUGENANSMechUnitType extends UnitType {
    private final static Vec2 legOffset = new Vec2();
    public float landTime = 60;
    public float outTime = 60;
    public float upDamage = -1;
    public float damageRadius = 60;
    public float landReload = 3600;
    public float needPower = 10000;
    public float powerRange = 1000;
    public float getRange = 100;

    public WUGENANSMechUnitType(String name) {
        super(name);
    }

    @Override
    public void draw(Unit unit) {
        float scl = xscl;
        if(unit.inFogTo(Vars.player.team())) return;

        if(buildSpeed > 0f){
            unit.drawBuilding();
        }

        if(unit.mining()){
            drawMining(unit);
        }

        boolean isPayload = !unit.isAdded();

        Mechc mech = unit instanceof Mechc m ? m : null;
        Segmentc seg = unit instanceof Segmentc c ? c : null;
        float z =
                isPayload ? Draw.z() :
                        //dead flying units are assumed to be falling, and to prevent weird clipping issue with the dark "fog", they always draw above it
                        unit.elevation > 0.5f || (flying && unit.dead) ? (flyingLayer) :
                                seg != null ? groundLayer + seg.segmentIndex() / 4000f * Mathf.sign(segmentLayerOrder) + (!segmentLayerOrder ? 0.01f : 0f) :
                                        groundLayer + Mathf.clamp(hitSize / 4000f, 0, 0.01f);

        if(!isPayload && (unit.isFlying() || shadowElevation > 0)){
            Draw.z(Math.min(Layer.darkness, z - 1f));
            drawShadow(unit);
        }

        Draw.z(z - 0.02f);

        if(mech != null){
            drawMech(mech);

            //side
            legOffset.trns(mech.baseRotation(), 0f, Mathf.lerp(Mathf.sin(mech.walkExtend(true), 2f/Mathf.PI, 1) * mechSideSway, 0f, unit.elevation));

            //front
            legOffset.add(Tmp.v1.trns(mech.baseRotation() + 90, 0f, Mathf.lerp(Mathf.sin(mech.walkExtend(true), 1f/Mathf.PI, 1) * mechFrontSway, 0f, unit.elevation)));

            unit.trns(legOffset.x, legOffset.y);
        }

        if (!(unit instanceof UnderLandMechUnit wu && wu.under)) {
            if(unit instanceof Tankc){
                drawTank((Unit & Tankc)unit);
            }

            if(unit instanceof Legsc && !isPayload){
                drawLegs((Unit & Legsc)unit);
            }

            Draw.z(Math.min(z - 0.01f, Layer.bullet - 1f));

            if(unit instanceof Payloadc){
                drawPayload((Unit & Payloadc)unit);
            }

            if(drawSoftShadow) drawSoftShadow(unit);

            Draw.z(z);

            if(unit instanceof Crawlc c){
                drawCrawl(c);
            }

            if(drawBody) drawOutline(unit);
            drawWeaponOutlines(unit);
            if(engineLayer > 0) Draw.z(engineLayer);
            if(trailLength > 0 && !naval && (unit.isFlying() || !useEngineElevation)){
                drawTrail(unit);
            }
            if(engines.size > 0) drawEngines(unit);
            Draw.z(z);
            if(drawBody) drawBody(unit);
            if(drawCell && !(unit instanceof Crawlc)) drawCell(unit);
            Draw.scl(scl);
            drawWeapons(unit);
            if(drawItems) drawItems(unit);
            if(!isPayload){
                drawLight(unit);
            }

            if(unit.shieldAlpha > 0 && drawShields){
                drawShield(unit);
            }

            if(parts.size > 0){
                for(int i = 0; i < parts.size; i++){
                    var part = parts.get(i);

                    WeaponMount mount = unit.mounts.length > part.weaponIndex ? unit.mounts[part.weaponIndex] : null;
                    if(mount != null){
                        DrawPart.params.set(mount.warmup, mount.reload / mount.weapon.reload, mount.smoothReload, mount.heat, mount.recoil, mount.charge, unit.x, unit.y, unit.rotation);
                    }else{
                        DrawPart.params.set(0f, 0f, 0f, 0f, 0f, 0f, unit.x, unit.y, unit.rotation);
                    }

                    if(unit instanceof Scaled s){
                        DrawPart.params.life = s.fin();
                    }

                    applyColor(unit);
                    part.draw(DrawPart.params);
                }
            }

            if (!isPayload) {
                for (Ability a : unit.abilities) {
                    Draw.reset();
                    a.draw(unit);
                }
            }
        }

        if(mech != null){
            unit.trns(-legOffset.x, -legOffset.y);
        }

        Draw.reset();
    }
}
