package me.leo.thejavatest;

import lombok.extern.slf4j.Slf4j;
import me.leo.thejavatest.domain.Study;
import me.leo.thejavatest.study.FindSlowTestExtension;
import me.leo.thejavatest.study.StudyStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudyTest {

	int value = 1;

	@RegisterExtension
	static FindSlowTestExtension findSlowTestExtension =
			new FindSlowTestExtension(1000L);

	@Test
	@Order(2)
	void test1() {
		System.out.println(this);
		System.out.println(value++);
	}

	@Test
	@Order(1)
	void test2() throws InterruptedException {
		Thread.sleep(1005L);
		System.out.println(this);
		System.out.println(value++);
	}

	@Test
	@Disabled
	@EnabledOnOs({OS.MAC, OS.WINDOWS})
	@EnabledOnJre({JRE.JAVA_9, JRE.JAVA_10})
	@EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "LOCAL")
	public void create_new_study() throws Exception {
		// 해당 환경 변수의 값을 가져온다.
		 String test_env = System.getenv("TEST_ENV");
		 System.out.println(test_env);
		// 환경 변수의 값이 LOCAL 일 경우 아래 테스트 진행
		 Assumptions.assumeTrue("LOCAL".equalsIgnoreCase(test_env));

		Study study = new Study(10);
		assertNotNull(study);

		Assumptions.assumingThat("LOCAL".equalsIgnoreCase(test_env), () -> {
			System.out.println("LOCAL");
			assertNotNull(study);
		});

		Assumptions.assumingThat("DEV".equalsIgnoreCase(test_env), () -> {
			System.out.println("DEV");
			assertNotNull(study);
		});
	}

	@Test
	@DisplayName("스터디 만들기 첫 번째")
	void create() throws Exception {
		//given
		Study study = new Study(10);

		//when

		//then
		assertAll(
			() -> assertNotNull(study),
			() -> assertEquals(StudyStatus.DRAFT, study.getStatus(),
					() -> "스터디를 처음 만들면 상태 값이 " + StudyStatus.DRAFT + " 상태다."),
			() -> assertTrue(study.getLimitCount() > 0,
					"스터디 최대 참석 가능 인원은 0보다 커야 한다.")
		);
	}

	@Test
	@DisplayName("스터디 만들기 두 번째")
	void createTwo() throws Exception {
		//given

		//when

		//then
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> new Study(-10));
		String message = ex.getMessage();
		// assertEquals("limit 은 0 보다 커야 한다.", message);
	}

	@Test
	@Disabled
	@DisplayName("스터디 만들기 세 번째")
	void createThree() throws Exception {
		//given

		//when

		//then
		assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
			new Study(10);
			Thread.sleep(300);
		});
		// assertTimeoutPreemptively 는 Thread 를 사용한다.
		// Thread 공유가 안되는 ThreadLocal 를 사용하는 코드는 에러가 발생한다.
		// ThreadLocal 를 사용하는 코드는 assertTimeout 사용
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
