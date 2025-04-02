package Commonplace.Type.EffectTypes;


import arc.graphics.Color;
import mindustry.entities.Effect;

import java.util.Arrays;

public class PackEffect extends Effect {
    public Effect effect;
    public float delay;
    public float rotation;
    public Color color;
    public float[] delays;

    public PackEffect(Effect self, Color color, float rotation, float delay, float... delays) {
        effect = self;
        this.color = color;
        this.rotation = rotation;
        this.delay = delay;
        this.delays = delays;
        Arrays.sort(this.delays);

        clip = effect.clip;
        lifetime = effect.lifetime + delay + delays[delays.length - 1];
        followParent = effect.followParent;
        rotWithParent = effect.rotWithParent;
    }

    @Override
    public void render(EffectContainer e) {
        if (e.time > delay) {
            var render = e.inner();
            for (int i = 0; i < delays.length; i++) {
                float d = delays[i];
                if (e.time > d + delay && e.time < d + delay + effect.lifetime) {
                    render.set(e.id + i, color, e.time - d + delay, effect.lifetime, rotation, e.x, e.y, e.data);
                    effect.render(render);
                }
            }
        }
    }
}
