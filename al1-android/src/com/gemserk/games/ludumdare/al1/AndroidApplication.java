package com.gemserk.games.ludumdare.al1;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.gemserk.componentsengine.input.InputDevicesMonitorImpl;
import com.gemserk.componentsengine.input.LibgdxInputMappingBuilder;
import com.swarmconnect.Swarm;
import com.swarmconnect.SwarmLeaderboard;
import com.swarmconnect.SwarmLeaderboard.GotLeaderboardCB;

public class AndroidApplication extends com.badlogic.gdx.backends.android.AndroidApplication {

	private Game game;
	private Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		RelativeLayout layout = new RelativeLayout(this);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		config.useGL20 = false;
		config.useAccelerometer = false;
		config.useCompass = false;
		config.useWakelock = true;

		handler = new Handler();

		game = new Game() {

			private InputDevicesMonitorImpl<String> inputMonitor;

			@Override
			public void create() {
				super.create();

				inputMonitor = new InputDevicesMonitorImpl<String>();
				new LibgdxInputMappingBuilder<String>(inputMonitor, Gdx.input) {
					{
						monitorKey("openDashboard", Keys.MENU);
					}
				};
			}

			@Override
			public void render() {
				super.render();

				inputMonitor.update();

				if (inputMonitor.getButton("openDashboard").isReleased()) {

					// handler.post(new Runnable() {
					//
					// @Override
					// public void run() {
					// // if (!Swarm.isInitialized())
					// // Swarm.autoLogin(null);
					//
					// SwarmLeaderboard.getLeaderboardById(99, new GotLeaderboardCB() {
					// @Override
					// public void gotLeaderboard(SwarmLeaderboard leaderboard) {
					// if (leaderboard != null)
					// leaderboard.submitScore(100, "super score!");
					// }
					// });
					// }
					// });

					if (!Swarm.isInitialized()) {
						Runnable openDashboardRunnable = new Runnable() {
							@Override
							public void run() {
								if (!Swarm.isInitialized())
									Swarm.init(AndroidApplication.this, 206, "95308f83c2b18956a15289e4064ce8d6");
							}
						};
						handler.post(openDashboardRunnable);
					} else {
						
						SwarmLeaderboard.getLeaderboardById(99, new GotLeaderboardCB() {
							@Override
							public void gotLeaderboard(SwarmLeaderboard leaderboard) {
								if (leaderboard != null)
									leaderboard.submitScore(100, "super score!");
							}
						});
						
						 Swarm.showDashboard();
					}

				}

			}

		};

		View gameView = initializeForView(game, config);

		layout.addView(gameView);

		setContentView(layout);

		Swarm.setActive(AndroidApplication.this);
	}

	public void onResume() {
		super.onResume();
		Swarm.setActive(this);
	}

	public void onPause() {
		super.onPause();
		Swarm.setInactive(this);
	}

}
