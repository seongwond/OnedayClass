package com.springboot.AoooA.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.springboot.AoooA.Entity.Reservation;
import com.springboot.AoooA.config.EmailValidator;
import com.springboot.AoooA.dto.ClassDTO;
import com.springboot.AoooA.dto.HeartDTO;
import com.springboot.AoooA.dto.ImgDTO;
import com.springboot.AoooA.dto.MemberDTO;
import com.springboot.AoooA.dto.ReservationDTO;
import com.springboot.AoooA.dto.ReviewDTO;
import com.springboot.AoooA.service.CategoryService;
import com.springboot.AoooA.service.ClassService;
import com.springboot.AoooA.service.FileService;
import com.springboot.AoooA.service.HeartService;
import com.springboot.AoooA.service.MemberService;
import com.springboot.AoooA.service.ReservationServiceImpl;
import com.springboot.AoooA.service.ReviewService;
import com.springboot.AoooA.service.UserSecurityService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
	private final CategoryService categoryService;
	private final UserSecurityService userService;
	private final MemberService memberService;
	private final ClassService classService;
	private final ReservationServiceImpl reservationService;
	private final HeartService heartService;
	private final ReviewService reviewService;
	private final FileService service;

	private static final String REPOSITORY_DIR = "C:\\Users\\Administrator\\Desktop\\AoooA\\AoooA\\src\\main\\resources\\static\\img\\";
	private static final String DEFAULT_IMAGE_DIR = "C:\\Users\\Administrator\\Desktop\\AoooA\\AoooA\\src\\main\\resources\\static\\img\\";

	@Autowired
	public MainController(CategoryService categoryService, UserSecurityService userService, MemberService memberService,
			ClassService classService, ReservationServiceImpl reservationService, HeartService heartService,
			ReviewService reviewService, FileService service ) {
		this.categoryService = categoryService;
		this.userService = userService;
		this.memberService = memberService;
		this.classService = classService;
		this.reservationService = reservationService;
		this.heartService = heartService;
		this.reviewService = reviewService;
		this.service = service;
	}

	@GetMapping(path = { "/" })
	public ModelAndView home() {
		List<Integer> classIds = classService.selectTypeClass();
		List<Integer> classIdss = classService.selectBestinfo();

		List<ImgDTO> combinedList = new ArrayList<>(); // 모든 결과를 담을 리스트
		List<ImgDTO> combinedList2 = new ArrayList<>();

		for (Integer classId1 : classIds) {
			ImgDTO list = categoryService.getClassTitles(classId1);
			combinedList.add(list); // 각 class_id에 대한 결과 추가
		}

		for (Integer classId2 : classIdss) {
			ImgDTO list2 = categoryService.getClassTitles(classId2);
			combinedList2.add(list2); // 각 class_id에 대한 결과 추가
		}

		ModelAndView mav = new ModelAndView("index");

		mav.addObject("list", combinedList);
		mav.addObject("list2", combinedList2);

		return mav;

	}

//   로그인(사업자, 유저구분)
	@GetMapping(path = { "/loginMain.do" })
	public String loginMain() {
		return "loginMain";
	}

//   회원가입(사업자, 유저선택)
	@GetMapping(path = { "/joinMain" })
	public String joinMain() {
		return "joinMain.html";
	}

//   회원가입 완료페이지
	@GetMapping(path = { "/joinFin" })
	public String joinFin() {
		return "joinFin.html";
	}

//   유저 내정보페이지
	@GetMapping(path = { "/userPage2" })
	public ModelAndView getUserPage2(Principal principal, @RequestParam(value = "page", defaultValue = "1") int page) {
		List<MemberDTO> list = memberService.getIdMember(principal.getName());

		// 사용자 아이디 가져오기
		String userEmail = principal.getName();
		int userId = reservationService.selectUserId(userEmail);

		// 사용자가 예약한 클래스 상세 정보 가져오기
		List<ImgDTO> combinedList = reservationService.getReservationClassDetails(userId);

		// 예약 날짜 가져오기
		Map<Integer, Map<String, Object>> reg = reservationService.selectReservationDate3(userId);

		// 페이지네이션 처리 (수정 필요)
		int rows = 3; // 행 수
		int cols = 2; // 열 수
		int totalClasses = combinedList.size(); // 총 클래스 수
		int totalPages = (int) Math.ceil((double) totalClasses / (rows * cols)); // 전체 페이지 수
		int startIndex = (page - 1) * (rows * cols); // 페이지의 시작 인덱스
		int endIndex = Math.min(startIndex + (rows * cols), totalClasses); // 페이지의 끝 인덱스

		// 현재 페이지에 보여줄 클래스 리스트
		List<ImgDTO> pageList = combinedList.subList(startIndex, endIndex);

		ModelAndView mav = new ModelAndView("userPage2");
		mav.addObject("list", list);
		mav.addObject("rlist", pageList);
		mav.addObject("reglist", reg);
		mav.addObject("totalPages", totalPages);
		mav.addObject("currentPage", page);

		return mav;
	}

//  유저 내정보페이지(예약내역 정보)
	@GetMapping("/userPage3")
	public ModelAndView userPage3(Principal principal, @RequestParam(value = "page", defaultValue = "1") int page) {
		ModelAndView mav = new ModelAndView("userPage3");

		List<MemberDTO> list = memberService.getIdMember(principal.getName());

		// 클래스 아이디 찾기
		String userEmail = principal.getName();
		int userId = reservationService.selectUserId(userEmail);

		// 좋아요한 클래스 목록 가져오기
		List<ImgDTO> combinedList = reservationService.getReservationClassDetailsx(userId);

		Map<Integer, Map<String, Object>> reg = reservationService.selectReservationDate(userId);

		// 페이지네이션 처리
		int pageSize = 2; // 한 페이지에 보여줄 클래스 수
		int totalClasses = combinedList.size(); // 총 클래스 수
		int totalPages = (int) Math.ceil((double) totalClasses / pageSize); // 전체 페이지 수
		int startIndex = (page - 1) * pageSize; // 페이지의 시작 인덱스
		int endIndex = Math.min(startIndex + pageSize, totalClasses); // 페이지의 끝 인덱스

		// 현재 페이지에 보여줄 클래스 리스트
		List<ImgDTO> pageList = combinedList.subList(startIndex, endIndex);

		mav.addObject("list", list);
		mav.addObject("rlist", pageList);
		mav.addObject("reglist", reg);
		mav.addObject("totalPages", totalPages);
		mav.addObject("currentPage", page);

		return mav;
	}

//  유저 내정보페이지(예약내역 정보 삭제)
	@PostMapping(path = "/userPage3")
	public ModelAndView deleteSelectedReservations(Principal principal,
			@RequestParam("reservation_id") List<Integer> reservation_id,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		for (int id : reservation_id) {
			System.out.println("reserv_id=" + id);
			reservationService.deleteReservationById(id);
		}

		return userPage3(principal, page);
	}

//  유저 내정보페이지
	@GetMapping(path = { "/userPage4" })
	public ModelAndView getUserPage4(Principal principal) {
		if (principal == null) {
			// Principal이 null인 경우 홈 페이지로 리다이렉트
			return new ModelAndView("redirect:/");
		}
		List<MemberDTO> list = memberService.getIdMember(principal.getName());

		// 사용자 관심사 정보를 가져와서 모델에 추가
		MemberDTO interest = memberService.viewInterest(principal.getName());

		ModelAndView mav = new ModelAndView("userPage4");
		mav.addObject("list", list);
		mav.addObject("inter", interest); // 관심사 정보 추가

		return mav;
	}

	@GetMapping(path = { "/userInterests" })
	@ResponseBody
	public ResponseEntity<?> getUserInterests(Principal principal) {
		if (principal == null) {
			// Principal이 null인 경우 홈 페이지로 리다이렉트
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
		}
		// 사용자의 관심사 정보를 반환
		MemberDTO interest = memberService.viewInterest(principal.getName());
		return ResponseEntity.ok(interest);
	}

	// 사용자 관심사 업데이트
	@PostMapping("/updateUserInterests")
	@ResponseBody
	public ResponseEntity<String> updateUserInterests(Principal principal, @RequestBody MemberDTO dto) {
		if (principal == null) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
		}

		System.out.println(dto.getInterest_type1());
		System.out.println(dto.getInterest_type2());
		System.out.println(dto.getInterest_type3());
		memberService.updateInterest(principal.getName(), dto);

		return ResponseEntity.ok("ok");

	}

	@GetMapping(path = { "/userPage5" })
	public ModelAndView getUserPage5(Principal principal, @RequestParam(value = "page", defaultValue = "1") int page) {
		List<MemberDTO> list = memberService.getIdMember(principal.getName());

		// 클래스 아이디 찾기
		String userEmail = principal.getName();
		int userId = heartService.selectUserId(userEmail);

		// 좋아요한 클래스 목록 가져오기
		List<ImgDTO> combinedList = heartService.getMyHeartClassDetails(userId);

		// 페이지네이션 처리
		int pageSize = 6; // 한 페이지에 보여줄 클래스 수
		int totalClasses = combinedList.size(); // 총 클래스 수
		int totalPages = (int) Math.ceil((double) totalClasses / pageSize); // 전체 페이지 수
		int startIndex = (page - 1) * pageSize; // 페이지의 시작 인덱스
		int endIndex = Math.min(startIndex + pageSize, totalClasses); // 페이지의 끝 인덱스

		// 현재 페이지에 보여줄 클래스 리스트
		List<ImgDTO> pageList = combinedList.subList(startIndex, endIndex);

		ModelAndView mav = new ModelAndView("userPage5");
		mav.addObject("list", list);
		mav.addObject("ilist", pageList);
		mav.addObject("totalPages", totalPages);
		mav.addObject("currentPage", page);

		return mav;
	}

	// 5/28 수정
	// 유저 리뷰작성페이지
	@GetMapping("/review")
	public ModelAndView review(Principal principal, @RequestParam("reservation_id") Integer reservation_id,
			HttpSession session) {
		ModelAndView mav = new ModelAndView("review");

		// 유저아이디 꺼내오기
		String userEmail = principal.getName();
		int userId = reviewService.getUserIdByEmail(userEmail);

		// reservation_id에 맞는 값으로 다시 불러오기
		ReviewDTO reservation = reviewService.getReservationById(reservation_id);

		// reservation이 null인 경우 오류 처리
		if (reservation == null) {
			mav.setViewName("error");
			mav.addObject("message", "유효하지 않은 예약 정보입니다.");
			return mav;
		}

		// 클래스 아이디에 맞는 클래스 정보 가져오기
		ImgDTO classInfo = reviewService.getClassTitle(reservation.getClass_id());

		// reservation_id에 맞는 클래스 이미지와 제목 가져오기
		List<ImgDTO> combinedList = reviewService.getAllReviewList(reservation.getClass_id());
		if (combinedList == null) {
			combinedList = new ArrayList<>();
		}

		// 예약 날짜 정보 가져오기
		List<Map<String, Object>> reg = reviewService.getReservationDates(userId);

		// 세션에 reservation_id 저장
		session.setAttribute("reservation_id", reservation_id);

		mav.addObject("reservation", reservation);
		mav.addObject("classInfo", classInfo);
		mav.addObject("combinedList", combinedList);
		mav.addObject("reglist", reg);
		return mav;
	}

	@PostMapping("/review")
	public ModelAndView saveReview(Principal principal, @RequestParam("class_star") int classStar,
			@RequestParam("review_content") String reviewContent, HttpSession session) {
		ModelAndView mav = new ModelAndView("review");

		// 유저 정보 가져오기
		String userEmail = principal.getName();
		int userId = reviewService.getUserIdByEmail(userEmail);

		// 세션에서 reservation_id 가져오기
		Integer reservation_id = (Integer) session.getAttribute("reservation_id");

		// reservation_id가 null인 경우 오류 처리
		if (reservation_id == null) {
			mav.addObject("message", "세션에 예약 정보가 없습니다.");
			return mav;
		}

		// reservation_id에 해당하는 예약 정보 가져오기
		ReviewDTO reservation = reviewService.getReservationById(reservation_id);

		// reservation이 null인 경우 오류 처리
		if (reservation == null) {
			mav.addObject("message", "유효하지 않은 예약 정보입니다.");
			return mav;
		}

		// reservation에서 class_id 가져오기
		int classId = reservation.getClass_id();

		// 동일한 user_id와 class_id로 예약이 존재하는지 확인
		int reservationCount = reviewService.countReservationByUserAndClass(userId, classId);
		if (reservationCount > 0) {
			mav.addObject("message", "이미 이 클래스에 대한 리뷰를 작성하셨습니다.");
			return mav;
		}

		// ReviewDTO 생성 및 값 설정
		ReviewDTO reviewDTO = new ReviewDTO();
		reviewDTO.setUser_id(userId);
		reviewDTO.setClass_id(classId); // 클래스 아이디 설정
		reviewDTO.setClass_star(classStar);
		reviewDTO.setReview_content(reviewContent);
		reviewDTO.setReservation_id(reservation_id);

		// 리뷰 저장
		reviewService.insertReview(reviewDTO);

		mav.addObject("message", "리뷰가 성공적으로 저장되었습니다.");
		return mav;
	}

	@GetMapping(path = { "/bisPage2" })
	public ModelAndView bisPage2(Principal principal) {
		List<MemberDTO> list = memberService.getIdMember(principal.getName());

		// 클래스 아이디 찾기
		String userEmail = principal.getName();
		int userId = reservationService.selectUserId(userEmail);
		int classId = reservationService.selectClassId(userId);

		//
		List<ImgDTO> combinedList = reservationService.getReservationClassDetailss(userId);
		List<ReservationDTO> rllist = reservationService.selectViewthis(classId);

		ModelAndView mav = new ModelAndView("bisPage2");
		mav.addObject("list", list);
		mav.addObject("rlist", combinedList);
		mav.addObject("rllist", rllist);

		return mav;
	}

	@GetMapping("/classPage/{id}")
	public ModelAndView classPage(@PathVariable("id") Integer classId, ReservationDTO reservationDTO,
	                              ReviewDTO reviewDTO, ImgDTO imgDTO) {
	    ModelAndView mav = new ModelAndView();
	    
	    try {
	        // 클래스 정보 추가
	        ClassDTO classList = classService.getClassList(classId);
	        mav.addObject("classlist", classList);
	        System.out.println(classList);

	        // 리뷰 정보 추가
	        List<ReviewDTO> reviews = reviewService.getReview(classId);
	        mav.addObject("reviews", reviews);
	        System.out.println("reviews :" + reviews);
	
	        // 현재 시간 타임스탬프 추가
	     //   long timestamp = System.currentTimeMillis();
	      //  mav.addObject("timestamp", timestamp);

	        // 뷰 이름 설정
	        mav.setViewName("classPage");

	    } catch (Exception e) {
	        mav.addObject("errorMessage", "클래스 등록 중 오류가 발생했습니다: " + e.getMessage());
	        mav.setViewName("classPage");
	        e.printStackTrace();
	    }
	    return mav;
	}

	@GetMapping("/api/classInfo/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getClassInfo(@PathVariable("id") Integer classId) {
		Map<String, Object> response = new HashMap<>();
		HttpStatus status = HttpStatus.OK;

		try {
			Map<String, Object> classInfo = classService.getClassInfo(classId); // 메서드 이름 수정
			response.put("data", classInfo);
			System.out.println(classInfo);
		} catch (Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			response.put("errorMessage", "클래스 정보 조회 중 오류가 발생했습니다.");
		}

		return new ResponseEntity<>(response, status);
	}

	@PostMapping("/classPage/{id}")
	ModelAndView classPage(Reservation rvForm) {
		// reservationService.insertRv(rvForm);
		
		ModelAndView mav = new ModelAndView();
		// mav.setViewName("redirect:/regCheck");
		return mav;
	}

	@PostMapping("/save-reservation/{id}")
	public ResponseEntity<String> saveReservation(@RequestBody ReservationDTO reservationDTO, Principal principal,
			@PathVariable("id") Integer classId, @RequestParam("reservation_date") String reservationDate) {
		System.out.println("Class ID: " + classId);
		reservationDTO.setClass_id(classId);

		try {
			String userEmail = principal.getName();
			System.out.println("User Email: " + userEmail);

			// 사용자 아이디 가져오기
			int userId = reservationService.selectUserId(userEmail);
			System.out.println("User ID: " + userId);

			// 중복 예약 여부 확인
			if (reservationService.isClassAlreadyReserved(userId, classId, reservationDate)) {
				System.out.println("Already reserved");

				return ResponseEntity.status(HttpStatus.CONFLICT).body("Already reserved");
			}

			// DTO에서 받은 예약 정보를 Entity로 변환
			Reservation reservation = convertToEntity(reservationDTO, principal);
			// 예약 정보를 서비스를 통해 저장
			reservationService.insertRv(reservation);
			return ResponseEntity.ok("Reservation saved successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to save reservation: " + e.getMessage());
		}
	}

	@GetMapping("/api/check-reservation/{classId}")
	public ResponseEntity<Map<String, Boolean>> checkReservation(Principal principal,
			@PathVariable("classId") Integer classId, @PathVariable("reservation_date") String reservation_date) {
		Map<String, Boolean> response = new HashMap<>();
		Integer userId = getUserId(principal.getName());

		boolean isReserved = reservationService.isClassAlreadyReserved(userId, classId, reservation_date);
		response.put("reserved", isReserved);

		return ResponseEntity.ok(response);
	}

	private Reservation convertToEntity(ReservationDTO reservationDTO, Principal principal) {
		Reservation reservation = new Reservation();

		// 다른 필드 값 설정
		reservationService.saveReservation(reservation);
		// DTO에서 받은 예약 정보를 Entity에 설정

		reservation.setUser_id(getUserId(principal.getName()));
		reservation.setClass_id(reservationDTO.getClass_id());
		reservation.setReservation_price(reservationDTO.getReservation_price());
		reservation.setReservation_regnum(reservationDTO.getReservation_regnum());
		reservation.setReservation_date(reservationDTO.getReservation_date());
		reservation.setReservation_people(reservationDTO.getReservation_people());
		reservation.setReservation_start(reservationDTO.getReservation_start());
		// 필요한 경우 더 많은 필드 설정

		return reservation;
	}

	private int getUserId(String userEmail) {
		return heartService.selectUserId(userEmail);
	}

	@PostMapping("/regCheck")
	public ModelAndView getRegCheck2(Principal principal, ReservationDTO reservationDTO, HttpServletRequest request, RedirectAttributes redirectAttributes) {
	    ModelAndView mav = new ModelAndView();

	    if (principal == null) {
	        redirectAttributes.addFlashAttribute("message", "로그인 해주세요.");
	        mav.setViewName("redirect:/loginMain.do");
	        return mav;
	    }

	    String userEmail = principal.getName();
	    int userId = reservationService.selectUserId(userEmail);
	    int userType = reservationService.selectUsertype(userId);
	    int class_id = reservationDTO.getClass_id();
	    String img_link = reservationService.selectImgname(class_id);
	    reservationDTO.setImg_link(img_link);
	    System.out.println(userType);
	    if (userType == 1) {
	        redirectAttributes.addFlashAttribute("message", "사업자는 예약할수없습니다");
	        SecurityContextHolder.clearContext();
	        mav.setViewName("redirect:/classPage/" + reservationDTO.getClass_id());
	        
	        return mav;
	    }

	    if (reservationService.isClassAlreadyReserved(userId, reservationDTO.getClass_id(), reservationDTO.getReservation_date())) {
	        redirectAttributes.addFlashAttribute("message", "이미 예약된 클래스입니다.");
	        mav.setViewName("redirect:/classPage/" + reservationDTO.getClass_id());
	        SecurityContextHolder.clearContext();
	        return mav;
	    }

	    MemberDTO memberDTO = memberService.selectUserEmail(userEmail);
	    mav.addObject("memberDTO", memberDTO);
	    mav.addObject("reservationDTO", reservationDTO);
	   // 적절한 뷰 이름으로 변경

	    return mav;
	}



	@PostMapping("/regFin")
	public ModelAndView regFin(Principal principal, ReservationDTO reservationDTO) {
		// reservationDTO 객체 출력
		System.out.println("ReservationDTO: " + reservationDTO);

		// 현재 인증된 사용자의 이메일을 가져옵니다.
		String userEmail = principal.getName();

		// 이메일을 사용하여 사용자 ID를 가져옵니다.
		int userId = reservationService.selectUserId22(userEmail);

		// reservationDTO에 사용자 ID를 설정합니다.
		reservationDTO.setUser_id(userId);

		// 사용자 ID가 설정된 reservationDTO를 저장합니다.
		reservationService.saveReservation(reservationDTO);

		// 예약 날짜 업데이트 추가
		reservationService.updateReservationDates();

		// class_id 값을 가져옵니다.
		int class_id = reservationDTO.getClass_id();
		System.out.println("Class ID: " + class_id);

		// 이미지 링크를 가져옵니다.
		String img_link = reservationService.selectImgname(class_id);
		reservationDTO.setImg_link(img_link);

		// 새로운 예약 날짜를 가져옵니다.
		LocalDate new_reservation_date = reservationService.selectNewReservationDates(class_id, userId);
		reservationDTO.setNew_reservation_date(new_reservation_date);
		System.out.println("reservation: " + reservationDTO);
		// "regFin" 뷰를 위한 ModelAndView 객체를 생성합니다.
		ModelAndView mav = new ModelAndView("regFin");
		mav.addObject("reservationDTO", reservationDTO);
		return mav;
	}

	@GetMapping("/classEdit")
	public ModelAndView editClass(Principal principal) {
		ModelAndView mav = new ModelAndView("classEdit");
		String user_email = principal.getName();
		ClassDTO classInfo = classService.selectClassEdit(user_email);
		
		
		mav.addObject("classInfo", classInfo);
		mav.addObject("imgList", classInfo.getImgList());
		mav.addObject("timestamp", System.currentTimeMillis());
		return mav;
	}

	@PostMapping("/classInfoEdit")
	public RedirectView updateClass(@RequestParam("mainImage") MultipartFile mainImage,
	                                @RequestParam("subImages") List<MultipartFile> subImages, ClassDTO classDTO, ImgDTO imgDTO, 
	                                RedirectAttributes redirectAttributes) {
	    try {
	        classService.updateClassInfo(classDTO, imgDTO, mainImage, subImages);
	        SecurityContextHolder.clearContext();
	        
	        redirectAttributes.addAttribute("timestamp", System.currentTimeMillis());
	        redirectAttributes.addFlashAttribute("message", "수정이 완료되었습니다.");
	        
	        return new RedirectView("/classPage/" + classDTO.getClass_id());
	    } catch (Exception e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("errorMessage", "클래스 수정 중 오류가 발생했습니다.");
	        return new RedirectView("/classEdit");
	    }
	}



	// 회원가입 페이지 1 보여주기
	@GetMapping("/joinUser1")
	public ModelAndView showJoinUser1() {
		ModelAndView mav = new ModelAndView("joinUser1");
		return mav;
	}

	// 이메일 중복 확인
	@PostMapping("/checkDuplicateEmail")
	@ResponseBody
	public Map<String, Boolean> checkDuplicateEmail(@RequestBody Map<String, String> requestData) {
		String userEmail = requestData.get("userEmail");

		if (!EmailValidator.validate(userEmail)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "올바르지 않은 이메일 형식입니다.");
		}

		Map<String, Boolean> response = new HashMap<>();
		response.put("duplicate", userService.isUserIdExists(userEmail));
		return response;
	}

	// 회원가입 페이지 1 처리 후 user_id 반환
	@PostMapping("/joinUser1")
	@ResponseBody
	public Map<String, Object> joinUser1(@RequestBody MemberDTO loginForm) {
		int nextNo = userService.findMaxUserId() + 1;
		System.out.println("[joinUser1] user_id: " + nextNo);
		System.out.println(loginForm);

		loginForm.setUser_id(nextNo);
		userService.insertUserInfo(loginForm);

		Map<String, Object> response = new HashMap<>();
		response.put("user_id", nextNo);
		return response;
	}

	// 관심분야 선택 페이지 보여주기
	@GetMapping("/joinUser2")
	public ModelAndView joinUser2(@RequestParam("user_id") Integer userId) {
		ModelAndView mav = new ModelAndView("joinUser2");
		if (userId != null && userId > 0) {
			System.out.println("[joinUser2] user_id: " + userId);
			mav.addObject("user_id", userId);
		} else {
			System.out.println("[joinUser2] 유효하지 않은 user_id");
		}
		return mav;
	}

	// 관심분야 정보 저장
	@PostMapping("/joinUser2")
	public ModelAndView insertJoinUser2(@RequestBody MemberDTO dto) {
		MemberDTO loginForm = new MemberDTO();
		loginForm.setUser_id(dto.getUser_id());
		loginForm.setInterest_type1(dto.getInterest_type1());
		loginForm.setInterest_type2(dto.getInterest_type2());
		loginForm.setInterest_type3(dto.getInterest_type3());

		userService.insertInterestInfo(loginForm);

		return new ModelAndView("joinFin");
	}

	@PostMapping("/cancelJoin")
	public ResponseEntity<?> cancelJoin(@RequestParam("user_id") Integer user_id) {
		try {
			userService.deleteUserInfo(user_id);
			// ResponseEntity에 Map을 사용하여 JSON 형식으로 응답
			Map<String, String> response = new HashMap<>();
			response.put("message", "회원가입 취소가 성공적으로 처리되었습니다.");
			return ResponseEntity.ok().body(response);
		} catch (DataAccessException e) {
			// 에러 발생 시에도 JSON 형식으로 에러 메시지 반환
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", "회원 정보 삭제 중 오류가 발생했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

//			회원가입(사업자 정보입력)
	@GetMapping("/joinBis1")
	public ModelAndView showJoinBis1() {

		ModelAndView mav = new ModelAndView("joinBis1");

		return mav;
	}

	@PostMapping("/joinBis1")
	@ResponseBody
	public Map<String, Object> joinBis1(@RequestBody MemberDTO loginForm) {
		int nextNo = userService.findMaxUserId() + 1;
		System.out.println("[joinUser1] user_id: " + nextNo);

		loginForm.setUser_id(nextNo);
		userService.insertBisInfo(loginForm);

		Map<String, Object> response = new HashMap<>();
		response.put("user_id", nextNo);
		return response;
	}

	@GetMapping("/joinBis2")
	public ModelAndView joinBis2(@RequestParam("user_id") Integer userId) {
		ModelAndView mav = new ModelAndView("joinBis2");
		if (userId != null && userId > 0) {
			System.out.println("[joinBis2] user_id: " + userId);
			mav.addObject("user_id", userId);
		} else {
			System.out.println("[joinBis2] 유효하지 않은 user_id");
		}
		return mav;
	}

	@PostMapping("/joinBis2")
	public ModelAndView registClass(@RequestParam("mainImage") MultipartFile mainImage,
			@RequestParam("subImages") List<MultipartFile> subImages, ClassDTO classDTO, ImgDTO imgDTO) {
		ModelAndView mav = new ModelAndView();
		try {
			// 첫 번째로 nextNo를 조회하여 class_id 값을 설정
			int nextNo = classService.findMaxClassId() + 1;
			classDTO.setClass_id(nextNo); // classDTO에 새로운 class_id 설정
			imgDTO.setClass_id(nextNo); // imgDTO에도 동일한 class_id 설정

			// 클래스 정보 추가
			classService.insertClass(classDTO);

			// 메인 이미지 처리
			if (mainImage == null || mainImage.isEmpty()) {
				imgDTO.setImg_name("main_img.png");
				imgDTO.setImg_path(DEFAULT_IMAGE_DIR);
				imgDTO.setImg_type(0); // 메인 이미지 유형
				classService.insertImg(imgDTO);
			} else {
				storeImage(mainImage, true, classDTO, imgDTO);
			}

			// 서브 이미지 처리
			for (int i = 0; i < subImages.size(); i++) {
				MultipartFile subImage = subImages.get(i);
				if (subImage == null || subImage.isEmpty()) {
					imgDTO.setImg_name("smallBlack.png");
					imgDTO.setImg_path(DEFAULT_IMAGE_DIR);
					imgDTO.setImg_type(i + 1); // 서브 이미지 유형
					classService.insertImg(imgDTO);
				} else {
					storeImage(subImage, false, classDTO, imgDTO, i + 1);
				}
			}

			mav.setViewName("redirect:/joinFin");
		} catch (Exception e) {
			mav.addObject("errorMessage", "클래스 등록 중 오류가 발생했습니다.");
			mav.setViewName("joinBis2");
			e.printStackTrace();
		}
		return mav;
	}

	private void storeImage(MultipartFile file, boolean isMain, ClassDTO classDTO, ImgDTO imgDTO) throws IOException {
		storeImage(file, isMain, classDTO, imgDTO, isMain ? 0 : 1);
	}

	private void storeImage(MultipartFile file, boolean isMain, ClassDTO classDTO, ImgDTO imgDTO, int imgType)
			throws IOException {
		String prefix = isMain ? "main_" : "sub_";
		String fileName = prefix + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		file.transferTo(new File(REPOSITORY_DIR + fileName));

		imgDTO.setImg_name(fileName);
		imgDTO.setImg_path(REPOSITORY_DIR);
		imgDTO.setImg_type(imgType); // 이미지 타입 설정
		classService.insertImg(imgDTO);
	}

	// 좋아요 상태 확인 메서드
	@GetMapping("/classPage/getLikes")
	@ResponseBody
	public Map<Integer, Boolean> getLikes(Principal principal) {
		Map<Integer, Boolean> likesMap = new HashMap<>();
		if (principal == null) {
			return likesMap;
		}

		String userEmail = principal.getName();
		int userId = heartService.selectUserId(userEmail);

		List<HeartDTO> heartList = heartService.getUserHearts(userId);

		for (HeartDTO heart : heartList) {
			likesMap.put(heart.getClass_id(), true);
		}

		return likesMap;
	}

	@GetMapping("/classPage/updateLike")
	@ResponseBody
	public Map<String, Object> updateLike(@RequestParam("classId") Integer classId, Principal principal) {
		Map<String, Object> result = new HashMap<>();
		if (principal == null) {
			result.put("isLoggedIn", false);
			return result;
		}

		String userEmail = principal.getName();
		int userId = heartService.selectUserId(userEmail);

		HeartDTO heartDTO = new HeartDTO();
		heartDTO.setUser_id(userId);
		heartDTO.setClass_id(classId);

		boolean isLiked = heartService.isLiked(heartDTO);

		if (!isLiked) {
			classService.updateClassHeart(classId);
			heartService.insertHeart(heartDTO);
			heartService.updateHeart(userId);
			result.put("isLiked", true);
		} else {
			classService.deleteClassHeart(classId);
			heartService.deleteHeart(heartDTO);
			result.put("isLiked", false);
		}

		result.put("isLoggedIn", true);
		return result;
	}

	@GetMapping("/download/{img_id}")
	public ResponseEntity<Resource> downloadFile(@PathVariable("img_id") int img_id) {
	    System.out.println(img_id);
	    try {
	        ImgDTO dto = service.getFilename(img_id);
	        Path filePath = Paths.get(REPOSITORY_DIR).resolve(dto.getImg_name()).normalize();
	        System.out.println(filePath);
	        Resource resource = new UrlResource(filePath.toUri());
	        System.out.println(resource);
	        if (resource.exists()) {
	            return ResponseEntity.ok()
	                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dto.getImg_name() + "\"")
	                    .body(resource);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity.notFound().build();
	    }
	}
}
