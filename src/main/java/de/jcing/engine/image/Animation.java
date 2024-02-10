package de.jcing.engine.image;

import de.jcing.game.Game;

public class Animation extends Image {

    public static final int DEFAULT_WAIT_MILLIS = 100;

    private final int startIndex;
    private final int length;
    private final int millisWait;

    public Animation(TextureAtlas atlas, int startIndex, int length, int millisWait) {
        super(atlas, startIndex);
        this.startIndex = startIndex;
        this.length = length;
        this.millisWait = millisWait;
    }

    public Animation(TextureAtlas atlas, int startIndex, int length) {
        this(atlas, startIndex, length, DEFAULT_WAIT_MILLIS);
    }

    public void update() {
        index = startIndex + (Game.time() / millisWait) % length;
    }

    public int getLength() {
        return length;
    }

    public void reset() {
        index = startIndex;
    }

}
