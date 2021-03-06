package com.gemserk.games.leavemealone.templates;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gemserk.commons.artemis.components.GroupComponent;
import com.gemserk.commons.artemis.components.LinearVelocityLimitComponent;
import com.gemserk.commons.artemis.components.PhysicsComponent;
import com.gemserk.commons.artemis.components.PreviousStateSpatialComponent;
import com.gemserk.commons.artemis.components.PropertiesComponent;
import com.gemserk.commons.artemis.components.RenderableComponent;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.components.SpatialComponent;
import com.gemserk.commons.artemis.components.SpriteComponent;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.gdx.box2d.BodyBuilder;
import com.gemserk.commons.gdx.games.Spatial;
import com.gemserk.commons.gdx.games.SpatialImpl;
import com.gemserk.commons.gdx.games.SpatialPhysicsImpl;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.games.leavemealone.Collisions;
import com.gemserk.games.leavemealone.GameResources;
import com.gemserk.games.leavemealone.Groups;
import com.gemserk.games.leavemealone.Tags;
import com.gemserk.games.leavemealone.components.AliveComponent;
import com.gemserk.games.leavemealone.components.AliveComponent.State;
import com.gemserk.games.leavemealone.components.Components;
import com.gemserk.games.leavemealone.components.FollowRandomTargetComponent;
import com.gemserk.games.leavemealone.scripts.AliveTimeScript;
import com.gemserk.games.leavemealone.scripts.BounceWhenCollideScript;
import com.gemserk.games.leavemealone.scripts.LookToMovingDirectionScript;
import com.gemserk.resources.ResourceManager;

public class EnemyParticleSimpleTemplate extends EntityTemplateImpl {

	Injector injector;
	BodyBuilder bodyBuilder;
	ResourceManager<String> resourceManager;

	public static class FixedMovementScript extends ScriptJavaImpl {

		private final Vector2 position = new Vector2();
		private final Vector2 force = new Vector2();

		@Override
		public void init(World world, Entity e) {
			FollowRandomTargetComponent followRandomTargetComponent = Components.getFollowRandomTargetComponent(e);
			followRandomTargetComponent.position.set(MathUtils.random(-5f, 5f), MathUtils.random(-5f, 5f));
		}

		@Override
		public void update(World world, Entity e) {

			AliveComponent aliveComponent = Components.getAliveComponent(e);
			if (aliveComponent.state == State.Spawning)
				return;

			FollowRandomTargetComponent followRandomTargetComponent = Components.getFollowRandomTargetComponent(e);

			SpatialComponent spatialComponent = Components.getSpatialComponent(e);
			Spatial spatial = spatialComponent.getSpatial();

			position.set(spatial.getX(), spatial.getY());

			if (followRandomTargetComponent.position.dst(position) < 1f) {
				followRandomTargetComponent.position.set(MathUtils.random(-5f, 5f), MathUtils.random(-5f, 5f));
			} else {
				PhysicsComponent physicsComponent = Components.getPhysicsComponent(e);

				force.set(spatial.getX(), spatial.getY());
				force.sub(followRandomTargetComponent.position.x, followRandomTargetComponent.position.y);

				force.mul(-3f);

				Body body = physicsComponent.getBody();
				body.applyForceToCenter(force);
			}

		}

	}

	public static class RandomizeParticleScript extends ScriptJavaImpl {

//		private final Vector2 position = new Vector2();
		// private final Rectangle worldRectangle = new Rectangle(-7.5f, -4.5f, 15f, 9f);

		Rectangle worldBounds;

		ResourceManager<String> resourceManager;

		@Override
		public void init(World world, Entity e) {
			Entity mainCharacter = world.getTagManager().getEntity(Tags.MainCharacter);

			if (mainCharacter != null) {
				// Spatial mainCharacterSpatial = Components.getSpatialComponent(mainCharacter).getSpatial();
				// Spatial spatial = Components.getSpatialComponent(e).getSpatial();
				//
				// position.set(MathUtils.random(5f, 12f), 0f);
				// position.rotate(MathUtils.random(0, 360f));
				//
				// position.add(mainCharacterSpatial.getX(), mainCharacterSpatial.getY());
				//
				// MathUtils2.truncate(position, worldBounds);
				//
				// spatial.setPosition(position.x, position.y);
			}

			AliveComponent aliveComponent = Components.getAliveComponent(e);
			aliveComponent.time = MathUtils.random(9f, 18f);
			aliveComponent.spawnTime = 1f;
			aliveComponent.dyingTime = 1f;
			aliveComponent.state = State.Spawning;

			LinearVelocityLimitComponent linearVelocityComponent = Components.getLinearVelocityComponent(e);
			linearVelocityComponent.setLimit(0.6f * MathUtils.random(3.5f, 6.5f));
			
			Sound spawnSound = resourceManager.getResourceValue(GameResources.Sounds.Spawn);
			spawnSound.play();
		}

	}

	@Override
	public void apply(Entity entity) {
		Spatial spatial = new SpatialImpl(0, 0, 0.5f, 0.5f, 0f);

		Body body = bodyBuilder //
				.fixture(bodyBuilder.fixtureDefBuilder() //
						// .restitution(1f) //
						.categoryBits(Collisions.Enemy) //
						.maskBits(Collisions.None) //
						.circleShape(0.2f)) //
				.type(BodyType.DynamicBody) //
				.position(spatial.getX(), spatial.getY()) //
				.angle(spatial.getAngle()) //
				.userData(entity) //
				.build();


		// entity.setGroup(Groups.EnemyCharacter);
		entity.addComponent(new GroupComponent(Groups.EnemyCharacter));

		entity.addComponent(new PhysicsComponent(body));
		entity.addComponent(new LinearVelocityLimitComponent(1f));

		entity.addComponent(new SpatialComponent(new SpatialPhysicsImpl(body, spatial)));
		entity.addComponent(new PreviousStateSpatialComponent());

		entity.addComponent(new ScriptComponent( //
				injector.getInstance(RandomizeParticleScript.class), //
				injector.getInstance(FixedMovementScript.class), //
				injector.getInstance(AliveTimeScript.class), //
				injector.getInstance(BounceWhenCollideScript.class), //
				injector.getInstance(LookToMovingDirectionScript.class) //
		));

		Sprite sprite = resourceManager.getResourceValue(GameResources.Sprites.Al3);

		SpriteComponent spriteComponent = new SpriteComponent(sprite);

		entity.addComponent(new PropertiesComponent());

		entity.addComponent(spriteComponent);
		entity.addComponent(new RenderableComponent(1));

		entity.addComponent(new AliveComponent(1f));
		entity.addComponent(new FollowRandomTargetComponent());
	}
}
