package com.gemserk.games.leavemealone.components;

import com.artemis.Component;
import com.gemserk.componentsengine.utils.Container;

public class ShieldComponent extends Component {

	public Container container;
	
	public boolean enabled;

	public ShieldComponent(Container container) {
		this.container = container;
	}

}
