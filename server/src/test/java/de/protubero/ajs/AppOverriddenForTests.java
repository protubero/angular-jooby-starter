package de.protubero.ajs;

public class AppOverriddenForTests extends App {

    // So that we can manipulate the store content for tests.
    public PersonStore personStore;

    @Override
    public <T> T require(Class<T> type) {
        T required = super.require(type);
        if (type == PersonStore.class) {
           personStore = (PersonStore) required;
        }
        return required;
    }
}
