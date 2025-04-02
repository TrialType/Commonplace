package Commonplace.Utils.Classes;

import arc.struct.Seq;

import static mindustry.Vars.state;

public abstract class GameChangeListener {
    private static final short MENU = 1, PLAYING = 1 << 1, EDITOR = 1 << 2, SANDBOX = 1 << 3, PAUSED = 1 << 4, SECTOR = 1 << 5;
    private static short lastState = MENU;
    private static final Seq<Active> listeners = new Seq<>();

    public static void update() {
        if (state.isPlaying()) {
            if ((lastState & MENU) == 0) {
                lastState += MENU;
                listeners.each(a -> a.menu.run());
            }
        } else {
            lastState ^= MENU;
        }
        if (state.isEditor()) {
            if ((lastState & PLAYING) == 0) {
                lastState += PLAYING;
                listeners.each(a -> a.playing.run());
            }
        } else {
            lastState ^= PLAYING;
        }
        if (state.isEditor()) {
            if ((lastState & EDITOR) == 0) {
                lastState += EDITOR;
                listeners.each(a -> a.editor.run());
            }
        } else {
            lastState ^= EDITOR;
        }
        if (state.isEditor()) {
            if ((lastState & SANDBOX) == 0) {
                lastState += SANDBOX;
                listeners.each(a -> a.sandbox.run());
            }
        } else {
            lastState ^= SANDBOX;
        }
        if (state.isEditor()) {
            if ((lastState & PAUSED) == 0) {
                lastState += PAUSED;
                listeners.each(a -> a.paused.run());
            }
        } else {
            lastState ^= PAUSED;
        }
        if (state.isEditor()) {
            if ((lastState & SECTOR) == 0) {
                lastState += SECTOR;
                listeners.each(a -> a.sector.run());
            }
        } else {
            lastState ^= SECTOR;
        }
    }

    public static void addListener(Runnable menu, Runnable playing, Runnable editor, Runnable sandbox, Runnable paused, Runnable sector) {
        listeners.add(new Active(menu, playing, editor, sandbox, paused, sector));
    }

    static class Active {
        Runnable menu;
        Runnable playing;
        Runnable editor;
        Runnable sandbox;
        Runnable paused;
        Runnable sector;

        public Active(Runnable menu, Runnable playing, Runnable editor, Runnable sandbox, Runnable paused, Runnable sector) {
            this.menu = menu;
            this.playing = playing;
            this.editor = editor;
            this.sandbox = sandbox;
            this.paused = paused;
            this.sector = sector;
        }
    }
}
