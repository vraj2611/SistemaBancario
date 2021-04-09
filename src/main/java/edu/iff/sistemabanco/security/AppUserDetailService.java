package edu.iff.sistemabanco.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.iff.sistemabanco.model.Cliente;
import edu.iff.sistemabanco.model.Operador;
import edu.iff.sistemabanco.model.Permissao;
import edu.iff.sistemabanco.repository.ClienteRepository;
import edu.iff.sistemabanco.repository.OperadorRepository;

@Service
public class AppUserDetailService implements UserDetailsService {

	@Autowired
	private OperadorRepository oRepo;

	@Autowired
	private ClienteRepository cRepo;

	@Override
	public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
		List<Operador> lo = oRepo.findByCpf(cpf);
		if (!lo.isEmpty()) {
			Operador o = lo.get(0);
			return new User(o.getCpf(), o.getSenha(), getAuthorities(o.getPermissoes()));
		}

		List<Cliente> lc = cRepo.findByCpf(cpf);
		if (!lc.isEmpty()) {
			Cliente c = lc.get(0);
			return new User(c.getCpf(), c.getSenha(), getAuthorities(c.getPermissoes()));
		}
		
		throw new UsernameNotFoundException("Pessoa não encontrada com esse cpf: " + cpf);
	}

	private List<GrantedAuthority> getAuthorities(List<Permissao> lista) {
		List<GrantedAuthority> l = new ArrayList<>();
		for (Permissao p : lista) {
			l.add(new SimpleGrantedAuthority("ROLE_" + p.getNome()));
		}
		return l;
	}
}
