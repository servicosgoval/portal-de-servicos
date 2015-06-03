package br.gov.servicos.importador;

import br.gov.servicos.cms.Conteudo;
import br.gov.servicos.cms.ConteudoRepository;
import br.gov.servicos.cms.Markdown;
import br.gov.servicos.foundation.exceptions.ConteudoNaoEncontrado;
import br.gov.servicos.orgao.OrgaoRepository;
import br.gov.servicos.servico.Orgao;
import br.gov.servicos.servico.linhaDaVida.LinhaDaVida;
import br.gov.servicos.servico.linhaDaVida.LinhaDaVidaRepository;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

import static br.gov.servicos.foundation.IO.read;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ImportadorConteudo {

    Markdown markdown;
    LinhaDaVidaRepository linhaDaVidaRepository;
    OrgaoRepository orgaoRepository;
    ConteudoRepository conteudoRepository;

    @Autowired
    public ImportadorConteudo(Markdown markdown, LinhaDaVidaRepository linhaDaVidaRepository, OrgaoRepository orgaoRepository, ConteudoRepository conteudoRepository) {
        this.markdown = markdown;
        this.linhaDaVidaRepository = linhaDaVidaRepository;
        this.orgaoRepository = orgaoRepository;
        this.conteudoRepository = conteudoRepository;
    }

    public Iterable<Conteudo> importar() {
        List<Conteudo> conteudos = concat(concat(
                        orgaoRepository.findAll().stream().map(this::paraConteudo),
                        linhaDaVidaRepository.findAll().stream().map(this::paraConteudo)),
                asList("acessibilidade", "documento-de-arrecadacao-de-receitas-federais-darf", "perguntas-frequentes").stream().map(this::paraConteudo)
        ).collect(toList());

        return this.conteudoRepository.save(conteudos);
    }

    private Conteudo paraConteudo(String id) {
        String caminho = format("/conteudo/%s.md", id);
        return new Conteudo()
                .withId(id)
                .withTitulo(markdown.toHtml(new ClassPathResource(caminho)).getTitulo())
                .withTipoConteudo("conteudo")
                .withConteudo(conteudo(caminho));
    }

    private Conteudo paraConteudo(LinhaDaVida linhaDaVida) {
        return new Conteudo()
                .withId(linhaDaVida.getId())
                .withTipoConteudo("linha-da-vida")
                .withTitulo(linhaDaVida.getTitulo())
                .withConteudo(conteudo(format("/conteudo/linhas-da-vida/%s.md", linhaDaVida.getId())));
    }

    private Conteudo paraConteudo(Orgao orgao) {
        return new Conteudo()
                .withId(orgao.getId())
                .withTipoConteudo("orgao")
                .withTitulo(orgao.getNome())
                .withConteudo(conteudo(format("/conteudo/orgaos/%s.md", orgao.getId())));
    }

    @SneakyThrows(ConteudoNaoEncontrado.class)
    private String conteudo(String caminho) {
        Resource resource = new ClassPathResource(caminho);
        log.debug("Conteúdo {} encontrado em: {}", caminho, resource);
        return read(resource);
    }

}
