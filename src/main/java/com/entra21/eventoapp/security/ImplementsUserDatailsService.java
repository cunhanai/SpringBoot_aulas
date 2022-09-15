package com.entra21.eventoapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import com.entra21.eventoapp.models.Usuario;
import com.entra21.eventoapp.repository.UsuarioRepository;

@Repository
public class ImplementsUserDatailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository ur;

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		Usuario usuario = ur.findByLogin(login);

		if (usuario == null) {
			throw new UsernameNotFoundException("Usuario nao encontrado");
		}

		return usuario;
	}

}
