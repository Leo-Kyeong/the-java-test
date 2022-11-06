package me.leo.thejavatest.study;

import me.leo.thejavatest.domain.Member;
import me.leo.thejavatest.domain.Study;
import me.leo.thejavatest.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

	@Mock
	MemberService memberService;
	@Mock
	StudyRepository studyRepository;

	@Test
	void createStudyService() {
		Member member = new Member();
		member.setId(1L);
		member.setEmail("leo@email.com");

		StudyService studyService = new StudyService(memberService, studyRepository);
		assertNotNull(studyService);

		// Mockito.when(memberService.findById(1L)).thenReturn(Optional.of(member));
		// Mockito.when(memberService.findById(any())).thenReturn(Optional.of(member));
		Mockito.doReturn(Optional.of(member)).when(memberService).findById(any());

		assertEquals("leo@email.com", memberService.findById(1L).get().getEmail());
		assertEquals("leo@email.com", memberService.findById(2L).get().getEmail());

		// memberService.findById(1L) 이 호출되면 예외를 던진다.
		Mockito.when(memberService.findById(1L)).thenThrow(new RuntimeException());
		assertThrows(RuntimeException.class, () -> memberService.findById(1L));

		// memberService 의 validate 가 호출되면 예외를 던진다.
		Mockito.doThrow(new IllegalArgumentException()).when(memberService).validatate(anyLong());
		assertThrows(IllegalArgumentException.class, () -> memberService.validatate(2L));
	}

	@Test
	void createStudyService1() {
		Member member = new Member();
		member.setId(1L);
		member.setEmail("leo@email.com");

		Mockito.when(memberService.findById(any()))
				.thenReturn(Optional.of(member)) // 첫 번째 호출
				.thenReturn(Optional.empty()) // 두 번째 호출
				.thenThrow(new RuntimeException("예외 발생")); // 세 번째 호출

		Optional<Member> findMember = memberService.findById(1L);
		assertEquals("leo@email.com",  findMember.get().getEmail());

		assertEquals(Optional.empty(), memberService.findById(2L));

		assertThrows(RuntimeException.class, () -> {
			memberService.findById(3L);
		});
	}

	@Test
	void example() {
		//given
		Member member = new Member();
		member.setId(1L);
		member.setEmail("leo@email.com");

		Study study = new Study(10, "테스트");

		StudyService studyService = new StudyService(memberService, studyRepository);

		BDDMockito.given(memberService.findById(1L)).willReturn(Optional.of(member));
		BDDMockito.given(studyRepository.save(study)).willReturn(study);

		//when
		studyService.createNewStudy(1L, study);

		//then
		assertAll(
			() -> assertNotNull(study.getOwner()),
			() -> assertEquals(member, study.getOwner())
		);
		BDDMockito.then(memberService).should(Mockito.times(1)).notify(study);
		BDDMockito.then(memberService).shouldHaveNoMoreInteractions();
	}

	@Test
	void notifyTest() {
		Member member = new Member();
		member.setId(1L);
		member.setEmail("leo@email.com");

		Study study = new Study(10, "테스트");

		StudyService studyService = new StudyService(memberService, studyRepository);
		studyService.notify(member, study);

		Mockito.verify(memberService, Mockito.times(1)).notify(study);
		Mockito.verify(memberService, Mockito.times(1)).notify(member);
		Mockito.verify(memberService, Mockito.timeout(100).atLeast(1)).notify(member);
		Mockito.verify(memberService, Mockito.never()).validatate(any());
	}

	@Test
	void notifyTest2() {
		Member member = new Member();
		member.setId(1L);
		member.setEmail("leo@email.com");

		Study study = new Study(10, "테스트");

		StudyService studyService = new StudyService(memberService, studyRepository);
		studyService.notify(member, study);

		InOrder inOrder = Mockito.inOrder(memberService);
		inOrder.verify(memberService).notify(study);
		inOrder.verify(memberService).notify(member);

		Mockito.verifyNoMoreInteractions(memberService);
	}

	@DisplayName("다른 사용자가 볼 수 있도록 스터디를 공개한다.")
	@Test
	void openStudy() {
	    //given
		StudyService studyService = new StudyService(memberService, studyRepository);
		Study study = new Study(10, "테스트");
		assertNull(study.getOpenedDateTime());
		// TODO: 2022-11-06 Mock 객체의 save 메소드를 호출 시 study 를 return 하도록 만들기
		BDDMockito.given(studyRepository.save(study)).willReturn(study);

	    //when
		Study openStudy = studyService.openStudy(study);
		//then
		// TODO: 2022-11-06 study 의 status 가 OPENED 로 변경됐는지 확인
		assertEquals(StudyStatus.OPENED, openStudy.getStatus());
		// TODO: 2022-11-06 study openedDataTime 이 null 이 아닌지 확인
		assertNotNull(openStudy.getOpenedDateTime());
		// TODO: 2022-11-06 memberService 의 notify(study) 가 호출 됐는지 확인
		BDDMockito.then(memberService).should(Mockito.times(1)).notify(openStudy);
	}
}
