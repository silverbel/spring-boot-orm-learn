package io.honeymon.learn.orm.view;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.honeymon.learn.orm.domain.Member;
import io.honeymon.learn.orm.service.MemberService;

@Controller
@RequestMapping("members")
public class MemberViewController {
    @Autowired
    private MemberService memberService;

    @ModelAttribute("memberForm")
    public MemberForm memberForm() {
        return new MemberForm();
    }

    @GetMapping
    public String viewMain(MemberCondition condition, Pageable pageable, Model model) {
        model.addAttribute("page", memberService.search(condition, pageable));
        return "members/main";
    }

    /**
     * 회원 생성 화면
     * 
     * @param memberForm
     * @return
     */
    @GetMapping("/create")
    public String viewCreate(MemberForm memberForm) {
        return "members/detail";
    }

    @PostMapping
    public String createMember(@Valid MemberForm memberForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // TODO 에러발생한 것에 대한 처리
            return "members/detail";
        }
        memberService.save(memberForm.createEntity());
        return "redirect:/members";
    }

    @GetMapping("/{member}")
    public String viewModifyMember(@PathVariable Member member, MemberForm memberForm, Model model) {
        model.addAttribute("member", member);
        memberForm.setName(member.getName());
        memberForm.setEmail(member.getEmail());
        return "members/detail";
    }

    @PutMapping("/{member}")
    public String modifyMember(@PathVariable Member member, @Valid MemberForm memberForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "members/detail";
        }

        member.setName(memberForm.getName());
        member.setEmail(memberForm.getEmail());
        memberService.save(member);

        return "redirect:/members";
    }
}
