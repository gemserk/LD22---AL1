package com.gemserk.games.leavemealone;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.animation4j.transitions.sync.Synchronizer;
import com.gemserk.commons.artemis.WorldWrapper;
import com.gemserk.commons.artemis.components.Components;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.events.Event;
import com.gemserk.commons.artemis.events.EventListener;
import com.gemserk.commons.artemis.events.EventManager;
import com.gemserk.commons.artemis.events.EventManagerImpl;
import com.gemserk.commons.artemis.render.RenderLayers;
import com.gemserk.commons.artemis.scripts.Script;
import com.gemserk.commons.artemis.systems.CameraUpdateSystem;
import com.gemserk.commons.artemis.systems.EventManagerWorldSystem;
import com.gemserk.commons.artemis.systems.GroupSystem;
import com.gemserk.commons.artemis.systems.LimitLinearVelocitySystem;
import com.gemserk.commons.artemis.systems.PhysicsSystem;
import com.gemserk.commons.artemis.systems.PreviousStateSpatialSystem;
import com.gemserk.commons.artemis.systems.ReflectionRegistratorEventSystem;
import com.gemserk.commons.artemis.systems.RenderLayerSpriteBatchImpl;
import com.gemserk.commons.artemis.systems.RenderableSystem;
import com.gemserk.commons.artemis.systems.ScriptSystem;
import com.gemserk.commons.artemis.systems.SpriteUpdateSystem;
import com.gemserk.commons.artemis.systems.TagSystem;
import com.gemserk.commons.artemis.templates.EntityFactory;
import com.gemserk.commons.artemis.templates.EntityFactoryImpl;
import com.gemserk.commons.artemis.templates.EntityTemplate;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.artemis.utils.EntityStore;
import com.gemserk.commons.gdx.GameStateImpl;
import com.gemserk.commons.gdx.GlobalTime;
import com.gemserk.commons.gdx.box2d.BodyBuilder;
import com.gemserk.commons.gdx.camera.CameraImpl;
import com.gemserk.commons.gdx.camera.CameraRestrictedImpl;
import com.gemserk.commons.gdx.camera.Libgdx2dCamera;
import com.gemserk.commons.gdx.camera.Libgdx2dCameraTransformImpl;
import com.gemserk.commons.gdx.games.SpatialImpl;
import com.gemserk.commons.gdx.graphics.ConvexHull2d;
import com.gemserk.commons.gdx.graphics.ConvexHull2dImpl;
import com.gemserk.commons.gdx.graphics.ImmediateModeRendererUtils;
import com.gemserk.commons.gdx.graphics.SpriteBatchUtils;
import com.gemserk.commons.gdx.math.MathUtils2;
import com.gemserk.commons.gdx.screens.transitions.TransitionBuilder;
import com.gemserk.commons.gdx.time.TimeStepProviderGameStateImpl;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.commons.text.CustomDecimalFormat;
import com.gemserk.commons.utils.StoreFactory;
import com.gemserk.componentsengine.utils.Interval;
import com.gemserk.componentsengine.utils.ParametersWrapper;
import com.gemserk.games.leavemealone.components.StoreComponent;
import com.gemserk.games.leavemealone.scripts.GameLogicScript;
import com.gemserk.games.leavemealone.scripts.StickControllerScript;
import com.gemserk.games.leavemealone.systems.RenderScriptSystem;
import com.gemserk.games.leavemealone.templates.CameraTemplate;
import com.gemserk.games.leavemealone.templates.EnemyParticleSimpleTemplate;
import com.gemserk.games.leavemealone.templates.EnemyParticleTemplate;
import com.gemserk.games.leavemealone.templates.ForceInAreaTemplate;
import com.gemserk.games.leavemealone.templates.MainParticleTemplate;
import com.gemserk.games.leavemealone.templates.ParticlesCenterTemplate;
import com.gemserk.games.leavemealone.templates.WaveSpawnerTemplate;

public class PlayGameState extends GameStateImpl {

	public static class TemplateStoreFactory implements StoreFactory<Entity> {

		EntityFactory entityFactory;
		EntityStores entityStores;
		Injector injector;

		Class<? extends EntityTemplate> templateClass;

		public TemplateStoreFactory(Class<? extends EntityTemplate> templateClass) {
			this.templateClass = templateClass;
		}

		@Override
		public Entity createObject() {
			Entity entity = entityFactory.instantiate(injector.getInstance(templateClass));
			entity.addComponent(new StoreComponent(entityStores.get(templateClass)));
			return entity;
		}
	}

	Game game;
	Injector injector;

	Libgdx2dCamera worldCamera;
	Libgdx2dCamera normalCamera;
	Libgdx2dCamera backgroundCamera;

	WorldWrapper scene;

	float score;
	SpriteBatch spriteBatch;
	ShapeRenderer shapeRenderer;
	BitmapFont font;
	CustomDecimalFormat customDecimalFormat;

	Synchronizer synchronizer;

	ConvexHull2d convexHull2d;
	private Rectangle worldBounds;

	@Override
	public void init() {

		final Injector injector = this.injector.createChildInjector();

		synchronizer = new Synchronizer();
		shapeRenderer = new ShapeRenderer();

		float gameZoom = Gdx.graphics.getHeight() / 480f;
		float realGameZoom = 48f * gameZoom;

		normalCamera = new Libgdx2dCameraTransformImpl(0f, 0f);
		normalCamera.zoom(1f * gameZoom);

		worldCamera = new Libgdx2dCameraTransformImpl(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f);
		worldCamera.zoom(realGameZoom);

		backgroundCamera = new Libgdx2dCameraTransformImpl(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f);
		backgroundCamera.zoom(24f * gameZoom);

		worldBounds = new Rectangle(-10f, -8f, 20f, 16f);

		RenderLayers renderLayers = new RenderLayers();

		renderLayers.add("World", new RenderLayerSpriteBatchImpl(-500, 500, worldCamera));

		scene = new WorldWrapper(new World());

		com.badlogic.gdx.physics.box2d.World physicsWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0f, 0f), false);
		final EntityFactory entityFactory = new EntityFactoryImpl(scene.getWorld());
		EventManager eventManager = new EventManagerImpl();

		final BodyBuilder bodyBuilder = new BodyBuilder(physicsWorld);

		TimeStepProviderGameStateImpl timeStepProvider = new TimeStepProviderGameStateImpl(this);

		final EntityStores entityStores = new EntityStores();

		injector.bind("entityFactory", entityFactory);
		injector.bind("eventManager", eventManager);
		injector.bind("physicsWorld", physicsWorld);
		injector.bind("bodyBuilder", bodyBuilder);
		injector.bind("synchronizer", synchronizer);
		injector.bind("shapeRenderer", shapeRenderer);
		injector.bind("timeStepProvider", timeStepProvider);
		injector.bind("worldBounds", worldBounds);
		injector.bind("entityStores", entityStores);

		scene.addUpdateSystem(new PreviousStateSpatialSystem());
		scene.addUpdateSystem(new ScriptSystem());
		scene.addUpdateSystem(new TagSystem());
		scene.addUpdateSystem(new GroupSystem());
		scene.addUpdateSystem(new ReflectionRegistratorEventSystem(eventManager));
		scene.addUpdateSystem(new PhysicsSystem(physicsWorld));

		scene.addUpdateSystem(new LimitLinearVelocitySystem(physicsWorld));

		scene.addUpdateSystem(injector.getInstance(EventManagerWorldSystem.class));

		scene.addRenderSystem(new SpriteUpdateSystem(timeStepProvider));
		scene.addRenderSystem(new CameraUpdateSystem(timeStepProvider));

		scene.addRenderSystem(new RenderableSystem(renderLayers));

		// scene.addRenderSystem(new Box2dRenderSystem(worldCamera, physicsWorld));
		// scene.addRenderSystem(new Box2dLinearVelocityRenderSystem(worldCamera));
		scene.addRenderSystem(new RenderScriptSystem());

		scene.init();

		TemplateStoreFactory simpleParticleTemplateStore = new TemplateStoreFactory(EnemyParticleSimpleTemplate.class);
		injector.injectMembers(simpleParticleTemplateStore);
		EntityStore simpleParticleStore = new EntityStore(simpleParticleTemplateStore);
		simpleParticleStore.preCreate(10);
		entityStores.put(EnemyParticleSimpleTemplate.class, simpleParticleStore);

		TemplateStoreFactory particleTemplateStore = new TemplateStoreFactory(EnemyParticleTemplate.class);
		injector.injectMembers(particleTemplateStore);
		EntityStore particleStore = new EntityStore(particleTemplateStore);
		particleStore.preCreate(10);
		entityStores.put(EnemyParticleTemplate.class, particleStore);

		entityFactory.instantiate(new EntityTemplateImpl() {
			@Override
			public void apply(Entity entity) {
				entity.addComponent(new ScriptComponent(injector.getInstance(GameLogicScript.class)));
			}
		});

		EntityTemplate mainParticleTemplate = injector.getInstance(MainParticleTemplate.class);
		entityFactory.instantiate(mainParticleTemplate, new ParametersWrapper() //
				.put("camera", worldCamera));

		Rectangle cameraBounds = new Rectangle(worldBounds);

		// MathUtils2.growRectangle(cameraBounds, 7f, 3f);
		MathUtils2.growRectangle(cameraBounds, 2f * 160f / realGameZoom, 2f * 160f / realGameZoom);

		entityFactory.instantiate(injector.getInstance(CameraTemplate.class), new ParametersWrapper() //
				.put("libgdx2dCamera", worldCamera) //
				.put("camera", new CameraRestrictedImpl(0f, 0f, realGameZoom, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cameraBounds)) //
				.put("targetTag", Tags.MainCharacter) //
				);

		entityFactory.instantiate(injector.getInstance(CameraTemplate.class), new ParametersWrapper() //
				.put("libgdx2dCamera", backgroundCamera) //
				.put("camera", new CameraImpl(0f, 0f, 24f * gameZoom, 0f)) //
				.put("targetTag", Tags.MainCharacter) //
				.put("parallax", 0.5f) //
				);

		// EntityTemplate shieldTemplate = injector.getInstance(ShieldTemplate.class);
		// entityFactory.instantiate(shieldTemplate, new ParametersWrapper() //
		// .put("camera", worldCamera));

		entityFactory.instantiate(injector.getInstance(WaveSpawnerTemplate.class), new ParametersWrapper() //
				.put("interval", new Interval(7, 14)) //
				.put("timeToSpawn", 1f) //
				);

		// entityFactory.instantiate(injector.getInstance(WaveSpawnerTemplate.class), new ParametersWrapper() //
		// .put("interval", new Interval(3, 20)) //
		// .put("timeToSpawn", 10f) //
		// .put("entityTemplate", injector.getInstance(EnemyParticleTemplate.class)) //
		// );

		// entityFactory.instantiate(new EntityTemplateImpl() {
		// @Override
		// public void apply(Entity entity) {
		// EnemyParticleSpawnerScript spawnerScript = new EnemyParticleSpawnerScript();
		// injector.injectMembers(spawnerScript);
		//
		// entity.addComponent(new SpawnerComponent(injector.getInstance(EnemyParticleTemplate.class), new Interval(5, 10), 5f));
		// entity.addComponent(new ScriptComponent(spawnerScript));
		// }
		// });
		//
		// entityFactory.instantiate(new EntityTemplateImpl() {
		// @Override
		// public void apply(Entity entity) {
		// EnemyParticleSpawnerScript spawnerScript = new EnemyParticleSpawnerScript();
		// injector.injectMembers(spawnerScript);
		//
		// entity.addComponent(new SpawnerComponent(injector.getInstance(EnemyParticleSimpleTemplate.class), new Interval(4, 8), 2f));
		// entity.addComponent(new ScriptComponent(spawnerScript));
		// }
		// });

		float width = 1f;

		entityFactory.instantiate(injector.getInstance(ForceInAreaTemplate.class), new ParametersWrapper() //
				.put("spatial", new SpatialImpl(0f, worldBounds.y - width, worldBounds.width, width, 0f)) //
				.put("force", new Vector2(0f, 100f)) //
				);
		entityFactory.instantiate(injector.getInstance(ForceInAreaTemplate.class), new ParametersWrapper() //
				.put("spatial", new SpatialImpl(0f, worldBounds.y + worldBounds.height + width, worldBounds.width, width, 0f)) //
				.put("force", new Vector2(0f, -100f)) //
				);

		entityFactory.instantiate(injector.getInstance(ForceInAreaTemplate.class), new ParametersWrapper() //
				.put("spatial", new SpatialImpl(worldBounds.x - width, 0f, width, worldBounds.height, 0f)) //
				.put("force", new Vector2(100f, 0f)) //
				);

		entityFactory.instantiate(injector.getInstance(ForceInAreaTemplate.class), new ParametersWrapper() //
				.put("spatial", new SpatialImpl(worldBounds.x + worldBounds.width + width, 0f, width, worldBounds.height, 0f)) //
				.put("force", new Vector2(-100f, 0f)) //
				);

		entityFactory.instantiate(injector.getInstance(ParticlesCenterTemplate.class));

		eventManager.register(Events.GameOver, new EventListener() {
			@Override
			public void onEvent(Event event) {
				new TransitionBuilder(game, game.gameOverScreen) //
						.disposeCurrent() //
						.restartScreen() //
						.parameter("score", (long) score).start();
			}
		});

		eventManager.register(Events.ParticlesDestroyed, new EventListener() {
			@Override
			public void onEvent(Event event) {
				// depends on the type maybe and quantity...
				ImmutableBag<Entity> particles = (ImmutableBag<Entity>) event.getSource();
				score += 5 * particles.size();
			}
		});

		score = 0;

		spriteBatch = new SpriteBatch();
		font = new BitmapFont();

		customDecimalFormat = new CustomDecimalFormat(5);

		convexHull2d = new ConvexHull2dImpl(10);
	}

	@Override
	public void update() {
		synchronizer.synchronize(getDelta());
		scene.update(getDeltaInMs());

		ImmutableBag<Entity> enemies = scene.getWorld().getGroupManager().getEntities(Groups.EnemyCharacter);

		score += GlobalTime.getDelta() * enemies.size();

	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		shapeRenderer.setProjectionMatrix(backgroundCamera.getProjectionMatrix());
		shapeRenderer.setTransformMatrix(backgroundCamera.getModelViewMatrix());

		Gdx.gl.glEnable(GL10.GL_BLEND);
		shapeRenderer.begin(ShapeType.Line);

		int sizeX = 1;
		int sizeY = 1;

		for (int i = -25; i < 25; i++) {
			shapeRenderer.setColor(0f, 0f, 1f, 0.15f);
			shapeRenderer.line(i * sizeX, -1000, i * sizeX, 1000);
		}

		for (int i = -25; i < 25; i++) {
			shapeRenderer.setColor(0f, 0f, 1f, 0.15f);
			shapeRenderer.line(-1000, i * sizeY, 1000, i * sizeY);
		}

		shapeRenderer.end();

		shapeRenderer.setProjectionMatrix(worldCamera.getProjectionMatrix());
		shapeRenderer.setTransformMatrix(worldCamera.getModelViewMatrix());

		scene.render();

		normalCamera.apply();

		spriteBatch.begin();
		SpriteBatchUtils.drawMultilineText(spriteBatch, font, customDecimalFormat.format((long) score), 20f, Gdx.graphics.getHeight() * 0.95f, 0f, 0.5f);
		spriteBatch.end();

		ImmediateModeRendererUtils.getProjectionMatrix().set(worldCamera.getCombinedMatrix());
		ImmediateModeRendererUtils.drawRectangle(worldBounds, Color.BLUE);

		renderMoveableStickOnScreen();
	}

	private void renderMoveableStickOnScreen() {
		Entity mainCharacter = scene.getWorld().getTagManager().getEntity(Tags.MainCharacter);

		if (mainCharacter == null)
			return;

		Script script = Components.getScriptComponent(mainCharacter).getScripts().get(0);

		if (script == null)
			return;

		if (!(script instanceof StickControllerScript))
			return;

		StickControllerScript stickControllerScript = (StickControllerScript) script;

		if (!stickControllerScript.moving)
			return;

		Vector2 stickPosition = stickControllerScript.stickPosition;
		Vector2 touchPosition = stickControllerScript.touchPosition;

		ImmediateModeRendererUtils.getProjectionMatrix().set(normalCamera.getCombinedMatrix());

		ImmediateModeRendererUtils.drawSolidCircle(stickPosition.x, stickPosition.y, stickControllerScript.radius * 0.1f, Color.RED);
		ImmediateModeRendererUtils.drawSolidCircle(stickPosition.x, stickPosition.y, stickControllerScript.radius, Color.RED);
		ImmediateModeRendererUtils.drawSolidCircle(touchPosition.x, touchPosition.y, stickControllerScript.radius * 0.25f, Color.RED);
	}

	@Override
	public void resume() {
		Gdx.input.setCatchBackKey(false);
	}

}
