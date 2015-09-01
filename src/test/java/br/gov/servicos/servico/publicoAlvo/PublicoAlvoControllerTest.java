package br.gov.servicos.servico.publicoAlvo;

import br.gov.servicos.busca.Buscador;
import br.gov.servicos.cms.Conteudo;
import com.github.slugify.Slugify;
import lombok.experimental.FieldDefaults;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static br.gov.servicos.fixtures.TestData.SERVICO;
import static br.gov.servicos.v3.schema.SegmentoDaSociedade.CIDADÃOS;
import static br.gov.servicos.v3.schema.SegmentoDaSociedade.EMPRESAS;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Optional.of;
import static lombok.AccessLevel.PRIVATE;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeValue;
import static org.springframework.test.web.ModelAndViewAssert.assertViewName;

@RunWith(MockitoJUnitRunner.class)
@FieldDefaults(level = PRIVATE)
public class PublicoAlvoControllerTest {

    @Mock(answer = Answers.RETURNS_SMART_NULLS)
    Buscador buscador;

    PublicoAlvoController publicosAlvo;

    @Before
    public void setUp() throws IOException {
        doReturn(asList(
                SERVICO.withNome("XXXX").withSegmentosDaSociedade(asList(CIDADÃOS, EMPRESAS)),
                SERVICO.withNome("AAAA").withSegmentosDaSociedade(asList(CIDADÃOS, EMPRESAS))
        )).when(buscador)
                .buscaServicosPor("segmentosDaSociedade", of("CIDADÃOS"));

        doReturn(asList(
                SERVICO.withNome("FFFF").withSegmentosDaSociedade(asList(CIDADÃOS, EMPRESAS)),
                SERVICO.withNome("AAAA").withSegmentosDaSociedade(asList(CIDADÃOS, EMPRESAS))
        )).when(buscador)
                .buscaServicosPor("segmentosDaSociedade", of("EMPRESAS"));

        publicosAlvo = new PublicoAlvoController(buscador, new Slugify());
    }

    @Test
    public void deveRedirecionarParaPaginaDePublicosAlvo() {
        assertViewName(publicosAlvo.publicoAlvo("cidadaos", null), "publico-alvo");
    }

    @Test
    public void deveRetornarOsServicosRelacionadosAoPublicoAlvo() {
        assertModelAttributeValue(publicosAlvo.publicoAlvo("cidadaos", null), "servicos",
                singletonList(Conteudo.fromServico(SERVICO.withNome("AAAA"))));
    }

    @Test
    public void deveRetornarOPublicoAlvoPesquisado() {
        assertModelAttributeValue(publicosAlvo.publicoAlvo("cidadaos", null), "publicoAlvo", CIDADÃOS);
    }

    @Test
    public void deveRetornarAsLetrasDisponiveis() {
        assertModelAttributeValue(publicosAlvo.publicoAlvo("cidadaos", null), "letras", asList('A', 'X'));
        assertModelAttributeValue(publicosAlvo.publicoAlvo("empresas", null), "letras", asList('A', 'F'));
    }

    @Test
    public void deveRetornarALetraAtiva() {
        assertModelAttributeValue(publicosAlvo.publicoAlvo("cidadaos", null), "letraAtiva", 'A');
        assertModelAttributeValue(publicosAlvo.publicoAlvo("cidadaos", 'x'), "letraAtiva", 'X');
    }

    @Test
    public void deveFiltrarPelaLetraInformada() {
        assertModelAttributeValue(publicosAlvo.publicoAlvo("cidadaos", 'X'), "servicos",
                singletonList(Conteudo.fromServico(SERVICO.withNome("XXXX"))));
    }

}