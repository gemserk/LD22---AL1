package com.gemserk.games.leavemealone.spawner;

import com.gemserk.commons.artemis.templates.EntityTemplate;
import com.gemserk.componentsengine.utils.Parameters;

public class SpawnElement {
	
	public float time;
	public EntityTemplate template;
	public Parameters parameters;
	
	public SpawnElement(float time, EntityTemplate template, Parameters parameters) {
		this.time = time;
		this.template = template;
		this.parameters = parameters;
	}
	
}
