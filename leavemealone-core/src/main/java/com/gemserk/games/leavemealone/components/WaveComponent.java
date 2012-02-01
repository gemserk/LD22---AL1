package com.gemserk.games.leavemealone.components;

import com.artemis.Component;
import com.gemserk.games.leavemealone.spawner.ElementsSpawner;

public class WaveComponent extends Component {
	
	public ElementsSpawner elementsSpawner;
	
	public WaveComponent(ElementsSpawner elementsSpawner) {
		this.elementsSpawner = elementsSpawner;
	}
	
}