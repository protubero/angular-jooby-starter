package de.protubero.ajs;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import javaslang.collection.List;

/**
 * Simple collection to hold the person objects. If it looks unfamiliar,
 * thats because i played around with the javaslang lib (now named vavr).
 * 
 * @author MSchaefer
 *
 */
public class PersonStore {

	private AtomicInteger idCounter = new AtomicInteger();
	
	// Immutable List of person objects
	private List<Person> data = List.empty();

	public Person insert(Person person) {
		person.setId(idCounter.incrementAndGet());
		data = data.append(person);
		return person;
	}

	public Optional<Person> selectById(int id) {
		return data.find(p -> p.getId() == id).toJavaOptional();
	}

	public java.util.List<Person> selectAll() {
		return data.toJavaList();
	}

	public boolean delete(int id) {
		Optional<Person> personToDelete = selectById(id);
		if (personToDelete.isPresent()) {
			data = data.remove(personToDelete.get());
			return true;
		} else {
			return false;
		}
		
	}

	
}
