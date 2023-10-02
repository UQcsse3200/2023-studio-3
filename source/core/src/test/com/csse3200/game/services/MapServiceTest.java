package com.csse3200.game.services;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;

import com.csse3200.game.entities.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;

public class MapServiceTest {

    private TerrainFactory mockTerrainFactory;
    private TerrainComponent mockTerrainComponent;
    private Entity mockEntity;


    @BeforeEach
    public void setUp() {
        mockTerrainFactory = mock(TerrainFactory.class);
        mockTerrainComponent = mock(TerrainComponent.class);
        mockEntity = mock(Entity.class);


        when(mockTerrainFactory.createTerrain(TerrainFactory.TerrainType.ALL_DEMO)).thenReturn(mockTerrainComponent);
        when(mockEntity.getComponent(TerrainComponent.class)).thenReturn(mockTerrainComponent);

    }

    @Test
    public void testConstructor() {
        MapService mapService = new MapService(mockEntity, mockTerrainFactory);

        assertEquals(mockTerrainComponent, mapService.getComponent());

    }

    @Test
    public void testGetHeight() {
        when(mockTerrainComponent.getMapBounds(0)).thenReturn(new GridPoint2(5,2)); // Example Point class to represent x and y
        MapService mapService = new MapService(mockEntity, mockTerrainFactory);

        assertEquals(2, mapService.getHeight());
    }

    @Test
    public void testGetWidth() {
        when(mockTerrainComponent.getMapBounds(0)).thenReturn(new GridPoint2(5, 10)); // Example Point class to represent x and y
        MapService mapService = new MapService(mockEntity, mockTerrainFactory);

        assertEquals(5, mapService.getWidth());
    }

}
