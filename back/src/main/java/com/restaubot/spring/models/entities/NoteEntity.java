package com.restaubot.spring.models.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "avis")
@DiscriminatorValue("Avis")
@NoArgsConstructor
@AllArgsConstructor
public class NoteEntity extends UserEntity {
    private String surname;
    private String firstname;
    private String phone;
    private String address;
}
