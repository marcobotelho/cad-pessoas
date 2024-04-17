package bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import dao.DaoPessoa;
import model.Pessoa;
import util.Mensagem;

@ManagedBean(name = "pessoaBean")
@ViewScoped
public class PessoaBean {

	private DaoPessoa daoPessoa = new DaoPessoa();

	private List<Pessoa> pessoas = new ArrayList<>();

	private Pessoa pessoa = new Pessoa();

	@PostConstruct
	public void init() {
		pessoas = daoPessoa.listarTodos(Pessoa.class);
	}

	public List<Pessoa> getPessoas() {
		return pessoas;
	}

	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public DaoPessoa getDaoPessoa() {
		return daoPessoa;
	}

	public void setDaoPessoa(DaoPessoa daoPessoa) {
		this.daoPessoa = daoPessoa;
	}

	public String excluirPorId(Long id) {
		try {
			Pessoa pessoaTemp = daoPessoa.buscarId(Pessoa.class, id);
			if (pessoaTemp == null) {
				throw new Exception("Pessoa não encontrada");
			}
			daoPessoa.excluir(pessoaTemp);
			this.pessoa = new Pessoa();
			pessoas = daoPessoa.listarTodos(Pessoa.class);
			Mensagem.info("Pessoa excluída com sucesso!");
		} catch (Exception e) {
			Mensagem.error("Erro ao excluír pessoa! Motivo: " + e.getMessage());
		}
		return "";
	}

	public String excluir() {
		return excluirPorId(this.pessoa.getId());
	}

	public String salvar() {
		try {
			if (pessoa.getId() == null) {
				daoPessoa.salvar(pessoa);
			} else {
				daoPessoa.atualizar(pessoa);
			}
			pessoa = new Pessoa();
			pessoas = daoPessoa.listarTodos(Pessoa.class);
			Mensagem.info("Pessoa salvo com sucesso!");
		} catch (Exception e) {
			Mensagem.error("Erro ao salvar pessoa! Motivo: " + e.getMessage());
		}
		return "";
	}

	public String limpar() {
		pessoa = new Pessoa();
		return "";
	}

	public String editar(Pessoa pessoa) {
		this.pessoa = pessoa;
		return "";
	}

	public void goToEnderecos(Long pessoaId) throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("endereco.xhtml?pessoaId=" + pessoaId);
	}

}
