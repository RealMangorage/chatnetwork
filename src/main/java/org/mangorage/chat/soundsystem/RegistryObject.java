package org.mangorage.chat.soundsystem;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class RegistryObject<Sound> {
    private final Supplier<Sound> supplier;


    public RegistryObject(Supplier<Sound> supplier) {
        this.supplier = supplier;
    }

    public Sound get() {
        return supplier.get();
    }

}
