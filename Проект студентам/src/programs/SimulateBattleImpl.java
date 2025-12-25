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
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * SimulateBattleImpl class
 *
 * Реализация интерфейса SimulateBattle
 *
 *
 * @version
1.6 25 Dec 2025  * @author
Skotnikov Alexander  */
public class SimulateBattleImpl implements SimulateBattle {

    private PrintBattleLog printBattleLog;

    /**
     * Метод "simulate" осуществляет симуляцию боя между армией игрока и армией компьютера.
     *
     * @param computerArmy объект армии компьютера, содержащий список её юнитов.
     * @param playerArmy   объект армии игрока, содержащий список её юнитов.
     */
    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {

        ArrayList<Unit> playerUnits = new ArrayList<>(playerArmy.getUnits());
        ArrayList<Unit> computerUnits = new ArrayList<>(computerArmy.getUnits());

        while (true) {
            playerUnits.sort(Comparator.comparingInt(unit -> -unit.getBaseAttack()));
            computerUnits.sort(Comparator.comparingInt(unit -> -unit.getBaseAttack()));

            if (playerUnits.isEmpty() || computerUnits.isEmpty()) {
                break;
            }

            goToAttack(playerUnits);
            goToAttack(computerUnits);
        }
    }


    /**
     * Метод "goToAttack" обеспечивает атаку юнитов.
     */
    private void goToAttack(List<Unit> attackingUnits) throws InterruptedException {

        for (Iterator<Unit> iterator = attackingUnits.iterator(); iterator.hasNext(); ) {
            Unit attackingUnit = iterator.next();
            if (!attackingUnit.isAlive()) {
                iterator.remove();
                continue;
            }
            Unit target = attackingUnit.getProgram().attack();
            if (target != null) {
                printBattleLog.printBattleLog(attackingUnit, target);
            }
        }
    }
}
