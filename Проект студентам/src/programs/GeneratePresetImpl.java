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

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * GeneratePresetImpl class
 *
 * Реализация интерфейса GeneratePreset
 *
 *
 * @version
1.6 25 Dec 2025  * @author
Skotnikov Alexander  */
public class GeneratePresetImpl implements GeneratePreset {

    public static final int UNITS_TYPE_LIMIT = 11;
    public static final int ARMY_COLUMNS = 21;
    public static final int ARMY_ROWS = 3;

    private final Random random = new Random();

    /**
     * Метод "generate" формирует пресет армии компьютера,
     * то есть максимально эффективный по соотношению атаки к стоимости в первую очередь
     * и соотношению здоровья к стоимости набора юнитов разного типа таким образом,
     * чтобы при этом соблюдалось ограничение в 11 юнитов каждого типа.
     *
     * @param unitList  список юнитов, содержит объект юнита каждого типа. На его основе происходит заполнение армии компьютера.
     * @param maxPoints максимальное число очков в сумме для всех юнитов армии.
     * @return возвращает объект армии компьютера со списком юнитов внутри неё.
     */
    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {

        Army army = new Army();
        int[] unitCount = new int[unitList.size()];
        ArrayList<Unit> generatedUnits = new ArrayList<Unit>();
        HashSet<Coordinate> occupiedCoordinates = new HashSet<Coordinate>();

        unitList.sort(Comparator.comparingDouble(unit -> -((double) (unit.getBaseAttack() + unit.getHealth()) / unit.getCost())));

        int totalPoints = 0;

        for (Unit unit : unitList) {
            while (unitCount[unitList.indexOf(unit)] < UNITS_TYPE_LIMIT && totalPoints + unit.getCost() <= maxPoints) {
                addUnit(unit, generatedUnits, occupiedCoordinates, unitCount[unitList.indexOf(unit)]);
                unitCount[unitList.indexOf(unit)]++; // увеличиваем счетчик
                totalPoints += unit.getCost();
            }
        }

        army.setUnits(generatedUnits);
        army.setPoints(totalPoints);

        return army;
    }

    /**
     * Метод "addUnit" добавляет юнит в армию противника.
     *
     * @param unit юнит, который нужно добавить в армию противника
     * @param generatedUnits список юнитов армии противника
     * @param occupiedCoordinates список занятых клеток на поле для расстановки армии
     * @param index порядковый номер юнита этого типа
     */
    private void addUnit(Unit unit, ArrayList<Unit> generatedUnits, Set<Coordinate> occupiedCoordinates, int index) {

        Coordinate coordinate = new Coordinate(random.nextInt(ARMY_ROWS), random.nextInt(ARMY_COLUMNS));

        while (occupiedCoordinates.contains(coordinate)) {
            coordinate.setxCoordinate(random.nextInt(ARMY_ROWS));
            coordinate.setyCoordinate(random.nextInt(ARMY_COLUMNS));
        }

        occupiedCoordinates.add(coordinate);

        Unit newUnit = new Unit(unit.getUnitType() + " " + index,
                unit.getUnitType(),
                unit.getHealth(),
                unit.getBaseAttack(),
                unit.getCost(),
                unit.getAttackType(),
                unit.getAttackBonuses(),
                unit.getDefenceBonuses(),
                coordinate.getxCoordinate(),
                coordinate.getyCoordinate());

        generatedUnits.add(newUnit);
    }

    /**
     * "Coordinate class" - класс с координатами, используется для определения координат юнита.
     */
    private static class Coordinate {

        private int xCoordinate;
        private int yCoordinate;

        Coordinate(int xCoordinate, int yCoordinate) {
            this.xCoordinate = xCoordinate;
            this.yCoordinate = yCoordinate;
        }

        public int getxCoordinate() {
            return xCoordinate;
        }
        public int getyCoordinate() {
            return yCoordinate;
        }
        public void setxCoordinate(int xCoordinate) {
            this.xCoordinate = xCoordinate;
        }
        public void setyCoordinate(int yCoordinate) {
            this.yCoordinate = yCoordinate;
        }
    }
}
