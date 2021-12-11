package com.university.semenenko.meterproject.Entity;

import lombok.Getter;

@Getter
public class Person {

    // тут реализован Builder pattern
    String personName;
    String personCity;

    private Person() {
    }

    public static Builder newBuilder() {
        return new Person().new Builder();
    }

    public class Builder {

        private Builder() {

        }

        public Builder setPersonName(String personName) {
            Person.this.personName = personName;
            return this;
        }

        public Builder setPersonCity(String personCity) {
            Person.this.personCity = personCity;
            return this;
        }

        public Person build() {
            return Person.this;
        }
    }
}
