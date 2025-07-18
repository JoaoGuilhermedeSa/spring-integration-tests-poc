package com.example.controller

import com.example.repository.ItemRepository
import com.example.entity.Item
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/items")
class ItemController(private val itemRepository: ItemRepository) {

	@PostMapping
	fun createItem(@RequestBody item: Item): ResponseEntity<Item> {
		if (itemRepository.existsByName(item.name)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build()
		}
		val savedItem = itemRepository.save(item)
		return ResponseEntity.status(HttpStatus.CREATED).body(savedItem)
	}

	@GetMapping
	fun getAllItems(): List<Item> {
		return itemRepository.findAll()
	}
}