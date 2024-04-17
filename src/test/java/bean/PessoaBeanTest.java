package bean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
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

import dao.DaoPessoa;
import enums.SexoEnum;
import model.Pessoa;

public class PessoaBeanTest {

	@Mock
	private DaoPessoa daoPessoa;

	@Mock
	private FacesContext facesContext;

	@Mock
	private ExternalContext externalContext;

	@InjectMocks
	private PessoaBean pessoaBean;

	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		// MockitoAnnotations.initMocks(this);
		facesContext = ContextMocker.mockFacesContext();

		when(facesContext.getExternalContext()).thenReturn(externalContext);
	}

	@Test
	public void testExcluirPorIdSuccess() throws Exception {
		Pessoa pessoa1 = new Pessoa(1L, "João", 45, SexoEnum.MASCULINO);
		Pessoa pessoa2 = new Pessoa(2L, "Maria", 23, SexoEnum.FEMININO);

		// Configuração do mock para buscar uma pessoa por id
		when(daoPessoa.buscarId(Pessoa.class, pessoa1.getId())).thenReturn(pessoa1);
		when(daoPessoa.listarTodos(Pessoa.class)).thenReturn(List.of(pessoa2));

		// Execução do método a ser testado
		String result = pessoaBean.excluirPorId(pessoa1.getId());

		// Verificações execução
		Mockito.verify(daoPessoa, Mockito.times(1)).buscarId(Pessoa.class, pessoa1.getId());
		Mockito.verify(daoPessoa, Mockito.times(1)).excluir(pessoa1);
		Mockito.verify(daoPessoa, Mockito.times(1)).listarTodos(Pessoa.class);
		Mockito.verifyNoMoreInteractions(daoPessoa);

		// Verificações resultado
		Assert.assertEquals("", result); // Verifica se o resultado do método é uma string vazia
		Assert.assertEquals(new Pessoa(), pessoaBean.getPessoa());
		Assert.assertEquals(1, pessoaBean.getPessoas().size());
		Assert.assertEquals(pessoa2.getNome(), pessoaBean.getPessoas().get(0).getNome());

		// Captura o argumento do método addMessage
		ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
		verify(facesContext).addMessage(any(), messageCaptor.capture());

		// Obtém a mensagem capturada
		FacesMessage capturedMessage = messageCaptor.getValue();

		// Verifica se a mensagem capturada é igual à mensagem esperada
		Assert.assertEquals(FacesMessage.SEVERITY_INFO, capturedMessage.getSeverity());
		Assert.assertEquals("Informação", capturedMessage.getSummary());
		Assert.assertEquals("Pessoa excluída com sucesso!", capturedMessage.getDetail());

	}

	@Test
	public void testExcluirPorIdNotFound() throws Exception {
		Pessoa pessoa1 = new Pessoa(1L, "João", 45, SexoEnum.MASCULINO);
		Pessoa pessoa2 = new Pessoa(2L, "Maria", 23, SexoEnum.FEMININO);

		// Configuração do mock para buscar uma pessoa por id
		when(daoPessoa.buscarId(Pessoa.class, pessoa1.getId())).thenReturn(null);
		when(daoPessoa.listarTodos(Pessoa.class)).thenReturn(List.of(pessoa1, pessoa2));

		// Execução do método a ser testado
		String result = pessoaBean.excluirPorId(pessoa1.getId());

		// Verificações execução
		Mockito.verify(daoPessoa, Mockito.times(1)).buscarId(Pessoa.class, pessoa1.getId());
		Mockito.verifyNoMoreInteractions(daoPessoa);

		// Verificações resultado
		Assert.assertEquals("", result);

		// Captura o argumento do método addMessage
		ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
		verify(facesContext).addMessage(any(), messageCaptor.capture());

		// Obtém a mensagem capturada
		FacesMessage capturedMessage = messageCaptor.getValue();

		// Verifica se a mensagem capturada é igual à mensagem esperada
		Assert.assertEquals(FacesMessage.SEVERITY_ERROR, capturedMessage.getSeverity());
		Assert.assertEquals("Erro", capturedMessage.getSummary());
		Assert.assertEquals("Erro ao excluír pessoa! Motivo: Pessoa não encontrada", capturedMessage.getDetail());

	}

	@Test
	public void testExcluirSuccess() throws Exception {
		Pessoa pessoa1 = new Pessoa(1L, "João", 45, SexoEnum.MASCULINO);
		Pessoa pessoa2 = new Pessoa(2L, "Maria", 23, SexoEnum.FEMININO);

		pessoaBean.setPessoa(pessoa1);

		// Configuração do mock para buscar uma pessoa por id
		when(daoPessoa.buscarId(Pessoa.class, pessoa1.getId())).thenReturn(pessoa1);
		when(daoPessoa.listarTodos(Pessoa.class)).thenReturn(List.of(pessoa2));

		// Execução do método a ser testado
		String result = pessoaBean.excluir();

		// Verificações execução
		Mockito.verify(daoPessoa, Mockito.times(1)).buscarId(Pessoa.class, pessoa1.getId());
		Mockito.verify(daoPessoa, Mockito.times(1)).excluir(pessoa1);
		Mockito.verify(daoPessoa, Mockito.times(1)).listarTodos(Pessoa.class);
		Mockito.verifyNoMoreInteractions(daoPessoa);

		// Verificações resultado
		Assert.assertEquals("", result); // Verifica se o resultado do método é uma string vazia
		Assert.assertEquals(new Pessoa(), pessoaBean.getPessoa());
		Assert.assertEquals(1, pessoaBean.getPessoas().size());
		Assert.assertEquals(pessoa2.getNome(), pessoaBean.getPessoas().get(0).getNome());

		// Captura o argumento do método addMessage
		ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
		verify(facesContext).addMessage(any(), messageCaptor.capture());

		// Obtém a mensagem capturada
		FacesMessage capturedMessage = messageCaptor.getValue();

		// Verifica se a mensagem capturada é igual à mensagem esperada
		Assert.assertEquals(FacesMessage.SEVERITY_INFO, capturedMessage.getSeverity());
		Assert.assertEquals("Informação", capturedMessage.getSummary());
		Assert.assertEquals("Pessoa excluída com sucesso!", capturedMessage.getDetail());

	}

	@Test
	public void testExcluirNotFound() throws Exception {
		Pessoa pessoa1 = new Pessoa(1L, "João", 45, SexoEnum.MASCULINO);
		Pessoa pessoa2 = new Pessoa(2L, "Maria", 23, SexoEnum.FEMININO);

		pessoaBean.setPessoa(pessoa1);

		// Configuração do mock para buscar uma pessoa por id
		when(daoPessoa.buscarId(Pessoa.class, pessoa1.getId())).thenReturn(null);
		when(daoPessoa.listarTodos(Pessoa.class)).thenReturn(List.of(pessoa1, pessoa2));

		// Execução do método a ser testado
		String result = pessoaBean.excluir();

		// Verificações execução
		Mockito.verify(daoPessoa, Mockito.times(1)).buscarId(Pessoa.class, pessoa1.getId());
		Mockito.verifyNoMoreInteractions(daoPessoa);

		// Verificações resultado
		Assert.assertEquals("", result);

		// Captura o argumento do método addMessage
		ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
		verify(facesContext).addMessage(any(), messageCaptor.capture());

		// Obtém a mensagem capturada
		FacesMessage capturedMessage = messageCaptor.getValue();

		// Verifica se a mensagem capturada é igual à mensagem esperada
		Assert.assertEquals(FacesMessage.SEVERITY_ERROR, capturedMessage.getSeverity());
		Assert.assertEquals("Erro", capturedMessage.getSummary());
		Assert.assertEquals("Erro ao excluír pessoa! Motivo: Pessoa não encontrada", capturedMessage.getDetail());

	}

	@Test
	public void testSalvarNovoSuccess() throws Exception {
		Pessoa pessoa1 = new Pessoa(null, "João", 45, SexoEnum.MASCULINO);
		pessoaBean.setPessoa(pessoa1);

		// Configuração do mock
		when(daoPessoa.listarTodos(Pessoa.class)).thenReturn(List.of(pessoa1));

		// Execução do método a ser testado
		String result = pessoaBean.salvar();

		// Verificações execução
		Mockito.verify(daoPessoa, Mockito.times(1)).salvar(pessoa1);
		Mockito.verify(daoPessoa, Mockito.times(1)).listarTodos(Pessoa.class);
		Mockito.verifyNoMoreInteractions(daoPessoa);

		// Verificações resultado
		Assert.assertEquals("", result); // Verifica se o resultado do método é uma string vazia
		Assert.assertEquals(new Pessoa(), pessoaBean.getPessoa());
		Assert.assertEquals(1, pessoaBean.getPessoas().size());
		Assert.assertEquals(pessoa1.getNome(), pessoaBean.getPessoas().get(0).getNome());

		// Captura o argumento do método addMessage
		ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
		verify(facesContext).addMessage(any(), messageCaptor.capture());

		// Obtém a mensagem capturada
		FacesMessage capturedMessage = messageCaptor.getValue();

		// Verifica se a mensagem capturada é igual à mensagem esperada
		Assert.assertEquals(FacesMessage.SEVERITY_INFO, capturedMessage.getSeverity());
		Assert.assertEquals("Informação", capturedMessage.getSummary());
		Assert.assertEquals("Pessoa salvo com sucesso!", capturedMessage.getDetail());
	}

	@Test
	public void testSalvarNovoError() throws Exception {
		Pessoa pessoa1 = new Pessoa(null, "João", 45, SexoEnum.MASCULINO);
		pessoaBean.setPessoa(pessoa1);
		String msgErro = "Testando erro ao salvar pessoa";

		// Configuração do mock
		when(daoPessoa.listarTodos(Pessoa.class)).thenReturn(Collections.emptyList());
		Mockito.doThrow(new Exception(msgErro)).when(daoPessoa).salvar(pessoa1);

		// Execução do método a ser testado
		String result = pessoaBean.salvar();

		// Verificações execução
		Mockito.verify(daoPessoa, Mockito.times(1)).salvar(pessoa1);
		Mockito.verifyNoMoreInteractions(daoPessoa);

		// Verificações resultado
		Assert.assertEquals("", result); // Verifica se o resultado do método é uma string vazia
		Assert.assertEquals(pessoa1, pessoaBean.getPessoa());
		Assert.assertEquals(0, pessoaBean.getPessoas().size());

		// Captura o argumento do método addMessage
		ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
		verify(facesContext).addMessage(any(), messageCaptor.capture());

		// Obtém a mensagem capturada
		FacesMessage capturedMessage = messageCaptor.getValue();

		// Verifica se a mensagem capturada é igual à mensagem esperada
		Assert.assertEquals(FacesMessage.SEVERITY_ERROR, capturedMessage.getSeverity());
		Assert.assertEquals("Erro", capturedMessage.getSummary());
		Assert.assertEquals("Erro ao salvar pessoa! Motivo: " + msgErro, capturedMessage.getDetail());
	}

	@Test
	public void testSalvarExistenteSuccess() throws Exception {
		Pessoa pessoa1 = new Pessoa(1L, "João", 45, SexoEnum.MASCULINO);
		pessoaBean.setPessoa(pessoa1);

		// Configuração do mock
		when(daoPessoa.listarTodos(Pessoa.class)).thenReturn(List.of(pessoa1));

		// Execução do método a ser testado
		String result = pessoaBean.salvar();

		// Verificações execução
		Mockito.verify(daoPessoa, Mockito.times(1)).atualizar(pessoa1);
		Mockito.verify(daoPessoa, Mockito.times(1)).listarTodos(Pessoa.class);
		Mockito.verifyNoMoreInteractions(daoPessoa);

		// Verificações resultado
		Assert.assertEquals("", result); // Verifica se o resultado do método é uma string vazia
		Assert.assertEquals(new Pessoa(), pessoaBean.getPessoa());
		Assert.assertEquals(1, pessoaBean.getPessoas().size());
		Assert.assertEquals(pessoa1.getNome(), pessoaBean.getPessoas().get(0).getNome());

		// Captura o argumento do método addMessage
		ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
		verify(facesContext).addMessage(any(), messageCaptor.capture());

		// Obtém a mensagem capturada
		FacesMessage capturedMessage = messageCaptor.getValue();

		// Verifica se a mensagem capturada é igual à mensagem esperada
		Assert.assertEquals(FacesMessage.SEVERITY_INFO, capturedMessage.getSeverity());
		Assert.assertEquals("Informação", capturedMessage.getSummary());
		Assert.assertEquals("Pessoa salvo com sucesso!", capturedMessage.getDetail());
	}

	@Test
	public void testSalvarExistenteError() throws Exception {
		Pessoa pessoa1 = new Pessoa(1L, "João", 45, SexoEnum.MASCULINO);
		pessoaBean.setPessoa(pessoa1);
		String msgErro = "Testando erro ao salvar pessoa";

		// Configuração do mock
		when(daoPessoa.listarTodos(Pessoa.class)).thenReturn(Collections.emptyList());
		Mockito.doThrow(new Exception(msgErro)).when(daoPessoa).atualizar(pessoa1);

		// Execução do método a ser testado
		String result = pessoaBean.salvar();

		// Verificações execução
		Mockito.verify(daoPessoa, Mockito.times(1)).atualizar(pessoa1);
		Mockito.verifyNoMoreInteractions(daoPessoa);

		// Verificações resultado
		Assert.assertEquals("", result); // Verifica se o resultado do método é uma string vazia
		Assert.assertEquals(pessoa1, pessoaBean.getPessoa());
		Assert.assertEquals(0, pessoaBean.getPessoas().size());

		// Captura o argumento do método addMessage
		ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
		verify(facesContext).addMessage(any(), messageCaptor.capture());

		// Obtém a mensagem capturada
		FacesMessage capturedMessage = messageCaptor.getValue();

		// Verifica se a mensagem capturada é igual à mensagem esperada
		Assert.assertEquals(FacesMessage.SEVERITY_ERROR, capturedMessage.getSeverity());
		Assert.assertEquals("Erro", capturedMessage.getSummary());
		Assert.assertEquals("Erro ao salvar pessoa! Motivo: " + msgErro, capturedMessage.getDetail());
	}

	@Test
	public void testEditarSuccess() throws Exception {
		Pessoa pessoa1 = new Pessoa(1L, "João", 45, SexoEnum.MASCULINO);

		// Execução do método a ser testado
		String result = pessoaBean.editar(pessoa1);

		// Verificações execução
		Mockito.verifyNoMoreInteractions(daoPessoa);

		// Verificações resultado
		Assert.assertEquals("", result); // Verifica se o resultado do método é uma string vazia
		Assert.assertEquals(pessoa1, pessoaBean.getPessoa());
	}

	@Test
	public void testLimparSuccess() throws Exception {
		Pessoa pessoa1 = new Pessoa(1L, "João", 45, SexoEnum.MASCULINO);
		pessoaBean.setPessoa(pessoa1);

		// Execução do método a ser testado
		String result = pessoaBean.limpar();

		// Verificações execução
		Mockito.verifyNoMoreInteractions(daoPessoa);

		// Verificações resultado
		Assert.assertEquals("", result); // Verifica se o resultado do método é uma string vazia
		Assert.assertEquals(new Pessoa(), pessoaBean.getPessoa());
	}

	@Test
	public void testGoToEnderecosSuccess() throws IOException {
		Long pessoaId = 1L;
		pessoaBean.goToEnderecos(pessoaId);

		// Verificando se o redirecionamento está sendo realizado corretamente
		verify(externalContext).redirect("endereco.xhtml?pessoaId=" + pessoaId);
	}

	@Test(expected = IOException.class)
	public void testGoToEnderecos_IOException() throws IOException {
		// Simulando uma IOException ao tentar redirecionar
		doThrow(IOException.class).when(externalContext).redirect(anyString());

		// Chama o método goToEnderecos
		pessoaBean.goToEnderecos(1L);
	}

	@Test
	public void teste() {
		PessoaBean pessoaBean = new PessoaBean();
		pessoaBean.excluirPorId(2L);
	}
}
