package util;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import dao.DaoPessoa;
import enums.SexoEnum;
import model.Endereco;
import model.Pessoa;

@WebListener
public class AppInitializer implements ServletContextListener {

	private DaoPessoa daoPessoa = new DaoPessoa();

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {

		InicializeData();

	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		JPAUtil.closeEntityManagerFactory();
	}

	private void InicializeData() {
		Pessoa pessoa = new Pessoa(null, "Jose", 12, SexoEnum.MASCULINO);
		List<Pessoa> pessoas = daoPessoa.pesquisarNome(pessoa.getNome());
		if (pessoas.isEmpty()) {
			Endereco endereco1 = new Endereco();
			endereco1.setCep("70000000");
			endereco1.setCidade("Brasilia");
			endereco1.setEstado("DF");
			endereco1.setLogradouro("Quadra 3");
			endereco1.setNumero(98);
			endereco1.setPessoa(pessoa);

			Endereco endereco2 = new Endereco();
			endereco2.setCep("71000000");
			endereco2.setCidade("São Paulo");
			endereco2.setEstado("SP");
			endereco2.setLogradouro("Avenida Paulista");
			endereco2.setNumero(45);
			endereco2.setPessoa(pessoa);

			pessoa.setEnderecos(List.of(endereco1, endereco2));

			try {
				daoPessoa.salvar(pessoa);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		pessoa = new Pessoa(null, "Ana", 25, SexoEnum.FEMININO);
		pessoas = daoPessoa.pesquisarNome(pessoa.getNome());
		if (pessoas.isEmpty()) {
			Endereco endereco1 = new Endereco();
			endereco1.setCep("72000000");
			endereco1.setCidade("Rio Janeiro");
			endereco1.setEstado("RJ");
			endereco1.setLogradouro("Ipanema");
			endereco1.setNumero(45);
			endereco1.setPessoa(pessoa);

			Endereco endereco2 = new Endereco();
			endereco2.setCep("78000000");
			endereco2.setCidade("Cuiaba");
			endereco2.setEstado("MT");
			endereco2.setLogradouro("Avenida Cuiaba");
			endereco2.setNumero(77);
			endereco2.setPessoa(pessoa);

			pessoa.setEnderecos(List.of(endereco1, endereco2));

			try {
				daoPessoa.salvar(pessoa);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
