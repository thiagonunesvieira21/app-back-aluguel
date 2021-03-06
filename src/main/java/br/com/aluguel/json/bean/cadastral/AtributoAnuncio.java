package br.com.aluguel.json.bean.cadastral;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by thiago on 29/06/17.
 */
@ApiModel(value = "atributo")
public class AtributoAnuncio {

    private Integer id;

    @ApiModelProperty(required=true)
    @NotEmpty
    @NotNull
    @Size(max = 20)
    private String nome;


    @ApiModelProperty(required=true)
    @NotEmpty
    @NotNull
    @Size(max = 100)
    private String texto;

    @NotNull
    private Integer ordem;

    public AtributoAnuncio() {
    }

    public AtributoAnuncio(String nome, String texto, Integer ordem) {
        this.nome = nome;
        this.texto = texto;
        this.ordem = ordem;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }
}
