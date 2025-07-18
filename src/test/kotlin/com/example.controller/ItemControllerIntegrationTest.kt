package com.example.controller

import com.example.repository.ItemRepository
import com.example.entity.Item
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemControllerIntegrationTest {

	@LocalServerPort
	private var port: Int = 0

	@Autowired
	private lateinit var restTemplate: TestRestTemplate

	@Autowired
	private lateinit var itemRepository: ItemRepository

	private fun getBaseUrl(): String = "http://localhost:$port/items"

	@BeforeEach
	fun setUp() {
		itemRepository.deleteAll()
	}

	@Test
	fun `test create new item successfully`() {
		val item = Item(name = "magic sword", description = "a mighty sword")
		val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
		val request = HttpEntity(item, headers)

		val response = restTemplate.postForEntity(getBaseUrl(), request, Item::class.java)

		assertEquals(HttpStatus.CREATED, response.statusCode)
		assertEquals(item.name, response.body?.name)
		assertEquals(item.description, response.body?.description)
	}

	@Test
	fun `test create duplicate item returns conflict`() {
		// Primeiro, cria um item
		val item = Item(name = "iron shield", description = "a sturdy shield")
		val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
		val request = HttpEntity(item, headers)
		restTemplate.postForEntity(getBaseUrl(), request, Item::class.java)

		// Tenta criar o mesmo novamente
		val duplicateResponse = restTemplate.postForEntity(getBaseUrl(), request, Item::class.java)

		assertEquals(HttpStatus.CONFLICT, duplicateResponse.statusCode)
	}

	@Test
	fun `test get all items after creation`() {
		// Cria um item para testar
		val item = Item(name = "health potion", description = "restores health")
		val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
		val request = HttpEntity(item, headers)
		restTemplate.postForEntity(getBaseUrl(), request, Item::class.java)

		val listResponse = restTemplate.getForEntity(getBaseUrl(), Array<Item>::class.java)

		assertEquals(HttpStatus.OK, listResponse.statusCode)
		assertEquals(1, listResponse.body?.size)
		assertEquals(item.name, listResponse.body?.first()?.name)
	}
}