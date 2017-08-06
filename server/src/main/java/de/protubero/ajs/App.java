package de.protubero.ajs;

import org.jooby.Jooby;
import org.jooby.banner.Banner;
import org.jooby.json.Jackson;
import org.jooby.metrics.Metrics;
import org.jooby.swagger.SwaggerUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.typesafe.config.Config;

import javaslang.control.Try;

/**
 * 
 * 
 * @author MSchaefer
 *
 */
public class App extends Jooby {

	final Logger logger = LoggerFactory.getLogger(getClass());

	static String TITLE = "Angular Jooby Starter";

	{
		// display a banner on the console when the server starts up
		use(new Banner(TITLE).font("slant"));

		// Include Jackson, providing the JSON parser and renderer
		use(new Jackson());

		// make the person stare available as singleton via dependency injection
		bind(new PersonStore());

		// Init person store at startup with data loaded from the configuration
		onStart(registry -> {
			Config config = require(Config.class);
			PersonStore store = require(PersonStore.class);
			
			config.getConfig("sampledata").getObjectList("data").forEach(c -> {
				Config personConfig = c.toConfig();
				Person person = new Person();
				person.setName(personConfig.getString("name"));
				person.setAge(personConfig.getInt("age"));
				person.setEmail(personConfig.getString("email"));
				store.insert(person);

				logger.info("Saving {}", person);
			});

		});
		
		// provide swagger API, but only for the API that is used by the client
		new SwaggerUI().filter(route -> {
			return route.pattern().startsWith("/api");
		}).install(this);

		// provide health checks services
		use(new Metrics().request().threadDump().ping().healthCheck("db", new HealthCheck() {

			@Override
			protected Result check() throws Exception {
				Try<Void> tTry = Try.run(() -> {
					// Here you should do a quick test, if the app is basically healthy, i.e. check the db connection  
				});
				if (tTry.isFailure()) {
					return HealthCheck.Result.unhealthy(tTry.getCause());
				} else {
					return HealthCheck.Result.healthy("OK");
				}
			}

		}).metric("memory", new MemoryUsageGaugeSet()).metric("threads", new ThreadStatesGaugeSet())
				.metric("gc", new GarbageCollectorMetricSet()).metric("fs", new FileDescriptorRatioGauge()));

		// Provide initialization data to the angular client
		get("/api/init", (req, rsp) -> {
			Config config = require(Config.class);
			rsp.send(new ClientInit(config.getString("title")));
		});

		// Provides the API which is used by the angular client
		use(PersonController.class);

		// Include a jooby module to serve the static files that make up the client
		use(new AngularClient());
	}

	public static void main(final String[] args) {
		// start the server
		run(App::new, args);
	}

}
