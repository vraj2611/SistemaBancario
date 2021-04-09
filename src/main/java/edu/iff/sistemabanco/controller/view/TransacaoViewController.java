package edu.iff.sistemabanco.controller.view;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.iff.sistemabanco.model.Conta;
import edu.iff.sistemabanco.model.Deposito;
import edu.iff.sistemabanco.model.Operador;
import edu.iff.sistemabanco.model.StatusTransacaoEnum;
import edu.iff.sistemabanco.model.TipoTransacaoEnum;
import edu.iff.sistemabanco.model.Transacao;
import edu.iff.sistemabanco.model.TransacaoDto;
import edu.iff.sistemabanco.model.Transferencia;
import edu.iff.sistemabanco.service.ContaService;
import edu.iff.sistemabanco.service.OperadorService;
import edu.iff.sistemabanco.service.TransacaoService;

@Controller
@RequestMapping(path = "/transacoes")
public class TransacaoViewController {

	@Autowired
	TransacaoService serv;
	
	@Autowired
	ContaService cServ;
	
	@Autowired
	OperadorService oServ;
	
	@GetMapping
	public String getAll(Model model) {
		model.addAttribute("transacoes", serv.findAll());
		return "transacoes";
	}

	@GetMapping(path = "/pendentes")
	public String getPendentes(Model model) {
		model.addAttribute("transacoes", serv.findPendentes());
		return "transacoes";
	}

	@GetMapping(path = "/transacao/{id}")
	public String alterar(@PathVariable("id") Long id, Model model) {
		Transacao t = serv.findById(id);
		model.addAttribute("transacao", t);
		model.addAttribute("tipos", t.getTipo());
		if(t instanceof Transferencia) {
			model.addAttribute("destinos", ((Transferencia) t).getConta_destino());	
		} else {
			model.addAttribute("destinos", t.getConta());
		}			
		return "formTransacao";
	}

	@GetMapping(path = "/conta/{conta_id}/transacao")
	public String cadastro(@PathVariable("conta_id") Long cid, Model model) {
		Transferencia t = new Transferencia();
		t.setConta(cServ.findById(cid));
		t.setStatus(StatusTransacaoEnum.PENDENTE);
		model.addAttribute("transacao", t);
		model.addAttribute("tipos", TipoTransacaoEnum.values());
		model.addAttribute("destinos", cServ.findAll());
		return "formTransacao";
	}
	
	@PostMapping(path = "/conta/{conta_id}/transacao")
	public String salvar(@PathVariable("conta_id") Long cid, @ModelAttribute TransacaoDto dto, Model model) {
		Transacao t = null;
		if(dto.getTipo() == TipoTransacaoEnum.DEPOSITO) {
			t = cServ.depositar(cid, dto);
		} else if(dto.getTipo() == TipoTransacaoEnum.RETIRADA){
			t = cServ.retirar(cid, dto);
		}
	
		model.addAttribute("transacao", t);
		model.addAttribute("tipos", t.getTipo());
		if(t instanceof Transferencia) {
			model.addAttribute("destinos", ((Transferencia) t).getConta_destino());	
		} else {
			model.addAttribute("destinos", t.getConta());
		}			
		return "formTransacao";
	}
	
	@GetMapping(path = "/transacao/{id}/autorizar")
	public String autorizar(@PathVariable("id") Long id, Model model) {
		List<Operador> lo = oServ.findAll(); 
		Transacao t = cServ.autorizar(lo.get(0), id);
		model.addAttribute("transacao", t);
		model.addAttribute("tipos", t.getTipo());		
		if(t instanceof Transferencia) {
			model.addAttribute("destinos", ((Transferencia) t).getConta_destino());	
		} else {
			model.addAttribute("destinos", t.getConta());
		}			
		return "formTransacao";
	}
	
	@GetMapping(path = "/transacao/{id}/bloquear")
	public String bloquear(@PathVariable("id") Long id, Model model) {
		List<Operador> lo = oServ.findAll(); 
		Transacao t = cServ.bloquear(lo.get(0), id);		
		model.addAttribute("transacao", t);
		model.addAttribute("tipos", t.getTipo());	
		if(t instanceof Transferencia) {
			model.addAttribute("destinos", ((Transferencia) t).getConta_destino());	
		} else {
			model.addAttribute("destinos", t.getConta());
		}			
		return "formTransacao";
	}
	
}
