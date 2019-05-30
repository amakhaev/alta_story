package computator.utils;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.altitudeMap.TileState;
import com.alta.computator.utils.MovementRouteComputator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.tiled.TiledMap;

import java.awt.*;
import java.util.Deque;

import static org.mockito.Mockito.*;

public class MovementRouteComputatorTest {

    private AltitudeMap altitudeMap;

    /**
     * Creates the default tiled map with size 10x10
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     */
    @Before
    public void setUp() {
        TiledMap tiledMap = mock(TiledMap.class);
        when(tiledMap.getTileWidth()).thenReturn(32);
        when(tiledMap.getTileHeight()).thenReturn(32);
        when(tiledMap.getWidth()).thenReturn(10);
        when(tiledMap.getHeight()).thenReturn(10);
        when(tiledMap.getLayerIndex("backgrounds")).thenReturn(1);
        when(tiledMap.getLayerIndex("barriers")).thenReturn(-1);
        when(tiledMap.getTileId(anyInt(), anyInt(), anyInt())).thenReturn(1);

        this.altitudeMap = new AltitudeMap(tiledMap, 100, 100);
    }

    /**
     * | * * *   * * * * * * * |
     * | * * *   * * * * * * * |
     * | * * S/F * * * * * * * |
     * | * * *   * * * * * * * |
     * | * * *   * * * * * * * |
     * | * * *   * * * * * * * |
     * | * * *   * * * * * * * |
     * | * * *   * * * * * * * |
     * | * * *   * * * * * * * |
     * | * * *   * * * * * * * |
     */
    @Test
    public void routeComputator_equalsCoordinates_returnEmptyList() {
        Point coordinate = new Point(2, 2);
        Deque<Point> route = new MovementRouteComputator().calculateRoute(coordinate, coordinate, this.altitudeMap);

        Assert.assertNull(route);
    }

    /**
     * | S - - - - - - - - F |
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     */
    @Test
    public void routeComputator_topLineWithoutBarriers_returnCorrectRoute() {
        Point start = new Point(0, 0);
        Point finish = new Point(9, 0);
        Deque<Point> route = new MovementRouteComputator().calculateRoute(start, finish, this.altitudeMap);

        Assert.assertEquals(10, route.size());
        Assert.assertEquals(route.pollFirst(), new Point(0, 0));
        Assert.assertEquals(route.pollFirst(), new Point(1, 0));
        Assert.assertEquals(route.pollFirst(), new Point(2, 0));
        Assert.assertEquals(route.pollFirst(), new Point(3, 0));
        Assert.assertEquals(route.pollFirst(), new Point(4, 0));
        Assert.assertEquals(route.pollFirst(), new Point(5, 0));
        Assert.assertEquals(route.pollFirst(), new Point(6, 0));
        Assert.assertEquals(route.pollFirst(), new Point(7, 0));
        Assert.assertEquals(route.pollFirst(), new Point(8, 0));
        Assert.assertEquals(route.pollFirst(), new Point(9, 0));
    }

    /**
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     * | * * * * * * * * * * |
     * | S - - - - - - - - F |
     */
    @Test
    public void routeComputator_bottomLineWithoutBarriers_returnCorrectRoute() {
        Point start = new Point(0, 9);
        Point finish = new Point(9, 9);
        Deque<Point> route = new MovementRouteComputator().calculateRoute(start, finish, this.altitudeMap);

        Assert.assertEquals(10, route.size());
        Assert.assertEquals(route.pollFirst(), new Point(0, 9));
        Assert.assertEquals(route.pollFirst(), new Point(1, 9));
        Assert.assertEquals(route.pollFirst(), new Point(2, 9));
        Assert.assertEquals(route.pollFirst(), new Point(3, 9));
        Assert.assertEquals(route.pollFirst(), new Point(4, 9));
        Assert.assertEquals(route.pollFirst(), new Point(5, 9));
        Assert.assertEquals(route.pollFirst(), new Point(6, 9));
        Assert.assertEquals(route.pollFirst(), new Point(7, 9));
        Assert.assertEquals(route.pollFirst(), new Point(8, 9));
        Assert.assertEquals(route.pollFirst(), new Point(9, 9));
    }

    /**
     * | F * * * * * * * * * |
     * | | * * * * * * * * * |
     * | | * * * * * * * * * |
     * | | * * * * * * * * * |
     * | | * * * * * * * * * |
     * | | * * * * * * * * * |
     * | | * * * * * * * * * |
     * | | * * * * * * * * * |
     * | | * * * * * * * * * |
     * | S * * * * * * * * * |
     */
    @Test
    public void routeComputator_leftLineWithoutBarriers_returnCorrectRoute() {
        Point start = new Point(0, 9);
        Point finish = new Point(0, 0);
        Deque<Point> route = new MovementRouteComputator().calculateRoute(start, finish, this.altitudeMap);

        Assert.assertEquals(10, route.size());
        Assert.assertEquals(route.pollFirst(), new Point(0, 9));
        Assert.assertEquals(route.pollFirst(), new Point(0, 8));
        Assert.assertEquals(route.pollFirst(), new Point(0, 7));
        Assert.assertEquals(route.pollFirst(), new Point(0, 6));
        Assert.assertEquals(route.pollFirst(), new Point(0, 5));
        Assert.assertEquals(route.pollFirst(), new Point(0, 4));
        Assert.assertEquals(route.pollFirst(), new Point(0, 3));
        Assert.assertEquals(route.pollFirst(), new Point(0, 2));
        Assert.assertEquals(route.pollFirst(), new Point(0, 1));
        Assert.assertEquals(route.pollFirst(), new Point(0, 0));
    }

    /**
     * | * * * * * * * * * F |
     * | * * * * * * * * * | |
     * | * * * * * * * * * | |
     * | * * * * * * * * * | |
     * | * * * * * * * * * | |
     * | * * * * * * * * * | |
     * | * * * * * * * * * | |
     * | * * * * * * * * * | |
     * | * * * * * * * * * | |
     * | * * * * * * * * * S |
     */
    @Test
    public void routeComputator_rightLineWithoutBarriers_returnCorrectRoute() {
        Point start = new Point(9, 9);
        Point finish = new Point(9, 0);
        Deque<Point> route = new MovementRouteComputator().calculateRoute(start, finish, this.altitudeMap);

        Assert.assertEquals(10, route.size());
        Assert.assertEquals(route.pollFirst(), new Point(9, 9));
        Assert.assertEquals(route.pollFirst(), new Point(9, 8));
        Assert.assertEquals(route.pollFirst(), new Point(9, 7));
        Assert.assertEquals(route.pollFirst(), new Point(9, 6));
        Assert.assertEquals(route.pollFirst(), new Point(9, 5));
        Assert.assertEquals(route.pollFirst(), new Point(9, 4));
        Assert.assertEquals(route.pollFirst(), new Point(9, 3));
        Assert.assertEquals(route.pollFirst(), new Point(9, 2));
        Assert.assertEquals(route.pollFirst(), new Point(9, 1));
        Assert.assertEquals(route.pollFirst(), new Point(9, 0));
    }

    /**
     * | S - - - - - - - - - |
     * | * * * * * * * * * | |
     * | * * * * * * * * * | |
     * | * * * * * * * * * | |
     * | * * * * * * * * * | |
     * | * * * * * * * * * | |
     * | * * * * * * * * * | |
     * | * * * * * * * * * | |
     * | * * * * * * * * * | |
     * | * * * * * * * * * F |
     */
    @Test
    public void routeComputator_diagonalWithoutBarriers_returnCorrectRoute() {
        Point start = new Point(0, 0);
        Point finish = new Point(9, 9);
        Deque<Point> route = new MovementRouteComputator().calculateRoute(start, finish, this.altitudeMap);

        Assert.assertEquals(19, route.size());
        Assert.assertEquals(route.pollFirst(), new Point(0, 0));
        Assert.assertEquals(route.pollFirst(), new Point(1, 0));
        Assert.assertEquals(route.pollFirst(), new Point(2, 0));
        Assert.assertEquals(route.pollFirst(), new Point(3, 0));
        Assert.assertEquals(route.pollFirst(), new Point(4, 0));
        Assert.assertEquals(route.pollFirst(), new Point(5, 0));
        Assert.assertEquals(route.pollFirst(), new Point(6, 0));
        Assert.assertEquals(route.pollFirst(), new Point(7, 0));
        Assert.assertEquals(route.pollFirst(), new Point(8, 0));
        Assert.assertEquals(route.pollFirst(), new Point(9, 0));
        Assert.assertEquals(route.pollFirst(), new Point(9, 1));
        Assert.assertEquals(route.pollFirst(), new Point(9, 2));
        Assert.assertEquals(route.pollFirst(), new Point(9, 3));
        Assert.assertEquals(route.pollFirst(), new Point(9, 4));
        Assert.assertEquals(route.pollFirst(), new Point(9, 5));
        Assert.assertEquals(route.pollFirst(), new Point(9, 6));
        Assert.assertEquals(route.pollFirst(), new Point(9, 7));
        Assert.assertEquals(route.pollFirst(), new Point(9, 8));
        Assert.assertEquals(route.pollFirst(), new Point(9, 9));
    }

    /**
     * | - - - - - - - - - F |
     * | | * * * * * * * * * |
     * | | * * * * * * * * * |
     * | | * * * * * * * * * |
     * | | * * * * * * * * * |
     * | | * * * * * * * * * |
     * | | * * * * * * * * * |
     * | | * * * * * * * * * |
     * | | * * * * * * * * * |
     * | S * * * * * * * * * |
     */
    @Test
    public void routeComputator_diagonal2WithoutBarriers_returnCorrectRoute() {
        Point start = new Point(0, 9);
        Point finish = new Point(9, 0);
        Deque<Point> route = new MovementRouteComputator().calculateRoute(start, finish, this.altitudeMap);

        Assert.assertEquals(19, route.size());
        Assert.assertEquals(route.pollFirst(), new Point(0, 9));
        Assert.assertEquals(route.pollFirst(), new Point(0, 8));
        Assert.assertEquals(route.pollFirst(), new Point(0, 7));
        Assert.assertEquals(route.pollFirst(), new Point(0, 6));
        Assert.assertEquals(route.pollFirst(), new Point(0, 5));
        Assert.assertEquals(route.pollFirst(), new Point(0, 4));
        Assert.assertEquals(route.pollFirst(), new Point(0, 3));
        Assert.assertEquals(route.pollFirst(), new Point(0, 2));
        Assert.assertEquals(route.pollFirst(), new Point(0, 1));
        Assert.assertEquals(route.pollFirst(), new Point(0, 0));
        Assert.assertEquals(route.pollFirst(), new Point(1, 0));
        Assert.assertEquals(route.pollFirst(), new Point(2, 0));
        Assert.assertEquals(route.pollFirst(), new Point(3, 0));
        Assert.assertEquals(route.pollFirst(), new Point(4, 0));
        Assert.assertEquals(route.pollFirst(), new Point(5, 0));
        Assert.assertEquals(route.pollFirst(), new Point(6, 0));
        Assert.assertEquals(route.pollFirst(), new Point(7, 0));
        Assert.assertEquals(route.pollFirst(), new Point(8, 0));
        Assert.assertEquals(route.pollFirst(), new Point(9, 0));
    }

    /**
     * | * * * * * * * * * * |
     * | * * * * * X * * * * |
     * | * * * * * X * * * * |
     * | * * * * * X * * * * |
     * | * * * * * X * * * * |
     * | * * S - - X - - F * |
     * | * * * * | X | * * * |
     * | * * * * | X | * * * |
     * | * * * * | X | * * * |
     * | * * * * - - - * * * |
     */
    @Test
    public void routeComputator_wall1_returnCorrectRoute() {
        Point start = new Point(2, 5);
        Point finish = new Point(8, 5);
        this.altitudeMap.setTileState(5, 1, TileState.BARRIER);
        this.altitudeMap.setTileState(5, 2, TileState.BARRIER);
        this.altitudeMap.setTileState(5, 3, TileState.BARRIER);
        this.altitudeMap.setTileState(5, 4, TileState.BARRIER);
        this.altitudeMap.setTileState(5, 5, TileState.BARRIER);
        this.altitudeMap.setTileState(5, 6, TileState.BARRIER);
        this.altitudeMap.setTileState(5, 7, TileState.BARRIER);
        this.altitudeMap.setTileState(5, 8, TileState.BARRIER);

        Deque<Point> route = new MovementRouteComputator().calculateRoute(start, finish, this.altitudeMap);

        Assert.assertEquals(15, route.size());
        Assert.assertEquals(route.pollFirst(), new Point(2, 5));
        Assert.assertEquals(route.pollFirst(), new Point(3, 5));
        Assert.assertEquals(route.pollFirst(), new Point(4, 5));
        Assert.assertEquals(route.pollFirst(), new Point(4, 6));
        Assert.assertEquals(route.pollFirst(), new Point(4, 7));
        Assert.assertEquals(route.pollFirst(), new Point(4, 8));
        Assert.assertEquals(route.pollFirst(), new Point(4, 9));
        Assert.assertEquals(route.pollFirst(), new Point(5, 9));
        Assert.assertEquals(route.pollFirst(), new Point(6, 9));
        Assert.assertEquals(route.pollFirst(), new Point(6, 8));
        Assert.assertEquals(route.pollFirst(), new Point(6, 7));
        Assert.assertEquals(route.pollFirst(), new Point(6, 6));
        Assert.assertEquals(route.pollFirst(), new Point(6, 5));
        Assert.assertEquals(route.pollFirst(), new Point(7, 5));
        Assert.assertEquals(route.pollFirst(), new Point(8, 5));
    }

    /**
     * | * * * * * * * * * * |
     * | * * * X X X * * * * |
     * | * * * * * X * * * * |
     * | * * * * * X * * * * |
     * | * * * * * X * * * * |
     * | * * F * * X * * S * |
     * | * * * * * X * X X X |
     * | * * * * * X * X * * |
     * | * * * * * X X X * * |
     * | * * * * * * * * * * |
     */
    @Test
    public void routeComputator_wall2_returnCorrectRoute() {
        Point start = new Point(8, 5);
        Point finish = new Point(2, 5);
        this.altitudeMap.setTileState(3, 1, TileState.BARRIER);
        this.altitudeMap.setTileState(4, 1, TileState.BARRIER);
        this.altitudeMap.setTileState(5, 1, TileState.BARRIER);
        this.altitudeMap.setTileState(5, 2, TileState.BARRIER);
        this.altitudeMap.setTileState(5, 3, TileState.BARRIER);
        this.altitudeMap.setTileState(5, 4, TileState.BARRIER);
        this.altitudeMap.setTileState(5, 5, TileState.BARRIER);
        this.altitudeMap.setTileState(5, 6, TileState.BARRIER);
        this.altitudeMap.setTileState(5, 7, TileState.BARRIER);
        this.altitudeMap.setTileState(5, 8, TileState.BARRIER);
        this.altitudeMap.setTileState(6, 8, TileState.BARRIER);
        this.altitudeMap.setTileState(7, 8, TileState.BARRIER);
        this.altitudeMap.setTileState(7, 7, TileState.BARRIER);
        this.altitudeMap.setTileState(7, 6, TileState.BARRIER);
        this.altitudeMap.setTileState(8, 6, TileState.BARRIER);
        this.altitudeMap.setTileState(9, 6, TileState.BARRIER);

        Deque<Point> route = new MovementRouteComputator().calculateRoute(start, finish, this.altitudeMap);

        Assert.assertEquals(17, route.size());
        Assert.assertEquals(route.pollFirst(), new Point(8, 5));
        Assert.assertEquals(route.pollFirst(), new Point(7, 5));
        Assert.assertEquals(route.pollFirst(), new Point(6, 5));
        Assert.assertEquals(route.pollFirst(), new Point(6, 4));
        Assert.assertEquals(route.pollFirst(), new Point(6, 3));
        Assert.assertEquals(route.pollFirst(), new Point(6, 2));
        Assert.assertEquals(route.pollFirst(), new Point(6, 1));
        Assert.assertEquals(route.pollFirst(), new Point(6, 0));
        Assert.assertEquals(route.pollFirst(), new Point(5, 0));
        Assert.assertEquals(route.pollFirst(), new Point(4, 0));
        Assert.assertEquals(route.pollFirst(), new Point(3, 0));
        Assert.assertEquals(route.pollFirst(), new Point(2, 0));
        Assert.assertEquals(route.pollFirst(), new Point(2, 1));
        Assert.assertEquals(route.pollFirst(), new Point(2, 2));
        Assert.assertEquals(route.pollFirst(), new Point(2, 3));
        Assert.assertEquals(route.pollFirst(), new Point(2, 4));
        Assert.assertEquals(route.pollFirst(), new Point(2, 5));
    }

    /**
     * | * * * * * X * * * * |
     * | * * * * * X * * * * |
     * | * * * * * X * * * * |
     * | * * * * * X * * * * |
     * | * * * * * X * * * * |
     * | * * S * * X * * F * |
     * | * * * * * X * * * * |
     * | * * * * * X * * * * |
     * | * * * * * X * * * * |
     * | * * * * - X - * * * |
     */
    @Test
    public void routeComputator_wall3_returnCorrectRoute() {
        Point start = new Point(2, 5);
        Point finish = new Point(8, 5);
        this.altitudeMap.setTileState(5, 0, TileState.BARRIER);
        this.altitudeMap.setTileState(5, 1, TileState.BARRIER);
        this.altitudeMap.setTileState(5, 2, TileState.BARRIER);
        this.altitudeMap.setTileState(5, 3, TileState.BARRIER);
        this.altitudeMap.setTileState(5, 4, TileState.BARRIER);
        this.altitudeMap.setTileState(5, 5, TileState.BARRIER);
        this.altitudeMap.setTileState(5, 6, TileState.BARRIER);
        this.altitudeMap.setTileState(5, 7, TileState.BARRIER);
        this.altitudeMap.setTileState(5, 8, TileState.BARRIER);
        this.altitudeMap.setTileState(5, 9, TileState.BARRIER);

        Deque<Point> route = new MovementRouteComputator().calculateRoute(start, finish, this.altitudeMap);

        Assert.assertNull(route);
    }
}
