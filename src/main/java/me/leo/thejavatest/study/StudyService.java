package me.leo.thejavatest.study;

import me.leo.thejavatest.domain.Study;
import me.leo.thejavatest.domain.Member;
import me.leo.thejavatest.member.MemberService;

import java.util.Optional;

public class StudyService {

    private final MemberService memberService;

    private final StudyRepository studyRepository;

    public StudyService(MemberService memberService, StudyRepository repository) {
        assert  memberService != null;
        assert  repository != null;
        this.memberService = memberService;
        this.studyRepository = repository;
    }

    public Study createNewStudy(Long memberId, Study study) {
        Optional<Member> member = memberService.findById(memberId);
        if (member.isEmpty()) {
            throw new IllegalArgumentException("Member doesn't exist for id: '" + memberId);
        }
        study.setOwner(member.get());
        Study newStudy = studyRepository.save(study);
        memberService.notify(newStudy); // 새로운 스터디 오픈 알림
        memberService.notify(member.get()); // 해당 인원에게 직접 알림
        return newStudy;
    }

    public void notify(Member member, Study newStudy) {
        memberService.notify(newStudy); // 새로운 스터디 오픈 알림
        memberService.notify(member); // 해당 인원에게 직접 알림
    }

    public Study openStudy(Study study) {
        study.open(); // 상태 변경(OPENED), 스터디 오픈 시간 설정
        Study openedStudy = studyRepository.save(study); // 스터디 저장
        memberService.notify(openedStudy); // 새로운 스터디 오픈 알림
        return openedStudy;
    }
}
