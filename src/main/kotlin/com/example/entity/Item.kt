package com.example.entity

import jakarta.persistence.*

@Entity
data class Item(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,

	@Column(unique = true, nullable = false)
	val name: String,

	val description: String? = null
)