package com.gemserk.games.leavemealone.scripts;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.commons.artemis.components.PhysicsComponent;
import com.gemserk.commons.artemis.components.SpatialComponent;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.gdx.games.Spatial;
import com.gemserk.games.leavemealone.Tags;
import com.gemserk.games.leavemealone.components.AliveComponent;
import com.gemserk.games.leavemealone.components.Components;
import com.gemserk.games.leavemealone.components.AliveComponent.State;

public class FollowMainCharacterScript extends ScriptJavaImpl {

	private final Vector2 force = new Vector2();

	@Override
	public void update(World world, Entity e) {
		
		AliveComponent aliveComponent = Components.getAliveComponent(e);
		if (aliveComponent.state == State.Spawning)
			return;

		SpatialComponent spatialComponent = Components.getSpatialComponent(e);
		Spatial spatial = spatialComponent.getSpatial();
		
		Entity target = world.getTagManager().getEntity(Tags.MainCharacter);

		if (target == null)
			return;

		SpatialComponent targetSpatialComponent = Components.getSpatialComponent(target);

		Spatial targetSpatial = targetSpatialComponent.getSpatial();

		PhysicsComponent physicsComponent = Components.getPhysicsComponent(e);

		force.set(spatial.getX(), spatial.getY());
		force.sub(targetSpatial.getX(), targetSpatial.getY());

		force.mul(-5f);

		physicsComponent.getBody().applyForceToCenter(force);
	}

}