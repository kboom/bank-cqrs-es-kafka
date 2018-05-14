package kboom.playground.bank.commons;

public interface Specification<T> {

    boolean isSatisfiedBy(T value);

}
