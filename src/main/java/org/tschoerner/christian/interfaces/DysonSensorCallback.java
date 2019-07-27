package org.tschoerner.christian.interfaces;

import org.tschoerner.christian.methods.DysonSensor;

public interface DysonSensorCallback {
    void onSensorDataReceived(DysonSensor dysonSensor);
}
