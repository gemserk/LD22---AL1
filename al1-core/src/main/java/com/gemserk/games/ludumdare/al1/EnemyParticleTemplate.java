package com.gemserk.games.ludumdare.al1;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gemserk.commons.artemis.components.LinearVelocityLimitComponent;
import com.gemserk.commons.artemis.components.PhysicsComponent;
import com.gemserk.commons.artemis.components.RenderableComponent;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.components.SpatialComponent;
import com.gemserk.commons.artemis.components.SpriteComponent;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.gdx.box2d.BodyBuilder;
import com.gemserk.commons.gdx.games.Spatial;
import com.gemserk.commons.gdx.games.SpatialPhysicsImpl;
import com.gemserk.resources.ResourceManager;

public class EnemyParticleTemplate extends EntityTemplateImpl {

	BodyBuilder bodyBuilder;
	ResourceManager<String> resourceManager;

	public static class FollowMainCharacterScript extends ScriptJavaImpl {

		private final Vector2 force = new Vector2();

		@Override
		public void update(World world, Entity e) {
			Entity target = world.getTagManager().getEntity(Tags.MainCharacter);

			if (target == null)
				return;

			SpatialComponent spatialComponent = Components.getSpatialComponent(e);
			SpatialComponent targetSpatialComponent = Components.getSpatialComponent(target);

			Spatial spatial = spatialComponent.getSpatial();
			Spatial targetSpatial = targetSpatialComponent.getSpatial();

			PhysicsComponent physicsComponent = Components.getPhysicsComponent(e);

			force.set(spatial.getX(), spatial.getY());
			force.sub(targetSpatial.getX(), targetSpatial.getY());

			force.mul(-5f);

			physicsComponent.getBody().applyForceToCenter(force);
		}

	}

	@Override
	public void apply(Entity entity) {
		Spatial spatial = parameters.get("spatial");

		Body body = bodyBuilder //
				.fixture(bodyBuilder.fixtureDefBuilder() //
						.categoryBits(Collisions.Enemy) //
						.maskBits(Collisions.Main) //
						.circleShape(0.25f)) //
				.type(BodyType.DynamicBody) //
				.position(spatial.getX(), spatial.getY()) //
				.angle(spatial.getAngle()) //
				.userData(entity) //
				.build();
		
		spatial.setSize(0.5f, 0.5f);

		entity.addComponent(new PhysicsComponent(body));
		entity.addComponent(new LinearVelocityLimitComponent(MathUtils.random(7f, 15f)));
		
		entity.addComponent(new SpatialComponent(new SpatialPhysicsImpl(body, spatial)));
		entity.addComponent(new ScriptComponent(new FollowMainCharacterScript()));
		
		Sprite sprite = resourceManager.getResourceValue(GameResources.Sprites.EnemyParticle);
		entity.addComponent(new SpriteComponent(sprite));
		entity.addComponent(new RenderableComponent(0));
	}

}
