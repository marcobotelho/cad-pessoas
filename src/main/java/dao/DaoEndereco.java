package dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import model.Endereco;
import util.JPAUtil;

public class DaoEndereco extends DaoGenerico<Endereco> implements Serializable {

	private static final long serialVersionUID = 1L;

	private EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

	public List<Endereco> pesquisarPorPessoaId(Long pessoaId) {

		entityManager.clear();

		String sql = "from Endereco e where e.pessoa.id = " + pessoaId;
		List<Endereco> enderecos = new ArrayList<Endereco>();
		try {
			enderecos = entityManager.createQuery(sql, Endereco.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return enderecos;
	}

}
