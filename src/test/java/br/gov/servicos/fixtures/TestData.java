package br.gov.servicos.fixtures;

import br.gov.servicos.cms.Conteudo;
import br.gov.servicos.cms.ConteudoHtml;
import br.gov.servicos.metricas.Opiniao;
import br.gov.servicos.v3.schema.Orgao;
import br.gov.servicos.v3.schema.Servico;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

public class TestData {

    public static final Servico SERVICO = new Servico()
            .withId("exemplo-de-servico")
            .withNome("Exemplo de serviço")
            .withDescricao("Descrição serviço");

    public static final Conteudo CONTEUDO_DE_SERVICO = Conteudo.fromServico(SERVICO);

    public static final ConteudoHtml CONTEUDO_HTML = new ConteudoHtml()
            .withId("pagina")
            .withNome("Título")
            .withHtml("<h1>Título</h1>\n\nConteúdo");

    public static final Conteudo CONTEUDO = new Conteudo()
            .withId("pagina-orgao")
            .withTipoConteudo("orgao")
            .withNome("Página órgão")
            .withConteudo("Descrição conteúdo");

    public static final Opiniao OPINIAO = new Opiniao()
            .withUrl("/servico/foo")
            .withQueryString("bar=baz")
            .withTimestamp(123L)
            .withConteudoEncontrado(true)
            .withMensagem("Otimo site");

    public static final List<Orgao> ORGAOS = unmodifiableList(asList(
            new Orgao().withId("arquivo-nacional-an"),
            new Orgao().withId("banco-central-do-brasil-bcb")
    ));

}
