package de.protubero.ajs;

import java.util.List;

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

import io.requery.EntityStore;
import io.requery.Persistable;

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
	
	private EntityStore<Persistable, Person> store;
	
	@Inject
	private PersonController(EntityStore<Persistable, Person> store) {
		this.store = store;
	}
	
    /**
     * List persons:
     */
	@GET
    public List<Person> list() {
      return store.select(Person.class)
          .get()
          .toList();
    }

    /**
     * Get a person by ID:
     */
	@GET
	@Path("/:id")
	public Person findById(@Named("id") Integer id) throws Throwable {
      return store.select(Person.class)
          .where(Person.ID.eq(id.intValue()))
          .get()
          .first();
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
		if (store.delete().from(Person.class).where(Person.ID.eq(id)).get().call() == 0) {
			return Results.with(404);
		} else {
			return Results.ok();
		}	
	}

	@PATCH
	@Path("/:id")
	@Consumes("application/json")
	public Person update(@Named("id") Integer id, @Body Person person) throws Throwable {
		Person existingPerson = findById(id);
		
		existingPerson.setAge(person.getAge());
		existingPerson.setName(person.getName());
		existingPerson.setEmail(person.getEmail());
		
		store.update(existingPerson);
		
		return existingPerson;
	}
	
}