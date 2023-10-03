package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TerrainFactoryTest {

    private TerrainFactory terrainFactory;
    private CameraComponent mockedCameraComponent;
    private OrthogonalTiledMapRenderer mockedRenderer;
    private ResourceService mockResourceService;
    private Texture mockTexture;

    @BeforeEach
    public void setUp() {
        // Create mocks
        mockedCameraComponent = mock(CameraComponent.class);
        mockedRenderer = mock(OrthogonalTiledMapRenderer.class);
        mockResourceService = mock(ResourceService.class);
        mockTexture = mock(Texture.class);


        ServiceLocator.registerResourceService(mockResourceService);


        OrthographicCamera mockedCamera = mock(OrthographicCamera.class);
        when(mockedCameraComponent.getCamera()).thenReturn(mockedCamera);


        terrainFactory = spy(new TerrainFactory(mockedCameraComponent));


        // When createRenderer is called on terrainFactory return the mockedRenderer
        doReturn(mockedRenderer).when(terrainFactory).createRenderer(any(TiledMap.class), anyFloat());
    }

    @Test
    public void testCreateTerrainGeneral() {
        // Given the texture is taken from the ResourceService
        when(mockResourceService.getAsset("images/terrain_use.png", Texture.class)).thenReturn(mockTexture);


        TerrainComponent terrainComponent = terrainFactory.createTerrain(TerrainFactory.TerrainType.ALL_DEMO);
        TiledMapTileLayer layer = (TiledMapTileLayer) terrainComponent.getMap().getLayers().get(0);

        //  the terrainComponent should not be null
        assertNotNull(terrainComponent, "TerrainComponent should not be null");
        assertEquals(6,layer.getHeight()); // 6 lanes
        assertEquals(20,layer.getWidth()); // 20 tiles per lane

    }
}
