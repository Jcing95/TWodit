package de.jcing.engine.io;

import lombok.Data;

@Data
public class MouseEvent {

    private int button;
    private int action;
    private int mods; 

}