package org.example.demo.services;

import org.example.demo.dao.OhmCalculationDao;
import org.example.demo.entities.OhmCalculation;

import java.util.List;

public class OhmCalculatorService {
    private final OhmCalculationDao calculationDao = new OhmCalculationDao();

    public OhmCalculation calculate(OhmCalculation calculation, String username) {
        if (calculation.getVoltage() == null) {
            calculation.setVoltage(calculation.getCurrent() * calculation.getResistance());
            calculation.setCalculationType("V");
        } else if (calculation.getCurrent() == null) {
            calculation.setCurrent(calculation.getVoltage() / calculation.getResistance());
            calculation.setCalculationType("I");
        } else {
            calculation.setResistance(calculation.getVoltage() / calculation.getCurrent());
            calculation.setCalculationType("R");
        }

        calculation.setTimestamp(java.time.LocalDateTime.now());
        calculation.setUsername(username);

        calculationDao.save(calculation);
        return calculation;
    }

    public List<OhmCalculation> getHistory(String username) {
        return calculationDao.getHistory(username);
    }
}