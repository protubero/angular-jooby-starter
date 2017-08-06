package de.protubero.ajs;

import java.io.File;
import java.util.function.Supplier;

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

import io.netty.util.internal.SystemPropertyUtil;
import javaslang.control.Try;

public class App extends Jooby {

	final Logger logger = LoggerFactory.getLogger(getClass());

	static String TITLE = "Angular Jooby Starter";

	{
		use(new Banner(TITLE).font("slant"));

		use(new Jackson());

		String confFilePath = SystemPropertyUtil.get("ajsconf");
		if (confFilePath != null) {
			logger.info("using config file " + confFilePath);
			File confFile = new File(confFilePath);
			if (confFile.isDirectory() || !confFile.exists()) {
				logger.error("invalid configuration file: " + confFilePath);
			}
			conf(confFile);
		} else {
			logger.warn("no config file found, use -Dajsconf=\"...\" to specify config file");
		}
		
		bind(new PersonStore());

		/** Save a new person on startup: */
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
		
		
		new SwaggerUI().filter(route -> {
			return route.pattern().startsWith("/api");
		}).install(this);

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

		get("/api/init", (req, rsp) -> {
			Config config = require(Config.class);
			rsp.send(new ClientInit(config.getString("title")));
		});

		use(PersonController.class);

		use(new AngularClient());
	}

	public static void main(final String[] args) {
		run(App::new, args);
	}

}
