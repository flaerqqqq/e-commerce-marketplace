package com.example.ecommercemarketplace.mappers;

public interface Mapper<A,B> {

    A mapFrom(B b);

    B mapTo(A a);
}
