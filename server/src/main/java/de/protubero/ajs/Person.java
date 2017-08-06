package de.protubero.ajs;

/**
 * Simple person class for demo purposes.
 * 
 * @author MSchaefer
 *
 */
public class Person {

	private int id;
	private String name;
	private String email;
	private int age;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void updateWith(Person upToDatePerson) {
		this.age = upToDatePerson.age;
		this.email = upToDatePerson.email;
		this.name = upToDatePerson.name;
	}

}
