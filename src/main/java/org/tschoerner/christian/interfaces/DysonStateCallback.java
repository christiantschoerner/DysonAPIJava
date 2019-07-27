package org.tschoerner.christian.interfaces;

import org.tschoerner.christian.methods.DysonState;

public interface DysonStateCallback {

    void onStateReceived(DysonState dysonState);
}
