package com.gemserk.games.leavemealone.components;

import com.artemis.Component;
import com.gemserk.componentsengine.utils.Interval;

public class SpawnerComponent extends Component {
	
	public float timeToSpawn;
	public Interval spawnInterval;
	
	public SpawnerComponent(Interval spawnInterval, float timeToSpawn) {
		this.spawnInterval = spawnInterval;
		this.timeToSpawn = timeToSpawn;
	}

}
