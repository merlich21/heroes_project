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
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

/**
 * SuitableForAttackUnitsFinderImpl class
 *
 * Реализация интерфейса SuitableForAttackUnitsFinder
 *
 *
 * @version
1.6 25 Dec 2025  * @author
Skotnikov Alexander  */
public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    /**
     * Метод "getSuitableUnits" определяет список юнитов, подходящих для атаки, для атакующего юнита одной из армий.
     *
     * @param unitsByRow       трехслойный массив юнитов противника.
     *                         Для юнита из атакующей армии компьютера эти юниты находятся на координатах 24..26 по оси x.
     *                         Для армии игрока они располагаются на координатах 0..2 по оси x
     *                         (фактически, это юниты армии компьютера).
     * @param isLeftArmyTarget параметр, указывающий, юниты какой армии подвергаются атаке.
     *                         Если значение true, то атаке подвергаются юниты армии компьютера (левая армия);
     *                         если false — юниты армии игрока (правая армия).
     * @return возвращает список юнитов, подходящих для атаки, для юнита атакующей армии.
     */
    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {

        ArrayList<Unit> result = new ArrayList<Unit>();
        List<Unit> suitableUnits;

        for (List<Unit> row : unitsByRow) {
            suitableUnits = extractSuitableUnits(row, isLeftArmyTarget);
            result.addAll(suitableUnits);
        }

        return result;
    }

    /**
     * Метод "extractSuitableUnits" помогает получить подходящие для атаки юниты в ряду.
     */
    private List<Unit> extractSuitableUnits(List<Unit> row, boolean isLeftArmyTarget) {

        ArrayList<Unit> suitableUnits = new ArrayList<Unit>();
        Unit unit;

        for (int i = 0; i < row.size(); i++) {
            unit = row.get(i);

            if (unit != null && unit.isAlive() &&
                    (isLeftArmyTarget ? isFirstUnitFromLeft(row, i) : isLastUnitFromRight(row, i))) {
                suitableUnits.add(unit);
            }
        }

        return suitableUnits;
    }

    /**
     * Метод "isFirstUnitFromLeft" проверяет, является ли юнит первым слева в ряду.
     */
    private boolean isFirstUnitFromLeft(List<Unit> row, int index) {
        return index == 0 || !row.subList(0, index).contains(null);
    }

    /**
     * Метод "isLastUnitFromRight" проверяет, является ли юнит последним справа в ряду.
     */
    private boolean isLastUnitFromRight(List<Unit> row, int index) {
        return index == row.size() - 1 || !row.subList(index + 1, row.size()).contains(null);
    }
}
