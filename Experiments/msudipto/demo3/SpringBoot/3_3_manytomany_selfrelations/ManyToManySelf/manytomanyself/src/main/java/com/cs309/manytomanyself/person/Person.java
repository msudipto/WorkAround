package com.cs309.manytomanyself.person;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull; // Replaced deprecated NotNull
import lombok.Data;

@Entity
@Table(name = "persons")
@Data
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "person_id")
	private Long id;

	@NotNull
	@Column
	private String name;

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Removed CascadeType.REMOVE
	@JoinTable(name = "friends_with",
			joinColumns = {@JoinColumn(name = "person_id")},
			inverseJoinColumns = {@JoinColumn(name = "friend_id")})
	@JsonIgnore
	private Set<Person> friends = new HashSet<>(); // Used diamond operator

	@ManyToMany(mappedBy = "friends")
	@JsonIgnore
	private Set<Person> friendsOf = new HashSet<>(); // Used diamond operator

}