package com.springboot.AoooA.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.springboot.AoooA.dto.MemberDTO;
import com.springboot.AoooA.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LayoutInterceptor implements HandlerInterceptor {
	@Autowired
	private MemberService memberService;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (modelAndView != null && !isResourceHandler(handler)) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null && authentication.isAuthenticated()) {
				String userEmail = authentication.getName();
				// 사용자 정보를 가져오는 서비스 로직을 호출합니다. 예를 들어, getUserByEmail 메소드 등을 사용합니다.
				MemberDTO member = memberService.selectUserEmail(userEmail);
				if (member != null) {
					// 사용자 타입을 모델에 추가합니다.
					modelAndView.addObject("userType", member.getUser_type());
					modelAndView.addObject("userNickName", member.getUser_nickname());
					modelAndView.addObject("userName", member.getUser_name());
				}
			}
		}
	}

	private boolean isResourceHandler(Object handler) {
		// 정적 리소스에 대한 요청은 처리하지 않도록 합니다.
		return handler.toString().contains("ResourceHttpRequestHandler");
	}
}
