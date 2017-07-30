package de.protubero.ajs;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.jooby.Env;
import org.jooby.Jooby;
import org.jooby.MediaType;
import org.jooby.Request;
import org.jooby.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Binder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javaslang.Function2;
import javaslang.control.Option;
import javaslang.control.Try;

public class AngularClient implements Jooby.Module {

	final static Logger logger = LoggerFactory.getLogger(AngularClient.class);

	public static final String INDEX_FILENAME = "index.html";

	public static final String ERROR_PAGE_TEMPLATE = "<!doctype html><html lang=\"en\"><head><meta charset=\"utf-8\"><title>#title</title></head><body>#content</body></html>";

	public static final Function2<String, String, String> errorPageCreator = (title, content) -> {
		return ERROR_PAGE_TEMPLATE.replaceFirst("#title", title).replace("#content", content);
	};

	private Map<String, Option<byte[]>> fileNameToContentMap = new HashMap<>();

	private byte[] loadFileByName(String fileName) {
		Option<byte[]> content = fileNameToContentMap.get(fileName);
		if (content == null) {
			content = Option.none();
			try (InputStream is = getClass().getResourceAsStream("/client/" + fileName)) {
				content = Option.of(IOUtils.toByteArray(is));
			} catch (IOException e) {
				logger.debug("error loading file " + fileName, e);
			}
			fileNameToContentMap.put(fileName, content);
		} 
		
		if (content.isDefined()) {
			return content.get();
		} else {
			throw new RuntimeException("Invalid file name: " + fileName);
		}
	}

	@Override
	public void configure(Env env, Config conf, Binder binder) throws Throwable {
		env.router().get("/*", (req, rsp) -> request(req, rsp, req.path().substring(1)));
	}

	public void request(final Request req, final Response rsp, String fileName) {
		if (fileName.length() == 0) {
			fileName = INDEX_FILENAME;
		}
		
		logger.debug("request client file '" + fileName + "'");

		final String fileToLoad = fileName; 
		
		Try<byte[]> tTry = Try.of(() -> loadFileByName(fileToLoad));
		
		tTry = tTry.recoverWith(e -> {
			if (fileToLoad.equals(INDEX_FILENAME)) {
				return Try.of(() -> errorPageCreator.apply("Client", "Angular Client not available").getBytes());
			} else {
				return Try.failure(e);
			}
		});
		
		tTry.onFailure(exc -> {
			rsp.status(404);
		});

		tTry.onSuccess(bytes -> {
			Try.run(() -> { 
				rsp.status(200)
				.type(MediaType.byPath(fileToLoad).orElseThrow(() -> new RuntimeException("unable to determine media type of file " + fileToLoad)))
				.send(bytes);
			}).onFailure(e -> {
				logger.debug("error sending response", e);
			});
		});
	}
	
	public Config config() {
		return ConfigFactory.parseResources(getClass(), "angular.properties");
	}
}
