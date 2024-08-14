package com.springboot.AoooA.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.AoooA.dto.PasswordChangeDTO;
import com.springboot.AoooA.Entity.User;
import com.springboot.AoooA.Repository.UserRepository;
import com.springboot.AoooA.dto.MemberDTO;
import com.springboot.AoooA.service.EmailService;
import com.springboot.AoooA.service.MemberService;
import com.springboot.AoooA.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MemberService memberService; // MemberService 주입

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@GetMapping(path = { "/userPage" })
	public ModelAndView getUserPage(Principal principal) {
		List<MemberDTO> list = memberService.getIdMember(principal.getName());

		ModelAndView mav = new ModelAndView("userPage");
		mav.addObject("list", list);

		return mav;
	}

	@PostMapping("/userInfo")
	@Transactional
	public String updateUserInfoAndChangePassword(@ModelAttribute PasswordChangeDTO passwordChangeDto,
			@ModelAttribute MemberDTO memberDto, Principal principal, RedirectAttributes redirectAttributes) {
		String userEmail = principal.getName();
		boolean isPasswordChanged = false;
		boolean isUserInfoUpdated = false;

		// 비밀번호 변경 로직
		if (passwordChangeDto.getNewPassword() != null && !passwordChangeDto.getNewPassword().isEmpty()) {
			String newPassword = passwordChangeDto.getNewPassword();
			String currentPassword = passwordChangeDto.getCurrentPassword();
			String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+|~=`{}\\[\\]:\";'<>?,./]{8,}$";
			if (!newPassword.matches(passwordPattern)) {
				redirectAttributes.addFlashAttribute("message", "비밀번호는 최소 8자 이상이어야 하며, 하나 이상의 문자와 숫자를 포함해야 합니다.");
				logger.error("비밀번호 변경 실패: 비밀번호 유효성 검사 실패");
				SecurityContextHolder.clearContext();
				return "redirect:/userPage";
			}
			if (newPassword.equals(currentPassword)) {
				redirectAttributes.addFlashAttribute("message", "새 비밀번호는 현재 비밀번호와 다르게 설정해야 합니다.");
				logger.error("비밀번호 변경 실패 : 새 비밀번호가 현재 비밀번호와 동일");
				SecurityContextHolder.clearContext();
				return "redirect:/userPage";
			}

			boolean result = userService.changePassword(userEmail, passwordChangeDto);
			logger.info("접속한 Email : " + userEmail);

			if (result) {
				redirectAttributes.addFlashAttribute("message", "비밀번호가 성공적으로 변경되었습니다. 다시 로그인해주세요.");
				redirectAttributes.addFlashAttribute("logout", true);
				logger.info("비밀번호 변경 성공 : 비밀번호가 성공적으로 변경되었습니다.");

				// 로그아웃 처리
				SecurityContextHolder.clearContext();
				return "redirect:/userPage";
			} else {
				redirectAttributes.addFlashAttribute("message", "현재 비밀번호가 일치하지 않습니다.");
				redirectAttributes.addFlashAttribute("logout", false);
				logger.error("비밀번호 변경 실패 : 현재 비밀번호가 일치하지 않습니다.");
				SecurityContextHolder.clearContext();
				return "redirect:/userPage";
			}
		}

		// 사용자 정보 업데이트 로직
		if (memberDto.getUser_nickname() != null || memberDto.getUser_tel() != null
				|| memberDto.getUser_postcode() != null || memberDto.getUser_address1() != null) {
			memberDto.setUser_email(userEmail); // 이메일 설정
			int updatedRows = userService.updateUserInfo(memberDto);
			if (updatedRows > 0) {
				isUserInfoUpdated = true;
			} else {
				redirectAttributes.addFlashAttribute("errorMessage", "사용자 정보 업데이트에 실패했습니다.");
				logger.error("사용자 정보 업데이트 실패");
				return "redirect:/userPage";
			}
		}

		// 성공 메시지 처리
		if (isPasswordChanged || isUserInfoUpdated) {
			redirectAttributes.addFlashAttribute("successMessage", "개인정보가 성공적으로 변경되었습니다.");
			logger.info("변경 성공: 비밀번호 변경됨 = " + isPasswordChanged + ", 사용자 정보 변경됨 = " + isUserInfoUpdated);
		}

		return "redirect:/userPage";
	}

//  사업자 내정보페이지
	@GetMapping(path = { "/bisPage" })
	public ModelAndView getBisPage(Principal principal) {
		List<MemberDTO> list = memberService.getIdMember(principal.getName());

		ModelAndView mav = new ModelAndView("bisPage");
		mav.addObject("list", list);

		return mav;
	}

	@PostMapping("/bisInfo")
	@Transactional
	public String updateBisInfoAndChangePassword(@ModelAttribute PasswordChangeDTO passwordChangeDto,
			@ModelAttribute MemberDTO memberDto, Principal principal, RedirectAttributes redirectAttributes) {
		String userEmail = principal.getName();
		boolean isPasswordChanged = false;
		boolean isUserInfoUpdated = false;

		// 비밀번호 변경 로직
		if (passwordChangeDto.getNewPassword() != null && !passwordChangeDto.getNewPassword().isEmpty()) {
			String newPassword = passwordChangeDto.getNewPassword();
			String currentPassword = passwordChangeDto.getCurrentPassword();
			String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+|~=`{}\\[\\]:\";'<>?,./]{8,}$";
			if (!newPassword.matches(passwordPattern)) {
				redirectAttributes.addFlashAttribute("message", "비밀번호는 최소 8자 이상이어야 하며, 하나 이상의 문자와 숫자를 포함해야 합니다.");
				logger.error("비밀번호 변경 실패: 비밀번호 유효성 검사 실패");
				SecurityContextHolder.clearContext();
				return "redirect:/bisPage";
			}
			if (newPassword.equals(currentPassword)) {
				redirectAttributes.addFlashAttribute("message", "새 비밀번호는 현재 비밀번호와 다르게 설정해야 합니다.");
				logger.error("비밀번호 변경 실패 : 새 비밀번호가 현재 비밀번호와 동일");
				SecurityContextHolder.clearContext();
				return "redirect:/bisPage";
			}

			boolean result = userService.changePassword(userEmail, passwordChangeDto);
			logger.info("접속한 Email : " + userEmail);

			if (result) {
				redirectAttributes.addFlashAttribute("message", "비밀번호가 성공적으로 변경되었습니다. 다시 로그인해주세요.");
				redirectAttributes.addFlashAttribute("logout", true);
				logger.info("비밀번호 변경 성공 : 비밀번호가 성공적으로 변경되었습니다.");

				// 로그아웃 처리
				SecurityContextHolder.clearContext();
				return "redirect:/bisPage";
			} else {
				redirectAttributes.addFlashAttribute("message", "현재 비밀번호가 일치하지 않습니다.");
				redirectAttributes.addFlashAttribute("logout", false);
				logger.error("비밀번호 변경 실패 : 현재 비밀번호가 일치하지 않습니다.");
				SecurityContextHolder.clearContext();
				return "redirect:/bisPage";
			}
		}

		// 사용자 정보 업데이트 로직
		if (memberDto.getUser_nickname() != null || memberDto.getUser_tel() != null
				|| memberDto.getUser_postcode() != null || memberDto.getUser_address1() != null) {
			memberDto.setUser_email(userEmail); // 이메일 설정
			int updatedRows = userService.updateUserInfo(memberDto);
			if (updatedRows > 0) {
				isUserInfoUpdated = true;
			} else {
				redirectAttributes.addFlashAttribute("errorMessage", "사용자 정보 업데이트에 실패했습니다.");
				logger.error("사용자 정보 업데이트 실패");
				return "redirect:/bisPage";
			}
		}

		// 성공 메시지 처리
		if (isPasswordChanged || isUserInfoUpdated) {
			redirectAttributes.addFlashAttribute("successMessage", "개인정보가 성공적으로 변경되었습니다.");
			logger.info("변경 성공: 비밀번호 변경됨 = " + isPasswordChanged + ", 사용자 정보 변경됨 = " + isUserInfoUpdated);
		}

		return "redirect:/bisPage";
	}

	@GetMapping("passwordpopup")
    public ModelAndView passwordpopup() {
       
       ModelAndView mav = new ModelAndView("passwordpopup");
       
       
       return mav;
    }
    
    @PostMapping("/passwordpopup")
    public ModelAndView sendTemporaryPassword(@RequestParam("user_name") String userName,
             @RequestParam("user_tel") String userTel,@RequestParam("user_email") String userEmail) {
       Logger logger = LoggerFactory.getLogger(this.getClass());
       ModelAndView mav = new ModelAndView("passwordpopup");

         User user = userRepository.findByUserNameAndUserTelAndUserEmail(userName, userTel, userEmail);
         if (user != null) {
             String temporaryPassword = userService.generateTemporaryPassword();

             // 임시 비밀번호를 사용자의 비밀번호로 변경
             userService.changePassword1(userEmail, temporaryPassword);
             System.out.println("임시비밀번호 : " + temporaryPassword);

             // 변경된 비밀번호를 이메일로 전송
             emailService.sendTemporaryPassword(userEmail, temporaryPassword);
             logger.info("임시 비밀번호를 사용자 {}에게 전송하고 비밀번호를 변경하였습니다.", userEmail);

             mav.addObject("message", "비밀번호 재설정에 성공했습니다. 이메일을 확인해주세요.");
         } else {
             // 세부적인 오류 메시지 확인
             user = userRepository.findByUserEmail(userEmail);
             if (user == null) {
                 mav.addObject("message", "이메일이 존재하지 않습니다. 다시 시도해주세요");
             } else if (!user.getUserName().equals(userName)) {
                 mav.addObject("message", "이름이 일치하지 않습니다. 다시 시도해주세요");
             } else if (!user.getUserTel().equals(userTel)) {
                 mav.addObject("message", "전화번호가 일치하지 않습니다. 다시 시도해주세요");
             } else {
                 mav.addObject("message", "입력하신 정보로 사용자를 찾을 수 없습니다. 다시 시도해주세요");
             }
         }
         return mav;
     }

}