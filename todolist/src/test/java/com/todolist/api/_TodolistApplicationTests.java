package com.todolist.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") 
class TodolistApplicationTest {

	@Test
	void contextLoads() {
		// Este teste verifica se o contexto da aplicação Spring pode ser carregado
		// com sucesso. Se o contexto carregar, o teste passa. Se houver algum erro
		// de configuração, o teste falhará.
	}
}