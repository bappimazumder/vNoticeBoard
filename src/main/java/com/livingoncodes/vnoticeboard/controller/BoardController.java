package com.livingoncodes.vnoticeboard.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.livingoncodes.vnoticeboard.dto.RegistrationForm;
import com.livingoncodes.vnoticeboard.entity.Board;
import com.livingoncodes.vnoticeboard.repository.BoardRepository;

@Controller
public class BoardController {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BoardController.class);
	
	@Autowired
	private BoardRepository boardRepository;
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerBoard(@Valid RegistrationForm registrationForm,
            BindingResult result, WebRequest request, RedirectAttributes redirectAttributes) {
		
		System.out.println(request.getParameter("boardName"));
		System.out.println(request.getParameter("password"));
		
		if( result.hasErrors() ) {
			System.out.println(result.getAllErrors());
			redirectAttributes.addAttribute("error", "Password should be more than 3 characters and less than 100");
			return "redirect:/" + request.getParameter("boardName");
		}

		Board board = new Board();
		
		board.setName(request.getParameter("boardName"));
		board.setPassword(request.getParameter("password"));
		
		boardRepository.save(board);
		return "redirect:/" + request.getParameter("boardName");
	}
	
	
	@RequestMapping(value ="/{boardName}", method = RequestMethod.GET)
	public String showBoard(@PathVariable("boardName") String boardName, Model model) {
		LOGGER.debug("Rendering board page: " + boardName);
		
		Board board = boardRepository.findByName(boardName);
		
		if( board == null) {
			LOGGER.debug("Board not found: " + boardName);
			
			// Add the not found boardname to model so that we can use it
			// in the new form
			model.addAttribute("boardName", boardName);
			return "newboard";
		}
		
		model.addAttribute("boardName", boardName);
		model.addAttribute("boardContent", board.getContent());
		return "board";
	}


}
