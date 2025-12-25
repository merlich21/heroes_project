/*

 * Heroes project
 *
 * Version information
 *
 * 25.12.2025
 *
 * author: Skotnikov Alexander
 */


package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.EdgeDistance;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * UnitTargetPathFinderImpl class
 *
 * Реализация интерфейса UnitTargetPathFinder
 *
 *
 * @version
1.6 25 Dec 2025  * @author
Skotnikov Alexander  */
public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    private static final int FIELD_WIDTH = 27;
    private static final int FIELD_HEIGHT = 21;
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    /**
     * Метод определяет кратчайший маршрут между атакующим и атакуемым юнитом и возвращает его в виде списка объектов
     * содержащих координаты каждой точки данного кратчайшего пути.
     *
     * @param attackUnit       юнит, который атакует.
     * @param targetUnit       юнит, который подвергается атаке.
     * @param existingUnitList список всех существующих юнитов.
     *
     * @return Cписок объектов Edge, т.е. координат клеток пути от атакующего юнита до атакуемого юнита включительно.
     *         Если маршрут не найден — возвращает пустой список.
     */
    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {

        int[][] distances = initDistances();
        boolean[][] visited = new boolean[FIELD_WIDTH][FIELD_HEIGHT];
        Edge[][] predecessors = new Edge[FIELD_WIDTH][FIELD_HEIGHT];
        Set<String> blockedCells = getBlockedCells(existingUnitList, attackUnit, targetUnit);

        PriorityQueue<EdgeDistance> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(EdgeDistance::getDistance));
        addStartPointToQueue(priorityQueue, attackUnit, distances);

        EdgeDistance current;

        while (!priorityQueue.isEmpty()) {
            current = priorityQueue.poll();
            if (visited[current.getX()][current.getY()]) {
                continue;
            }
            visited[current.getX()][current.getY()] = true;

            if (isTargetReached(current, targetUnit)) {
                break;
            }

            updateNeighbours(current, blockedCells, distances, predecessors, priorityQueue);
        }

        return buildPath(predecessors, targetUnit);
    }

    /**
     * Метод "initDistances" производит инициализацию массива расстояний.
     */
    private int[][] initDistances() {

        int[][] distances = new int[FIELD_WIDTH][FIELD_HEIGHT];

        for (int[] row : distances) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        return distances;
    }

    /**
     * Метод "getBlockedCells" помогает получить список заблокированных клеток
     */
    private Set<String> getBlockedCells(List<Unit> existingUnitList, Unit attackUnit, Unit targetUnit) {

        HashSet<String> blockedCells = new HashSet<String>();

        for (Unit unit : existingUnitList) {
            if (unit.isAlive() && unit != attackUnit && unit != targetUnit) {
                blockedCells.add(unit.getxCoordinate() + "," + unit.getyCoordinate());
            }
        }
        return blockedCells;
    }

    /**
     * Метод "addStartPointToQueue" добавляет начальную точку в очередь
     */
    private void addStartPointToQueue(Queue<EdgeDistance> priorityQueue, Unit attackUnit, int[][] distances) {
        int x = attackUnit.getxCoordinate();
        int y = attackUnit.getyCoordinate();
        distances[x][y] = 0;

        priorityQueue.offer(new EdgeDistance(x, y, 0));
    }

    /**
     * Метод "isTargetReached" производит проверку достижения цели
     */
    private boolean isTargetReached(EdgeDistance current, Unit targetUnit) {
        return current.getX() == targetUnit.getxCoordinate() && current.getY() == targetUnit.getyCoordinate();
    }

    /**
     * Метод "updateNeighbours" производит обновление соседних клеток
     */
    private void updateNeighbours(EdgeDistance current, Set<String> blockedCells,
                                  int[][] distances, Edge[][] predecessors, Queue<EdgeDistance> priorityQueue) {
        for (int[] direction : DIRECTIONS) {

            int neighborX = current.getX() + direction[0];
            int neighborY = current.getY() + direction[1];
            int newDistance = 0;

            if (isValidCell(neighborX, neighborY, blockedCells)) {

                newDistance = distances[current.getX()][current.getY()] + 1;
                if (newDistance < distances[neighborX][neighborY]) {
                    distances[neighborX][neighborY] = newDistance;
                    predecessors[neighborX][neighborY] = new Edge(current.getX(), current.getY());
                    priorityQueue.offer(new EdgeDistance(neighborX, neighborY, newDistance));
                }
            }
        }
    }

    /**
     * Метод "isValidCell" производит проверку валидности клетки
     */
    private boolean isValidCell(int x, int y, Set<String> blockedCells) {
        return x >= 0 && x < FIELD_WIDTH && y >= 0 && y < FIELD_HEIGHT && !blockedCells.contains(x + "," + y);
    }

    /**
     * Метод "buildPath" собирает путь
     */
    private List<Edge> buildPath(Edge[][] cameFrom, Unit targetUnit) {

        ArrayDeque<Edge> path = new ArrayDeque<Edge>();
        int x = targetUnit.getxCoordinate();
        int y = targetUnit.getyCoordinate();
        Edge predecessor;

        while (true) {
            predecessor = cameFrom[x][y];
            if (predecessor == null) {
                break;
            }
            path.push(new Edge(x, y));
            x = predecessor.getX();
            y = predecessor.getY();
        }

        if (!path.isEmpty()) {
            path.pop();
        }

        return new ArrayList<>(path);
    }
}
