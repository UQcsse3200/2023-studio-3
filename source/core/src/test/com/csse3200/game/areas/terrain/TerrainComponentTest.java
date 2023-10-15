package com.csse3200.game.areas.terrain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.csse3200.game.currency.Scrap;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import static org.mockito.Mockito.*;

import com.csse3200.game.screens.TowerType;
import com.csse3200.game.services.CurrencyService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class TerrainComponentTest {

  private TerrainComponent component;
  private TiledMapTile mockTile;

  @BeforeEach
  public void setUp() {
    component = makeComponent(TerrainOrientation.ORTHOGONAL, 1f);

    // Mock Gdx input to return specific mouse position
    Gdx.input = mock(Input.class);
    when(Gdx.input.getX()).thenReturn(2);
    when(Gdx.input.getY()).thenReturn(4);


    MapLayers mockLayers = mock(MapLayers.class);
    when(component.getMap().getLayers()).thenReturn(mockLayers);

    TiledMapTileLayer mockTileLayer = mock(TiledMapTileLayer.class);
    when(mockLayers.get(0)).thenReturn(mockTileLayer);

    TiledMapTileLayer.Cell mockCell = mock(TiledMapTileLayer.Cell.class);
    when(mockTileLayer.getCell(2, 4)).thenReturn(mockCell);

    mockTile = mock(TiledMapTile.class);
    when(mockCell.getTile()).thenReturn(mockTile);

  }
  @Test
  void shouldConvertPositionOrthogonal() {
    TerrainComponent component = makeComponent(TerrainOrientation.ORTHOGONAL, 3f);
    assertEquals(new Vector2(0f, 0f), component.tileToWorldPosition(0, 0));
    assertEquals(new Vector2(6f, 12f), component.tileToWorldPosition(2, 4));
    assertEquals(new Vector2(-15f, -9f), component.tileToWorldPosition(-5, -3));
  }

  @Test
  void shouldConvertPositionIsometric() {
    TerrainComponent component = makeComponent(TerrainOrientation.ISOMETRIC, 3f);
    assertEquals(new Vector2(0f, 0f), component.tileToWorldPosition(0, 0));
    assertEquals(new Vector2(9f, 3f), component.tileToWorldPosition(2, 4));
    assertEquals(new Vector2(-12f, 3f), component.tileToWorldPosition(-5, -3));
  }

  @Test
  void shouldConvertPositionHexagonal() {
    TerrainComponent component = makeComponent(TerrainOrientation.HEXAGONAL, 3f);
  }

  @Test
  void shouldHighlightTileGreenOnHover() {

    Texture mockTexture = mock(Texture.class);
    ServiceLocator.registerResourceService(mock(ResourceService.class));

    ServiceLocator.registerCurrencyService(mock(CurrencyService.class));
    when(ServiceLocator.getCurrencyService().getTower()).thenReturn(TowerType.WEAPON);

    ServiceLocator.registerEntityService(mock(EntityService.class));
    when(ServiceLocator.getEntityService().entitiesInTile(2,4)).thenReturn(Boolean.FALSE);

    when(ServiceLocator.getCurrencyService().getScrap()).thenReturn(mock(Scrap.class));
    when(ServiceLocator.getCurrencyService().getScrap().canBuy(Integer.parseInt(ServiceLocator.getCurrencyService().getTower().getPrice()))).thenReturn(Boolean.TRUE);
    // The tile should turn green since it meets the requirement
    when(ServiceLocator.getResourceService().getAsset("images/green_tile.png", Texture.class))
            .thenReturn(mockTexture);

    component.hoverHighlight();
    // Verify that the tile's texture region was changed
    verify(mockTile).setTextureRegion(any(TextureRegion.class));
  }

  @Test
  void shouldHighlightTileRedOnHover() {

    Texture mockTexture = mock(Texture.class);
    ServiceLocator.registerResourceService(mock(ResourceService.class));

    ServiceLocator.registerCurrencyService(mock(CurrencyService.class));
    when(ServiceLocator.getCurrencyService().getTower()).thenReturn(TowerType.TNT);

    ServiceLocator.registerEntityService(mock(EntityService.class));
    when(ServiceLocator.getEntityService().entitiesInTile(2,4)).thenReturn(Boolean.FALSE);

    when(ServiceLocator.getCurrencyService().getScrap()).thenReturn(mock(Scrap.class));
    when(ServiceLocator.getCurrencyService().getScrap().canBuy(Integer.parseInt(ServiceLocator.getCurrencyService().getTower().getPrice()))).thenReturn(Boolean.FALSE);
    // The tile should turn red since we can't afford the tower
    when(ServiceLocator.getResourceService().getAsset("images/red_tile.png", Texture.class))
            .thenReturn(mockTexture);

    component.hoverHighlight();
    // Verify that the tile's texture region was changed
    verify(mockTile).setTextureRegion(any(TextureRegion.class));
  }

  private static TerrainComponent makeComponent(TerrainOrientation orientation, float tileSize) {
    OrthographicCamera camera = mock(OrthographicCamera.class);
    TiledMap map = mock(TiledMap.class);
    TiledMapRenderer mapRenderer = mock(TiledMapRenderer.class);
    return new TerrainComponent(camera, map, mapRenderer, orientation, tileSize);
  }
}
