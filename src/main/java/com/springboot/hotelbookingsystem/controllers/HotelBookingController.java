package com.example.hotelbookingsystem.controller;



import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.hotelbookingsystem.entity.Room;
import com.example.hotelbookingsystem.model.OrderModel;
import com.example.hotelbookingsystem.model.RegisterModel;
import com.example.hotelbookingsystem.model.SearchRoomModel;
import com.example.hotelbookingsystem.model.UserModel;
import com.example.hotelbookingsystem.security.CustomUserDetails;
import com.example.hotelbookingsystem.service.HotelBookingService;


@Controller
public class HotelBookingController {
	
    @Autowired
    private final HotelBookingService hotelBookingService;


	public HotelBookingController(HotelBookingService hotelBookingService) {
		super();
		this.hotelBookingService = hotelBookingService;
	}

	@GetMapping({"/home","/"})
    public String showDefault(Model model) {
        return "home";
    }

	@GetMapping("/reserve")
    public String showIndexPage(final HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
    	if(request.getSession().getAttribute("MYSESSION")!=null) {
    	   	SearchRoomModel searchRoom = (SearchRoomModel)request.getSession().getAttribute("MYSESSION");
    	   	request.getSession().invalidate();
    	   	return hotelBookingService.showCheckoutPage(customUserDetails, searchRoom, null,model);
    	}

		return hotelBookingService.showIndexPage(model);
    }

	@GetMapping("/register")
    public String showRegisterForm(Model model) {
		model.addAttribute("register",new RegisterModel());
        return "register";
    }
	
    @PostMapping("/register")
    public String registerUser(@Valid RegisterModel register, BindingResult result, Model model) {
        return hotelBookingService.registerUser(register);
    }

	@GetMapping("/login")
    public String showLoginForm(Model model) {
		return hotelBookingService.showLoginForm(model);
    }

    @GetMapping("/admin/user")
    public String listUsers(BindingResult result, Model model) {
    	return hotelBookingService.listUsers(result,model);
    }

	    
    @PostMapping("/login")
    public String login(@Valid UserModel user, BindingResult result, Model model) {
    	return "redirect:/index";
    }

    @PostMapping("/searchRoom")
    public String searchRoom(@Valid SearchRoomModel searchRoom, BindingResult result, Model model) {
    	Room room = hotelBookingService.searchRoom(searchRoom, result, model);
    	if(room==null) {
    		return "redirect:/reserve";
    	}
    	searchRoom.setRoomid(room.getId());
    	model.addAttribute("searchRoom",searchRoom);
    	return "reservation";
    }
    
    @PostMapping("/checkout")
    public String showCheckoutPage(final HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid SearchRoomModel searchRoom, BindingResult result, Model model) {
    	if (customUserDetails==null) {
    		request.getSession().setAttribute("MYSESSION", searchRoom);
    		return "redirect:/login";
    	}

    	return hotelBookingService.showCheckoutPage(customUserDetails, searchRoom, result,model);
    }

    @PostMapping("/placeorder")
    public String checkout(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid OrderModel order, BindingResult result, Model model) {
    	order.setUserId(customUserDetails.getUser().getId());
    	return hotelBookingService.placeorder(order, result, model);
    }
    
    /*
    @GetMapping("/index")
    public String index(@AuthenticationPrincipal CustomUserDetails customUserDetails,Model model) {
    	model.addAttribute("articles", articleRepository.findAll());
		model.addAttribute("currentuser",customUserDetails.getUser().getUsername());
        return "index";
    }

//    @GetMapping("/")
//    public String defaultRoot(Model model) {
//        return "redirect:/index";
//    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
		model.addAttribute("user",new User());
		//model.addAttribute("currentuser","");
        return "register";
    }
    @PostMapping("/register")
    public String registerUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
	  		model.addAttribute("errors",result.getFieldErrors());
            return "register";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/login";
    }

	@GetMapping("/articleadd")
    public String showAddArticleForm(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
		model.addAttribute("article",new Article());
		model.addAttribute("currentuser",customUserDetails.getUser().getUsername());
        return "articleadd";
    }
    @PostMapping("/articleadd")
    public String addArticle(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid Article article, BindingResult result, Model model) {
        if (result.hasErrors()) {
	  		model.addAttribute("errors",result.getFieldErrors());
            return "articleadd";
        }
        article.setUser(customUserDetails.getUser());
        article.setCreatedTS(new Date());
        articleRepository.save(article);
        return "redirect:/index";
    }

	@GetMapping("/articleedit/{id}")
    public String showEditArticleForm(@AuthenticationPrincipal CustomUserDetails customUserDetails,Model model, @PathVariable String id) {
		Article Article = articleRepository.findById(Long.parseLong(id))
		          .orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + id));
		model.addAttribute("article",Article);
		model.addAttribute("currentuser",customUserDetails.getUser().getUsername());
        return "articleedit";
    }
    @PostMapping("/articleedit/{id}")
    public String EditArticle(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("id") long id, @Valid Article article, BindingResult result, Model model) {
    	if (result.hasErrors()) {
	  		model.addAttribute("errors",result.getFieldErrors());
    		article.setId(id);
            return "articleedit";
        }
        article.setUser(customUserDetails.getUser());
        article.setCreatedTS(new Date());
        articleRepository.save(article);
        return "redirect:/index";
    }
	@GetMapping("/articledelete/{id}")
    public String deleteArticle(Model model, @PathVariable String id) {
		Article article = articleRepository.findById(Long.parseLong(id))
		          .orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + id));
		User user = article.getUser();
		user.getArticles().remove(article);
		userRepository.save(user);
		articleRepository.delete(article);
        return "redirect:/index";
    }

	@GetMapping("/article/{id}")
    public String showArticleForm(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model, @PathVariable String id) {
		model.addAttribute("article",new Article());
		Article article = articleRepository.findById(Long.parseLong(id))
		          .orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + id));
		model.addAttribute("article", article);
		model.addAttribute("comment", new Comment());
		model.addAttribute("currentuser",customUserDetails.getUser().getUsername());
		return "article";
    }

	@PostMapping("/commentadd")
    public String addComment(@AuthenticationPrincipal CustomUserDetails customUserDetails,@RequestParam("articleid") String articleid, @Valid Comment comment, BindingResult result, Model model) {
        if (result.hasErrors()) {
    		Article article = articleRepository.findById(Long.parseLong(articleid))
  		          .orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + articleid));
	  		model.addAttribute("article", article);
	  		model.addAttribute("errors",result.getFieldErrors());
	  		return "article";
        }
		Article article = articleRepository.findById(Long.parseLong(articleid))
		          .orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + articleid));
		comment.setArticle(article);
		comment.setUser(customUserDetails.getUser());
		comment.setCreatedTS(new Date());
		commentRepository.save(comment);
		return "redirect:/article/"+articleid;
    }
	@GetMapping("/commentdelete/{commentid}")
    public String deleteComment(Model model, @PathVariable String commentid) {
        Comment comment = commentRepository.findById(Long.parseLong(commentid))
		          .orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + commentid));
		Article article = articleRepository.findById(comment.getArticle().getId())
		          .orElseThrow(() -> new IllegalArgumentException("Invalid article Id:"));
		User user = comment.getUser();
		user.getComments().remove(comment);
		article.getComments().remove(comment);
		articleRepository.save(article);
		userRepository.save(user);
		commentRepository.delete(comment);
		return "redirect:/article/"+comment.getArticle().getId();
    }
*/
}
