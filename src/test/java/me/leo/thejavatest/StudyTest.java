package me.leo.thejavatest;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {

	@Test
	@DisplayName("스터디 만들기 첫 번째")
	void create() throws Exception {
		//given
		Study study = new Study();

		//when

		//then
		assertNotNull(study);
		System.out.println("create");
	}

	@Test
	@DisplayName("스터디 만들기 두 번째")
	void createTwo() throws Exception {
		//given
		Study study = new Study();

		//when

		//then
		assertNotNull(study);
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