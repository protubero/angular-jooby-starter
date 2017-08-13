/**
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <https://unlicense.org>
 */
package de.protubero.ajs;

import java.util.List;
import java.util.Optional;

import org.jooby.Result;
import org.jooby.Results;
import org.jooby.Status;
import org.jooby.mvc.Body;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.DELETE;
import org.jooby.mvc.GET;
import org.jooby.mvc.PATCH;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;


/**
 * http://www.restapitutorial.com/lessons/httpmethods.html
 * 
 * 
 * @author MSchaefer
 *
 */
@Path("/api/persons")
@Produces("application/json")
public class PersonController {

	final Logger logger = LoggerFactory.getLogger(getClass());	
	
	private PersonStore store;
	
	@Inject
	private PersonController(PersonStore store) {
		this.store = store;
	}
	
    /**
     * List persons:
     */
	@GET
    public List<Person> list() {
      return store.selectAll();
    }

    /**
     * Get a person by ID:
     */
	@GET
	@Path("/:id")
	public Result findById(@Named("id") Integer id) throws Throwable {
      Optional<Person> person = store.selectById(id.intValue());
      if (!person.isPresent()) {
		 return Results.with(404);
      } else {
    	 return Results.ok(person.get()); 
      }
    }

	@POST
	@Consumes("application/json")
	public Result submit(@Body Person newPerson) throws Throwable {
		Person insertedPerson = store.insert(newPerson);
		return Results.with(Status.CREATED).header("Location", "/api/persons/" + insertedPerson.getId());
	}

	@DELETE
	@Path("/:id")
	public Result delete(@Named("id") Integer id) throws Throwable {
		if (!store.delete(id)) {
			return Results.with(404);
		} else {
			return Results.ok();
		}	
	}

	@PATCH
	@Path("/:id")
	@Consumes("application/json")
	public Result update(@Named("id") Integer id, @Body Person person) throws Throwable {
	    Optional<Person> personToUpdate = store.selectById(id.intValue());
		if (!personToUpdate.isPresent()) {
			return Results.with(404);
		} else {
			personToUpdate.get().updateWith(person);
			return Results.ok(personToUpdate.get());
		}	
	}
	
}