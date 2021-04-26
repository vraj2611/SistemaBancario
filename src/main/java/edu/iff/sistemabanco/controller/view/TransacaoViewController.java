package edu.iff.sistemabanco.controller.view;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
		if (t instanceof Transferencia) {
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
		try {
			Transacao t = null;
			if (dto.getTipo() == TipoTransacaoEnum.DEPOSITO) {
				t = cServ.depositar(cid, dto);
			} else if (dto.getTipo() == TipoTransacaoEnum.RETIRADA) {
				t = cServ.retirar(cid, dto);
			} else if (dto.getTipo() == TipoTransacaoEnum.TRANSFERENCIA) {
				t = cServ.transferir(cid, dto);
			}

			model.addAttribute("transacao", t);
			model.addAttribute("tipos", t.getTipo());
			if (t instanceof Transferencia) {
				model.addAttribute("destinos", ((Transferencia) t).getConta_destino());
			} else {
				model.addAttribute("destinos", t.getConta());
			}
			return "formTransacao";
		} catch (Exception e) {
			model.addAttribute("msgErros", new ObjectError("Transacao", e.getMessage()));
			Transferencia t = new Transferencia();
			t.setConta(cServ.findById(cid));
			t.setStatus(StatusTransacaoEnum.PENDENTE);
			model.addAttribute("transacao", t);
			model.addAttribute("tipos", TipoTransacaoEnum.values());
			model.addAttribute("destinos", cServ.findAll());
			return "formTransacao";
		}
	}

	@GetMapping(path = "/transacao/{id}/autorizar")
	public String autorizar(@AuthenticationPrincipal User user, @PathVariable("id") Long id, Model model) {
		try {
			Operador o = oServ.findByCpf(user.getUsername());
			Transacao t = cServ.autorizar(o, id);
			model.addAttribute("transacao", t);
			model.addAttribute("tipos", t.getTipo());
			if (t instanceof Transferencia) {
				model.addAttribute("destinos", ((Transferencia) t).getConta_destino());
			} else {
				model.addAttribute("destinos", t.getConta());
			}
			return "formTransacao";
		} catch (Exception e) {
			model.addAttribute("msgErros", new ObjectError("Transacao", e.getMessage()));
			model.addAttribute("transacao", serv.findById(id));
			model.addAttribute("tipos", TipoTransacaoEnum.values());
			model.addAttribute("destinos", cServ.findAll());
			return "formTransacao";
		}
	}

	@GetMapping(path = "/transacao/{id}/bloquear")
	public String bloquear(@AuthenticationPrincipal User user, @PathVariable("id") Long id, Model model) {
		try {
			
			Operador o = oServ.findByCpf(user.getUsername());
			Transacao t = cServ.bloquear(o, id);
			model.addAttribute("transacao", t);
			model.addAttribute("tipos", t.getTipo());
			if (t instanceof Transferencia) {
				model.addAttribute("destinos", ((Transferencia) t).getConta_destino());
			} else {
				model.addAttribute("destinos", t.getConta());
			}
			return "formTransacao";
		} catch (Exception e) {
			model.addAttribute("msgErros", new ObjectError("Transacao", e.getMessage()));
			model.addAttribute("transacao", serv.findById(id));
			model.addAttribute("tipos", TipoTransacaoEnum.values());
			model.addAttribute("destinos", cServ.findAll());
			return "formTransacao";
		}
	}

}
