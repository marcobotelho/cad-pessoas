package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.json.JSONObject;

import dao.DaoEndereco;
import dao.DaoPessoa;
import model.Endereco;
import model.Pessoa;
import util.Mensagem;
import util.ViaCep;

@ManagedBean(name = "enderecoBean")
@ViewScoped
public class EnderecoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long pessoaId = null;

	private Pessoa pessoa = new Pessoa();

	private Endereco endereco = new Endereco();

	private DaoEndereco daoEndereco = new DaoEndereco();

	private DaoPessoa daoPessoa = new DaoPessoa();

	private List<Endereco> enderecos = new ArrayList<>();

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}

	public Long getPessoaId() {
		return pessoaId;
	}

	public void setPessoaId(Long pessoaId) {
		this.pessoaId = pessoaId;
	}

	@PostConstruct
	private void init() {

		if (pessoaId == null) {
			pessoaId = Long.parseLong(
					FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pessoaId"));
		}

		pessoa = daoPessoa.buscarId(Pessoa.class, pessoaId);

		endereco.setPessoa(pessoa);

		enderecos = daoEndereco.pesquisarPorPessoaId(pessoaId);
	}

	public String salvar() {
		try {
			// endereco.setPessoa(pessoa);
			if (endereco.getId() == null) {
				daoEndereco.salvar(endereco);
			} else {
				daoEndereco.atualizar(endereco);
			}
			endereco = new Endereco();
			endereco.setPessoa(pessoa);
			this.enderecos = daoEndereco.pesquisarPorPessoaId(pessoaId);
			Mensagem.info("Endereço salvo com sucesso!");
		} catch (Exception e) {
			Mensagem.error("Erro ao salvar endereço! " + e.getMessage());
		}
		return "";
	}

	public String limpar() {
		endereco = new Endereco();
		endereco.setPessoa(pessoa);
		return "endereco.jsf?pessoaId=" + pessoaId + "&faces-redirect=true";
	}

	public void excluir() {
		try {
			if (daoEndereco.buscarId(Endereco.class, endereco.getId()) == null) {
				throw new Exception("Endereço não encontrado");
			}
			daoEndereco.excluir(endereco);
			endereco = new Endereco();
			endereco.setPessoa(pessoa);
			enderecos = daoEndereco.pesquisarPorPessoaId(pessoaId);
			Mensagem.info("Endereço excluído com sucesso!");
		} catch (Exception e) {
			Mensagem.error("Erro ao excluír endereço! " + e.getMessage());
		}
	}

	public void excluir(Long id) {
		try {
			endereco = daoEndereco.buscarId(Endereco.class, id);
			if (endereco == null) {
				throw new Exception("Endereço não encontrado");
			}
			daoEndereco.excluir(endereco);
			endereco = new Endereco();
			endereco.setPessoa(pessoa);
			enderecos = daoEndereco.pesquisarPorPessoaId(pessoaId);
			Mensagem.info("Endereço excluído com sucesso!");
		} catch (Exception e) {
			Mensagem.error("Erro ao excluír endereço! " + e.getMessage());
		}
	}

	public void editar(Long id) {
		try {
			endereco = daoEndereco.buscarId(Endereco.class, id);
			if (endereco == null) {
				throw new Exception("Endereço não encontrado");
			}
		} catch (Exception e) {
			Mensagem.error("Erro ao buscar endereço! " + e.getMessage());
		}
	}

	public void buscaEnderecoByCep() {
//		{
//			  "cep": "01001-000",
//			  "logradouro": "Praça da Sé",
//			  "complemento": "lado ímpar",
//			  "bairro": "Sé",
//			  "localidade": "São Paulo",
//			  "uf": "SP",
//			  "ibge": "3550308",
//			  "gia": "1004",
//			  "ddd": "11",
//			  "siafi": "7107"
//			}		
		JSONObject retorno = ViaCep.buscaCep(endereco.getCep());
		if (retorno != null) {
			endereco.setCep(retorno.getString("cep"));
			endereco.setLogradouro(retorno.getString("logradouro"));
			endereco.setEstado(retorno.getString("uf"));
			endereco.setCidade(retorno.getString("localidade"));
			endereco.setBairro(retorno.getString("bairro"));
//		ufUsuarioId = daoUf.buscaUfSigla(retorno.getString("uf")).getId();
//		municipios = daoMunicipio.buscaMunicipiosPorUfSigla(retorno.getString("uf"));
//		municipioUsuarioId = daoMunicipio.buscaMunicipioNome(retorno.getString("uf"), retorno.getString("localidade"))
//				.getId();
		}
	}

}
