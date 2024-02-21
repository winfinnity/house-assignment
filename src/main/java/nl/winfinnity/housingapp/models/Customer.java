package nl.winfinnity.housingapp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "customer")
public class Customer {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "Firstname is mandatory")
    @Column(name = "firstname")
    private String firstname;

    @NotBlank(message = "Lastname is mandatory")
    @Column(name = "lastname")
    private String lastname;



}