package com.plgrnds.bank.commons;

public interface Specification<T> {

    boolean isSatisfiedBy(T value);

}
