package Commonplace.UI.Elements;

import arc.Core;
import arc.util.Align;
import mindustry.Vars;
import arc.scene.Group;
import arc.input.KeyCode;
import arc.scene.Element;
import arc.scene.ui.Label;
import arc.graphics.Color;
import mindustry.gen.Tex;
import arc.graphics.g2d.Draw;
import mindustry.type.UnitType;
import arc.scene.event.Touchable;
import arc.scene.actions.Actions;
import arc.scene.event.InputEvent;
import arc.scene.event.InputListener;
import arc.scene.style.TextureRegionDrawable;
import mindustry.ui.Styles;

public class UnitGroup extends Group {
    public static final float labelWidth = 150;
    public static final float labelHeight = 40;

    private UnitLabel actor;
    private final FeatureLabel display;
    private float displayTimer = 0;
    private boolean displaying = false;

    protected int pointId = -1;
    protected boolean enter;
    protected float tid = 0;
    protected float lastY = -1;

    public UnitGroup() {
        super();

        width = labelWidth;
        height = 0;

        display = new FeatureLabel("");
        display.setSize(labelWidth, labelHeight * 5);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button) {
                return Vars.mobile;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (pointId == pointer) {
                    if (lastY >= 0) {
                        tid += y - lastY;
                    }
                    lastY = y;
                    tid = Math.max(0, Math.min(children.size - 5, tid));

                    invalidate();
                } else {
                    lastY = -1;
                    pointId = pointer;
                }
            }

            @Override
            public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
                tid += amountY;
                tid = Math.max(0, Math.min(children.size - 5, tid));

                invalidate();
                return false;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Element fromActor) {
                enter = true;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Element fromActor) {
                if (x >= translation.x && x <= width + translation.x && y >= translation.y && y <= height + translation.y) {
                    return;
                }

                enter = false;
                Core.scene.setScrollFocus(null);
            }
        });
    }

    @Override
    public void drawChildren() {
        validate();

        super.drawChildren();

        if (displayTimer > 0 || displaying) {
            Element e = display;
            float dx = e.x, dy = e.y;
            if (transform) {
                e.x += e.translation.x;
                e.y += e.translation.y;
            } else {
                e.x += x + e.translation.x;
                e.y += y + e.translation.y;
            }
            e.draw();
            e.x = dx;
            e.y = dy;
        }
    }

    @Override
    public void layout() {
        float height = getHeight(), offsetX = transform ? 0 : -x, offsetY = transform ? 0 : -y;
        for (int i = 0, n = children.size; i < n; i++) {
            Element e = children.get(i);
            e.x = x + offsetX;
            if (i >= (int) tid && i < tid + 5) {
                e.y = y + offsetY + height - labelHeight * (i - (int) tid + 1);
            } else {
                e.y = y + offsetY;
            }
        }

        display.y = Math.min(children.size - 5, 0) * labelHeight;
    }

    @Override
    public Element hit(float x, float y, boolean touchable) {
        if (touchable && this.touchable == Touchable.disabled) return null;
        Element[] childrenArray = children.items;
        for (int i = children.size - 1; i >= 0; i--) {
            Element child = childrenArray[i];
            Element hit = child.hit(x, y, touchable);
            if (hit != null) {
                return hit;
            }
        }
        return x >= translation.x && x <= width + translation.x && y >= translation.y && y <= height + translation.y ? this : null;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (enter) {
            requestScroll();
        }
        actDisplay(delta);
    }

    protected void changeTid(float change) {
        tid += change;
        tid = Math.max(0, Math.min(children.size - 5, tid));
        invalidate();
    }

    public void addLabel(UnitType type) {
        addChild(new UnitLabel(type));
        if (children.size <= 5) {
            y -= labelHeight;
            height += labelHeight;
        }
        tid = Math.max(0, Math.min(children.size - 5, tid));
        invalidate();
    }

    private void removeLabel(UnitLabel label) {
        removeChild(label);
        if (children.size < 5) {
            y += labelHeight;
            height -= labelHeight;
        }
        tid = Math.max(0, Math.min(children.size - 5, tid));
        invalidate();
    }

    private boolean isHide(UnitLabel label) {
        int idx = children.indexOf(label);
        return idx < tid || idx > tid + 4;
    }

    private void showDisplay(UnitLabel actor, UnitType type) {
        if (!displaying) {
            displayTimer = 0.1f;
            displaying = true;
        }

        if (this.actor != actor) {
            this.actor = actor;
            display.setType(type);
        }
    }

    private void hideDisplay() {
        if (displaying) {
            actor = null;
            displaying = false;
            displayTimer = 0.1f;
        }
    }

    private void actDisplay(float delta) {
        if (displayTimer > 0) {
            displayTimer = Math.max(displayTimer - delta, 0);
            float fin;
            if (displaying) {
                fin = (0.1f - displayTimer) / 0.1f;
            } else {
                fin = displayTimer / 0.1f;
            }
            display.setAlpha(fin);
            display.x = x + fin * labelWidth;
        }
    }

    private class UnitLabel extends Label {
        protected float lastX = -1, deltaX = 0;

        protected boolean left;
        protected boolean right;
        protected boolean fading;
        protected boolean deploy;

        protected int lastPointer = -1;

        protected float deployTimer;
        protected float regionW, regionH, regionX, regionY;
        protected TextureRegionDrawable region;

        public UnitLabel(UnitType type) {
            super(type.localizedName);

            invalidate();
            deploy = false;
            fading = false;
            deployTimer = 0f;
            width = labelWidth;
            height = labelHeight;
            setAlignment(Align.center);
            region = new TextureRegionDrawable(type.uiIcon);

            if (Vars.mobile) {
                addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button) {
                        left = true;
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, KeyCode button) {
                        UnitLabel label = UnitLabel.this;
                        UnitGroup group = UnitGroup.this;
                        if (right) {
                            fading = true;
                            clearActions();
                            if (group.actor == label) {
                                group.hideDisplay();
                            }
                            actions(Actions.moveTo(-width, UnitLabel.this.y, 0.15f));
                        } else if (left) {
                            left = false;
                            int idx = group.children.indexOf(label);
                            if (idx == group.tid && idx != 0) {
                                group.changeTid(-2);
                            } else if (idx == group.tid + 4 && idx != group.children.size - 1) {
                                group.changeTid(2);
                            } else if (actor != label) {
                                group.showDisplay(label, type);
                            } else {
                                group.hideDisplay();
                            }
                        } else {
                            actions(Actions.moveTo(label.x, label.y, 0.1f));
                        }
                    }

                    @Override
                    public void touchDragged(InputEvent event, float x, float y, int pointer) {
                        left = false;
                        if (pointer == lastPointer) {
                            if (lastX < 0) {
                                lastX = UnitLabel.this.x - x;
                            }
                            UnitLabel.this.x = x + lastX;
                        } else {
                            lastX = -1;
                            lastPointer = pointer;
                        }
                    }
                });
            } else {
                addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button) {
                        if (button == KeyCode.mouseRight) {
                            right = true;
                        } else if (button == KeyCode.mouseLeft) {
                            left = true;
                        }
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, KeyCode button) {
                        UnitLabel label = UnitLabel.this;
                        UnitGroup group = UnitGroup.this;
                        if (right) {
                            fading = true;
                            clearActions();
                            if (group.actor == label) {
                                group.hideDisplay();
                            }
                            actions(Actions.moveTo(-width, UnitLabel.this.y, 0.15f));
                        } else if (left) {
                            left = false;
                            int idx = group.children.indexOf(label);
                            if (idx == group.tid && idx != 0) {
                                group.changeTid(-2);
                            } else if (idx == group.tid + 4 && idx != group.children.size - 1) {
                                group.changeTid(2);
                            } else if (actor != label) {
                                group.showDisplay(label, type);
                            } else {
                                group.hideDisplay();
                            }
                        }
                    }

                    @Override
                    public void exit(InputEvent event, float x, float y, int pointer, Element fromActor) {
                        if (x <= UnitLabel.this.x + width && x >= UnitLabel.this.x && y <= UnitLabel.this.y + height && y >= UnitLabel.this.y) {
                            return;
                        }

                        if (!fading) {
                            left = false;
                            right = false;
                        }
                    }
                });
            }
        }

        @Override
        public void draw() {
            if (UnitGroup.this.isHide(this)) {
                return;
            }

            validate();

            int id = UnitGroup.this.children.indexOf(this);
            float x = this.x + deltaX;
            if (id == tid && id != 0 || id == tid + 4 && id != UnitGroup.this.children.size - 1) {
                parentAlpha = 0.5f;
            } else {
                parentAlpha = 1;
            }
            parentAlpha *= 1 - (parent.x - x / width);

            Color color = this.color.cpy();
            color.a *= parentAlpha;
            Draw.color(color.r, color.g, color.b, color.a);
            Tex.buttonEdge3.draw(x, y, width * scaleX, height * scaleY);

            Draw.color(this.color);
            Draw.alpha(parentAlpha * this.color.a);

            region.draw(x + regionX, y + regionY, regionW * scaleX, regionH * scaleY);

            if (style.fontColor != null) {
                color.mul(style.fontColor);
            }
            cache.tint(color);
            cache.setPosition(x + 15, y);
            cache.draw();
        }

        @Override
        public void layout() {
            super.layout();

            deployTimer = 0.15f;
            deltaX = -width;

            float scale = Math.max(region.getMinWidth() / (labelHeight - 5), region.getMinHeight() / (labelHeight - 5));
            regionW = region.getMinWidth() / scale * 0.9f;
            regionH = region.getMinHeight() / scale * 0.9f;
            regionX = 5;
            regionY = (height - regionH) / 2;
        }

        @Override
        public void act(float delta) {
            if (deployTimer > 0) {
                deployTimer -= delta;
                deployTimer = Math.max(deployTimer, 0);
                deltaX = -width * deployTimer / 0.15f;
                if (deployTimer <= 0) {
                    deploy = true;
                }
            } else if (fading && x <= -width) {
                UnitGroup.this.removeLabel(this);
            } else {
                super.act(delta);
            }
        }

        @Override
        public Element hit(float x, float y, boolean touchable) {
            return deploy && !UnitGroup.this.isHide(this) && touchable && this.touchable == Touchable.enabled &&
                    x <= this.x + width && x >= this.x && y <= this.y + height && y >= this.y ? this : null;
        }
    }

    private static class FeatureLabel extends Label {
        protected String feature;
        protected float regionW, regionH, regionX, regionY;
        protected TextureRegionDrawable region;

        public FeatureLabel(CharSequence text) {
            super(text);

            width = labelWidth;
            height = labelHeight * 5;
            setAlignment(Align.left);
        }

        protected void setAlpha(float alpha) {
            parentAlpha = alpha;
        }

        protected void setType(UnitType type) {
            feature = Core.bundle.get("unit." + type.name + ".feature", "无法确定该单位特征");
            setText(feature);
            region = new TextureRegionDrawable(type.uiIcon);
        }

        @Override
        public void draw() {
            validate();

            Color color = this.color.cpy();
            color.a *= parentAlpha;
            Draw.color(color.r, color.g, color.b, color.a);
            Styles.black3.draw(x, y, width * scaleX, height * scaleY);

            Draw.color(this.color);
            Draw.alpha(parentAlpha * this.color.a);

            region.draw(x + regionX, y + regionY, regionW * scaleX, regionH * scaleY);

            if (style.fontColor != null) {
                color.mul(style.fontColor);
            }
            cache.tint(color);
            cache.setPosition(x + 15, y);
            cache.draw();
        }

        @Override
        public void layout() {
            super.layout();

            float scale = Math.max(region.getMinWidth() / 60, region.getMinHeight() / 60);
            regionW = region.getMinWidth() / scale * 0.9f;
            regionH = region.getMinHeight() / scale * 0.9f;
            regionX = labelWidth / 2 - regionW / 2;
            regionY = height - regionH - 5;
        }

        @Override
        public Element hit(float x, float y, boolean touchable) {
            return null;
        }
    }
}
