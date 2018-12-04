package de.Jcing.window.gui.utillities;

import java.util.HashSet;

import de.Jcing.window.gui.Component;

public class Group {
	
	protected HashSet<Component> components;

	
	public Group(Component...components) {
		this.components = new HashSet<>();
		for(Component c : components)
			this.components.add(c);
	}
	
	public Group add(Component c) {
		components.add(c);
		return this;
	}
	
	public Group remove(Component c) {
		components.remove(c);
		return this;
	}
	
	public void doAll(ComponentAction a) {
		for(Component c : components)
			a.apply(c);
	}
	
	public interface ComponentAction {
		public void apply(Component c);
	}
	
}
