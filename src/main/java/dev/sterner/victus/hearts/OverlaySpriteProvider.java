package dev.sterner.victus.hearts;

public interface OverlaySpriteProvider {

    int getOverlayTint();

    int getOverlayIndex();

    default boolean shouldRenderOverlay() {
        return true;
    }
}
