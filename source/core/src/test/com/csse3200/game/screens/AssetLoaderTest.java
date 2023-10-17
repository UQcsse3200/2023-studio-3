package com.csse3200.game.screens;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class AssetLoaderTest {

    @Test
    void testAreAllAssetsLoaded() {
        assertFalse(AssetLoader.areAllAssetsLoaded());
    }
}

