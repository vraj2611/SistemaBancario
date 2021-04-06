package edu.iff.sistemabanco.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.iff.sistemabanco.service.TransacaoService;

@Controller
@RequestMapping(path = "/transacoes")
public class TransacaoViewController {

	@Autowired
	TransacaoService serv;
	
	@GetMapping
	public String getAll(Model model) {
		model.addAttribute("transacoes", serv.findAll());
		return "transacoes";
	}

	
}
