package bean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dao.DaoEndereco;
import dao.DaoPessoa;
import enums.SexoEnum;
import model.Endereco;
import model.Pessoa;

public class EnderecoBeanTest {

	@Mock
	private DaoPessoa daoPessoa;

	@Mock
	private DaoEndereco daoEndereco;

	@Mock
	private FacesContext facesContext;

	@Mock
	private ExternalContext externalContext;

	@InjectMocks
	private EnderecoBean enderecoBean;

	private Endereco endereco;

	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		// MockitoAnnotations.initMocks(this);
		facesContext = ContextMocker.mockFacesContext();

		when(facesContext.getExternalContext()).thenReturn(externalContext);
	}

	@Test
	public void testSalvarNovoSuccess() throws Exception {
		Long pessoaId = 1L;
		endereco = new Endereco(null, "DF", "Brasilia", "Asa Sul", "SQS 115", 54, "71000-000",
				new Pessoa(pessoaId, "João", 45, SexoEnum.MASCULINO));

		enderecoBean.setPessoaId(pessoaId);
		enderecoBean.setEndereco(endereco);

		// Configuração do mock
		when(daoEndereco.pesquisarPorPessoaId(pessoaId)).thenReturn(List.of(endereco));

		// Execução do método a ser testado
		String result = enderecoBean.salvar();

		// Verificações execução
		Mockito.verify(daoEndereco, Mockito.times(1)).salvar(endereco);
		Mockito.verify(daoEndereco, Mockito.times(1)).pesquisarPorPessoaId(endereco.getPessoa().getId());
		Mockito.verifyNoMoreInteractions(daoPessoa);

		// Verificações resultado
		Assert.assertEquals("", result); // Verifica se o resultado do método é uma string vazia
		Assert.assertEquals(new Endereco(), enderecoBean.getEndereco());
		Assert.assertEquals(1, enderecoBean.getEnderecos().size());
		Assert.assertEquals(endereco.getCep(), enderecoBean.getEnderecos().get(0).getCep());

		// Captura o argumento do método addMessage
		ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
		verify(facesContext).addMessage(any(), messageCaptor.capture());

		// Obtém a mensagem capturada
		FacesMessage capturedMessage = messageCaptor.getValue();

		// Verifica se a mensagem capturada é igual à mensagem esperada
		Assert.assertEquals(FacesMessage.SEVERITY_INFO, capturedMessage.getSeverity());
		Assert.assertEquals("Informação", capturedMessage.getSummary());
		Assert.assertEquals("Endereço salvo com sucesso!", capturedMessage.getDetail());
	}

	@Test
	public void testSalvarNovoError() throws Exception {
		Long pessoaId = 1L;
		endereco = new Endereco(null, "DF", "Brasilia", "Asa Sul", "SQS 115", 54, "71000-000",
				new Pessoa(pessoaId, "João", 45, SexoEnum.MASCULINO));

		enderecoBean.setPessoaId(pessoaId);
		enderecoBean.setEndereco(endereco);

		String msgErro = "Testando erro ao salvar endereco";

		// Configuração do mock
		when(daoEndereco.pesquisarPorPessoaId(pessoaId)).thenReturn(Collections.emptyList());
		Mockito.doThrow(new Exception(msgErro)).when(daoEndereco).salvar(endereco);

		// Execução do método a ser testado
		String result = enderecoBean.salvar();

		// Verificações execução
		Mockito.verify(daoEndereco, Mockito.times(1)).salvar(endereco);
		Mockito.verifyNoMoreInteractions(daoEndereco);

		// Verificações resultado
		Assert.assertEquals("", result); // Verifica se o resultado do método é uma string vazia
		Assert.assertEquals(endereco, enderecoBean.getEndereco());
		Assert.assertEquals(0, enderecoBean.getEnderecos().size());

		// Captura o argumento do método addMessage
		ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
		verify(facesContext).addMessage(any(), messageCaptor.capture());

		// Obtém a mensagem capturada
		FacesMessage capturedMessage = messageCaptor.getValue();

		// Verifica se a mensagem capturada é igual à mensagem esperada
		Assert.assertEquals(FacesMessage.SEVERITY_ERROR, capturedMessage.getSeverity());
		Assert.assertEquals("Erro", capturedMessage.getSummary());
		Assert.assertEquals("Erro ao salvar endereço! " + msgErro, capturedMessage.getDetail());
	}

	@Test
	public void testSalvarExistenteSuccess() throws Exception {
		Long pessoaId = 1L;
		endereco = new Endereco(1L, "DF", "Brasilia", "Asa Sul", "SQS 115", 54, "71000-000",
				new Pessoa(pessoaId, "João", 45, SexoEnum.MASCULINO));

		enderecoBean.setPessoaId(pessoaId);
		enderecoBean.setEndereco(endereco);

		// Configuração do mock
		when(daoEndereco.pesquisarPorPessoaId(pessoaId)).thenReturn(List.of(endereco));

		// Execução do método a ser testado
		String result = enderecoBean.salvar();

		// Verificações execução
		Mockito.verify(daoEndereco, Mockito.times(1)).atualizar(endereco);
		Mockito.verify(daoEndereco, Mockito.times(1)).pesquisarPorPessoaId(endereco.getPessoa().getId());
		Mockito.verifyNoMoreInteractions(daoPessoa);

		// Verificações resultado
		Assert.assertEquals("", result); // Verifica se o resultado do método é uma string vazia
		Assert.assertEquals(new Endereco(), enderecoBean.getEndereco());
		Assert.assertEquals(1, enderecoBean.getEnderecos().size());
		Assert.assertEquals(endereco.getCep(), enderecoBean.getEnderecos().get(0).getCep());

		// Captura o argumento do método addMessage
		ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
		verify(facesContext).addMessage(any(), messageCaptor.capture());

		// Obtém a mensagem capturada
		FacesMessage capturedMessage = messageCaptor.getValue();

		// Verifica se a mensagem capturada é igual à mensagem esperada
		Assert.assertEquals(FacesMessage.SEVERITY_INFO, capturedMessage.getSeverity());
		Assert.assertEquals("Informação", capturedMessage.getSummary());
		Assert.assertEquals("Endereço salvo com sucesso!", capturedMessage.getDetail());
	}

	@Test
	public void testSalvarExistenteError() throws Exception {
		Long pessoaId = 1L;
		endereco = new Endereco(1L, "DF", "Brasilia", "Asa Sul", "SQS 115", 54, "71000-000",
				new Pessoa(pessoaId, "João", 45, SexoEnum.MASCULINO));

		enderecoBean.setPessoaId(pessoaId);
		enderecoBean.setEndereco(endereco);

		String msgErro = "Testando erro ao salvar endereco";

		// Configuração do mock
		when(daoEndereco.pesquisarPorPessoaId(pessoaId)).thenReturn(Collections.emptyList());
		Mockito.doThrow(new Exception(msgErro)).when(daoEndereco).atualizar(endereco);

		// Execução do método a ser testado
		String result = enderecoBean.salvar();

		// Verificações execução
		Mockito.verify(daoEndereco, Mockito.times(1)).atualizar(endereco);
		Mockito.verifyNoMoreInteractions(daoEndereco);

		// Verificações resultado
		Assert.assertEquals("", result); // Verifica se o resultado do método é uma string vazia
		Assert.assertEquals(endereco, enderecoBean.getEndereco());
		Assert.assertEquals(0, enderecoBean.getEnderecos().size());

		// Captura o argumento do método addMessage
		ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
		verify(facesContext).addMessage(any(), messageCaptor.capture());

		// Obtém a mensagem capturada
		FacesMessage capturedMessage = messageCaptor.getValue();

		// Verifica se a mensagem capturada é igual à mensagem esperada
		Assert.assertEquals(FacesMessage.SEVERITY_ERROR, capturedMessage.getSeverity());
		Assert.assertEquals("Erro", capturedMessage.getSummary());
		Assert.assertEquals("Erro ao salvar endereço! " + msgErro, capturedMessage.getDetail());
	}

	@Test
	public void testExcluirPorIdSuccess() throws Exception {
		Long pessoaId = 1L;
		endereco = new Endereco(1L, "DF", "Brasilia", "Asa Sul", "SQS 115", 54, "71000-000",
				new Pessoa(pessoaId, "João", 45, SexoEnum.MASCULINO));

		enderecoBean.setPessoaId(pessoaId);
		enderecoBean.setEndereco(endereco);

		// Configuração do mock para buscar uma pessoa por id
		when(daoEndereco.buscarId(Endereco.class, endereco.getId())).thenReturn(endereco);
		when(daoEndereco.pesquisarPorPessoaId(pessoaId)).thenReturn(List.of(endereco));

		// Execução do método a ser testado
		enderecoBean.excluir(endereco.getId());

		// Verificações execução
		Mockito.verify(daoEndereco, Mockito.times(1)).buscarId(Endereco.class, endereco.getId());
		Mockito.verify(daoEndereco, Mockito.times(1)).excluir(endereco);
		Mockito.verify(daoEndereco, Mockito.times(1)).pesquisarPorPessoaId(endereco.getPessoa().getId());
		Mockito.verifyNoMoreInteractions(daoPessoa);

		// Verificações resultado
		// Assert.assertEquals("", result); // Verifica se o resultado do método é uma
		// string vazia
		Assert.assertEquals(new Endereco(), enderecoBean.getEndereco());
		Assert.assertEquals(1, enderecoBean.getEnderecos().size());
		Assert.assertEquals(endereco.getCep(), enderecoBean.getEnderecos().get(0).getCep());

		// Captura o argumento do método addMessage
		ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
		verify(facesContext).addMessage(any(), messageCaptor.capture());

		// Obtém a mensagem capturada
		FacesMessage capturedMessage = messageCaptor.getValue();

		// Verifica se a mensagem capturada é igual à mensagem esperada
		Assert.assertEquals(FacesMessage.SEVERITY_INFO, capturedMessage.getSeverity());
		Assert.assertEquals("Informação", capturedMessage.getSummary());
		Assert.assertEquals("Endereço excluído com sucesso!", capturedMessage.getDetail());

	}

	@Test
	public void testExcluirPorIdNotFound() throws Exception {
		endereco = new Endereco(1L, "DF", "Brasilia", "Asa Sul", "SQS 115", 54, "71000-000",
				new Pessoa(1L, "João", 45, SexoEnum.MASCULINO));

		// Configuração do mock para buscar uma pessoa por id
		when(daoEndereco.buscarId(Endereco.class, endereco.getId())).thenReturn(null);
		when(daoEndereco.pesquisarPorPessoaId(endereco.getPessoa().getId())).thenReturn(List.of(endereco));

		// Execução do método a ser testado
		enderecoBean.excluir(endereco.getId());

		// Verificações execução
		Mockito.verify(daoEndereco, Mockito.times(1)).buscarId(Endereco.class, endereco.getId());
		Mockito.verifyNoMoreInteractions(daoEndereco);

		// Verificações resultado
		// Assert.assertEquals("", result);

		// Captura o argumento do método addMessage
		ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
		verify(facesContext).addMessage(any(), messageCaptor.capture());

		// Obtém a mensagem capturada
		FacesMessage capturedMessage = messageCaptor.getValue();

		// Verifica se a mensagem capturada é igual à mensagem esperada
		Assert.assertEquals(FacesMessage.SEVERITY_ERROR, capturedMessage.getSeverity());
		Assert.assertEquals("Erro", capturedMessage.getSummary());
		Assert.assertEquals("Erro ao excluír endereço! Endereço não encontrado", capturedMessage.getDetail());

	}

	@Test
	public void testExcluirSuccess() throws Exception {
		Long pessoaId = 1L;
		endereco = new Endereco(1L, "DF", "Brasilia", "Asa Sul", "SQS 115", 54, "71000-000",
				new Pessoa(pessoaId, "João", 45, SexoEnum.MASCULINO));

		enderecoBean.setPessoaId(pessoaId);
		enderecoBean.setEndereco(endereco);

		// Configuração do mock para buscar uma pessoa por id
		when(daoEndereco.buscarId(Endereco.class, endereco.getId())).thenReturn(endereco);
		when(daoEndereco.pesquisarPorPessoaId(pessoaId)).thenReturn(List.of(endereco));

		// Execução do método a ser testado
		enderecoBean.excluir();

		// Verificações execução
		Mockito.verify(daoEndereco, Mockito.times(1)).buscarId(Endereco.class, endereco.getId());
		Mockito.verify(daoEndereco, Mockito.times(1)).excluir(endereco);
		Mockito.verify(daoEndereco, Mockito.times(1)).pesquisarPorPessoaId(pessoaId);
		Mockito.verifyNoMoreInteractions(daoPessoa);

		// Verificações resultado
		// Assert.assertEquals("", result); // Verifica se o resultado do método é uma
		// string vazia
		Assert.assertEquals(new Endereco(), enderecoBean.getEndereco());
		Assert.assertEquals(1, enderecoBean.getEnderecos().size());
		Assert.assertEquals(endereco.getCep(), enderecoBean.getEnderecos().get(0).getCep());

		// Captura o argumento do método addMessage
		ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
		verify(facesContext).addMessage(any(), messageCaptor.capture());

		// Obtém a mensagem capturada
		FacesMessage capturedMessage = messageCaptor.getValue();

		// Verifica se a mensagem capturada é igual à mensagem esperada
		Assert.assertEquals(FacesMessage.SEVERITY_INFO, capturedMessage.getSeverity());
		Assert.assertEquals("Informação", capturedMessage.getSummary());
		Assert.assertEquals("Endereço excluído com sucesso!", capturedMessage.getDetail());

	}

	@Test
	public void testExcluirNotFound() throws Exception {
		endereco = new Endereco(1L, "DF", "Brasilia", "Asa Sul", "SQS 115", 54, "71000-000",
				new Pessoa(1L, "João", 45, SexoEnum.MASCULINO));

		enderecoBean.setEndereco(endereco);

		// Configuração do mock para buscar uma pessoa por id
		when(daoEndereco.buscarId(Endereco.class, endereco.getId())).thenReturn(null);
		when(daoEndereco.pesquisarPorPessoaId(endereco.getPessoa().getId())).thenReturn(Collections.emptyList());

		// Execução do método a ser testado
		enderecoBean.excluir();

		// Verificações execução
		Mockito.verify(daoEndereco, Mockito.times(1)).buscarId(Endereco.class, endereco.getId());
		Mockito.verifyNoMoreInteractions(daoEndereco);

		// Verificações resultado
		// Assert.assertEquals("", result);

		// Captura o argumento do método addMessage
		ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
		verify(facesContext).addMessage(any(), messageCaptor.capture());

		// Obtém a mensagem capturada
		FacesMessage capturedMessage = messageCaptor.getValue();

		// Verifica se a mensagem capturada é igual à mensagem esperada
		Assert.assertEquals(FacesMessage.SEVERITY_ERROR, capturedMessage.getSeverity());
		Assert.assertEquals("Erro", capturedMessage.getSummary());
		Assert.assertEquals("Erro ao excluír endereço! Endereço não encontrado", capturedMessage.getDetail());

	}

	@Test
	public void testEditarSuccess() throws Exception {
		endereco = new Endereco(1L, "DF", "Brasilia", "Asa Sul", "SQS 115", 54, "71000-000",
				new Pessoa(1L, "João", 45, SexoEnum.MASCULINO));

		when(daoEndereco.buscarId(Endereco.class, endereco.getId())).thenReturn(endereco);

		// Execução do método a ser testado
		enderecoBean.editar(endereco.getId());

		// Verificações execução
		Mockito.verify(daoEndereco, Mockito.times(1)).buscarId(Endereco.class, endereco.getId());
		Mockito.verifyNoMoreInteractions(daoPessoa);

		// Verificações resultado
		// Assert.assertEquals("", result); // Verifica se o resultado do método é uma
		// string vazia
		Assert.assertEquals(endereco, enderecoBean.getEndereco());
	}

	@Test
	public void testEditarNotFound() throws Exception {
		endereco = new Endereco(1L, "DF", "Brasilia", "Asa Sul", "SQS 115", 54, "71000-000",
				new Pessoa(1L, "João", 45, SexoEnum.MASCULINO));

		when(daoEndereco.buscarId(Endereco.class, endereco.getId())).thenReturn(null);

		// Execução do método a ser testado
		enderecoBean.editar(endereco.getId());

		// Verificações execução
		Mockito.verify(daoEndereco, Mockito.times(1)).buscarId(Endereco.class, endereco.getId());
		Mockito.verifyNoMoreInteractions(daoPessoa);

		// Verificações resultado
		// Assert.assertEquals("", result); // Verifica se o resultado do método é uma
		// string vazia
		Assert.assertEquals(null, enderecoBean.getEndereco());

		// Captura o argumento do método addMessage
		ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
		verify(facesContext).addMessage(any(), messageCaptor.capture());

		// Obtém a mensagem capturada
		FacesMessage capturedMessage = messageCaptor.getValue();

		// Verifica se a mensagem capturada é igual à mensagem esperada
		Assert.assertEquals(FacesMessage.SEVERITY_ERROR, capturedMessage.getSeverity());
		Assert.assertEquals("Erro", capturedMessage.getSummary());
		Assert.assertEquals("Erro ao buscar endereço! Endereço não encontrado", capturedMessage.getDetail());
	}

	@Test
	public void testLimparSuccess() throws Exception {
		Long pessoaId = 1L;
		endereco = new Endereco(1L, "DF", "Brasilia", "Asa Sul", "SQS 115", 54, "71000-000",
				new Pessoa(pessoaId, "João", 45, SexoEnum.MASCULINO));

		enderecoBean.setPessoaId(pessoaId);
		enderecoBean.setEndereco(endereco);

		// Execução do método a ser testado
		String result = enderecoBean.limpar();

		// Verificações execução
		Mockito.verifyNoMoreInteractions(daoPessoa);

		// Verificações resultado
		Assert.assertEquals("endereco.jsf?pessoaId=" + pessoaId + "&faces-redirect=true", result);
		Assert.assertEquals(new Endereco(), enderecoBean.getEndereco());
	}

	@Test
	public void testBuscaEnderecoByCep() {
		String cep = "01001-000";
		// Mock para o objeto endereco
		Endereco endereco = new Endereco();
		endereco.setCep(cep);
		enderecoBean.setEndereco(endereco);

		enderecoBean.buscaEnderecoByCep();

		// Verifica se os atributos do objeto endereco foram atualizados corretamente
		Assert.assertEquals("01001-000", enderecoBean.getEndereco().getCep());
		Assert.assertEquals("Praça da Sé", enderecoBean.getEndereco().getLogradouro());
		Assert.assertEquals("Sé", enderecoBean.getEndereco().getBairro());
		Assert.assertEquals("São Paulo", enderecoBean.getEndereco().getCidade());
		Assert.assertEquals("SP", enderecoBean.getEndereco().getEstado());
	}
}
