package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // final이 붙은 모든 필드의 생성자 injection을 만들어 줌
public class MemberService {

    private final MemberRepository memberRepository;

    // 필드 injection 보단 생성자 injection을 사용하자!
    // 생성자 호출시점에 딱 1번만 호출되는 것이 보장되며, 불변, 필수 의존관계에 사용
    // @Autowired // memberRepository injection
    // public MemberService(MemberRepository memberRepository) {
    //     this.memberRepository = memberRepository;
    // }

    /**
     * 회원 가입
     */
    @Transactional // JPA의 모든 데이터 변경과 로직은 한 트랜잭션 안에서 이뤄져야 함 (default는 readOnly = false)
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    public void validateDuplicateMember(Member member) {
        // exception
        List<Member> findMembers = memberRepository.findByName(member.getName()); // 최후의 방어로 member의 name을 db에서 unique 제약 조건을 주는 것을 권장
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
     */
    @Transactional(readOnly = true) // 데이터의 변경이 없는 읽기 전용 메서드에 사용, 영속성 컨텍스트를 플러시 하지 않으므로 약간의 성능 향상이 있음 (읽기에는 다 적용하는 것을 권장)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 단건 조회
     */
    @Transactional(readOnly = true)
    public Member findOne(Long id) {
        return memberRepository.findOne(id);
    }
}
