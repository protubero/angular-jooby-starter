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

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import io.vavr.collection.List;

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
