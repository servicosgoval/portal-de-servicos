package br.gov.servicos.v3.schema;

import com.github.slugify.Slugify;
import lombok.SneakyThrows;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import java.util.stream.Stream;


@XmlType(name = "SegmentoDaSociedade")
@XmlEnum
public enum SegmentoDaSociedade {

    @XmlEnumValue("Cidad\u00e3os")
    CIDADÃOS("Cidad\u00e3os"),

    @XmlEnumValue("Empresas")
    EMPRESAS("Empresas"),

    @XmlEnumValue("\u00d3rg\u00e3os e entidades p\u00fablicas")
    ÓRGÃOS_E_ENTIDADES_PÚBLICAS("\u00d3rg\u00e3os e entidades p\u00fablicas"),

    @XmlEnumValue("Demais segmentos (ONGs, organiza\u00e7\u00f5es sociais, etc)")
    DEMAIS_SEGMENTOS_ONGS_ORGANIZAÇÕES_SOCIAIS_ETC("Demais segmentos (ONGs, organiza\u00e7\u00f5es sociais, etc)");

    private final String id;
    private final String value;

    @SneakyThrows
    SegmentoDaSociedade(String v) {
        id = new Slugify().slugify(v);
        value = v;
    }

    public static SegmentoDaSociedade findById(String v) {
        return Stream.of(values())
                .filter(c -> c.getId().equals(v))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(v));
    }

    public String getValue() {
        return value;
    }

    @SneakyThrows
    public String getId() {
        return id;
    }
}
