package me.leo.thejavatest;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class StudyTest {

	@Test
	void create() throws Exception {
		//given
		Study study = new Study();

		//when

		//then
		assertNotNull(study);
		System.out.println("create");
	}

	@Test
	@Disabled
	void createTwo() throws Exception {
		System.out.println("create Two");
	}

	@BeforeAll
	static void beforeAll() {
		System.out.println("before all");
	}

	@AfterAll
	static void afterAll() {
		System.out.println("after all");
	}

	@BeforeEach
	void beforeEach() {
		System.out.println("before each");
	}

	@AfterEach
	void afterEach() {
		System.out.println("after each");
	}
}