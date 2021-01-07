package com.ktpt.quna;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Question {
    @Id
    private Long id;

    protected Question() {
    }
}
