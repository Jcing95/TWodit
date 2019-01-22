package de.jcing.window.gui.animator;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;

import de.jcing.util.Util;
import de.jcing.window.gui.Component;
import de.jcing.window.gui.Component.DrawHook;
import de.jcing.window.gui.utillities.Group;

public class Fader extends Animator {

	public static final int ALPHA = 11;

	protected int compositeType;
	protected float startValue, endValue;

	protected float currentValue;

	protected DrawHook faderHook;

	public Fader(Group group, int composite, float start, float end) {
		super(group);
		startValue = start;
		endValue = end;
		compositeType = composite;
		currentValue = start;
		faderHook = new DrawHook() {
			public void draw(Graphics2D g, Component c) {
				Composite composite;
				switch (compositeType) {
				case ALPHA:
					composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,currentValue);
					break;
				default:
					composite = g.getComposite();
				}
				g.setComposite(composite);
				Component.DEFAULT_HOOK.draw(g, c);
			}
		};
	}

	public Fader reverse() {
		float tmp = startValue;
		startValue = endValue;
		endValue = tmp;
		return this;
	}
	
	@Override
	protected void animate(Component c, int tick, double of) {
		currentValue = startValue + (startValue > endValue ? -1 : 1) * (Util.fastABS(startValue - endValue) * tick / (float) of);
		if (tick == of)
			c.setDrawHook(null);
		else
			c.setDrawHook(faderHook);
	}

}
