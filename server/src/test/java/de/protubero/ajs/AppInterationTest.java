package de.protubero.ajs;

import org.hamcrest.CustomMatcher;
import org.hamcrest.Matcher;
import org.jooby.test.JoobyRule;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.IOException;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.fail;

public class AppInterationTest {

    private static final AppOverriddenForTests APP_OVERRIDDEN_FOR_TESTS = new AppOverriddenForTests();

    @ClassRule
    public static JoobyRule app = new JoobyRule(APP_OVERRIDDEN_FOR_TESTS);

    @Test
    public void integrationTest() {

        // App launches with starter data - ignore that for tests
        Person i1 = APP_OVERRIDDEN_FOR_TESTS.personStore.insert(Helpers.namedPerson("998877"));
        Person i2 = APP_OVERRIDDEN_FOR_TESTS.personStore.insert(Helpers.namedPerson("887766"));

        get("/api/persons/")
                .then()
                .assertThat()
                .body(isJsonArrayOrPeopleWithNames("998877", "887766"))
                .statusCode(200)
                .contentType(containsString("application/json;charset=UTF-8"));

        APP_OVERRIDDEN_FOR_TESTS.personStore.delete(i1.getId());
        APP_OVERRIDDEN_FOR_TESTS.personStore.delete(i2.getId());

    }

    private Matcher<String> isJsonArrayOrPeopleWithNames(final String... uniqueStrings) {
        return new CustomMatcher<String>("Matches names in people JSON array") {
            @Override
            public boolean matches(Object o) {
                String content = (String) o;

                Person[] people;

                try {
                    // Well it's roughly a Json array of people in
                    people = new com.fasterxml.jackson.databind.ObjectMapper().readValue(content, Person[].class);
                } catch (IOException e) {
                    throw new AssertionError("Not a JSON array of people, was: " + content);
                }

                for (String uniqueString : uniqueStrings) {
                    boolean hasIt = false;
                    for (Person person : people) {
                        if (person.getName().equals(uniqueString)) {
                            hasIt = true;
                            break;
                        }
                    }
                    if (!hasIt) {
                        fail("People array should have contained person with name " + uniqueString);
                    }
                }
                return true;
            }
        };
    }
}
