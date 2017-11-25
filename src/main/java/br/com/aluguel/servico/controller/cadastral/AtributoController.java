package br.com.aluguel.servico.controller.cadastral;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import br.com.aluguel.entity.cadastral.Atributo;
import br.com.aluguel.exceptions.InvalidRequestException;
import br.com.aluguel.json.bean.cadastral.AtributoProduto;
import br.com.aluguel.servico.controller.UtilController;
import br.com.aluguel.servico.security.SecurityUser;
import br.com.aluguel.servico.service.cadastral.AtributoService;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by thiago on 30/06/17.
 */
@RestController
@RequestMapping(value = "/api/atributo")
public class AtributoController extends UtilController {

    @Autowired
    private HttpSession session;

    @Autowired
    private AtributoService service;
    
    @ApiOperation(value = "Serviço responsável por atualizar o atributo do produto")
    @ApiImplicitParam(paramType = "header", name = AUTH_HEADER_NAME, value = "API Key")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Secured({ "ROLE_MANTER_ATRIBUTO" })
    public ResponseEntity<?> update(@RequestBody @Valid AtributoProduto model, @PathVariable Integer id,
                                    BindingResult result) {

        if (result.hasErrors()) {
            throw new InvalidRequestException("Validação do cadastro de atributo", result);
        }

        Atributo atributo = service.findById(id);

        if (atributo == null) {
            throw new NoResultException("Não existe atributo cadastrado com os dados inseridos.");
        }

        atributo = prepareAtributo(model, atributo);

        HashMap<String, Object> map = new HashMap<>();
        map.put("msg", "Atributo alterado com sucesso");
        map.put("id", atributo.getId());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @ApiOperation(value = "Buscar atributo pelo ID", response = Atributo.class, notes = "Retorna o atributo a partir do ID especificado")
    @ApiImplicitParam(paramType = "header", name = AUTH_HEADER_NAME, value = "API Key")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Atributo getById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @ApiOperation(value = "Deleta atributo pelo ID", response = Atributo.class, notes = "Exclui o atributo a partir do ID especificado")
    @ApiImplicitParam(paramType = "header", name = AUTH_HEADER_NAME, value = "API Key")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void remover(@PathVariable Integer id) {
        service.delete(id);
    }

    @ApiOperation(value = "Buscar atributos pelo ID do produto", response = Atributo.class, notes = "Retorna todos os atributos a partir do ID do produto especificado", responseContainer = "List")
    @ApiImplicitParam(paramType = "header", name = AUTH_HEADER_NAME, value = "API Key")
    @RequestMapping(value = "/produto/{idProduto}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Set<Atributo> getByIdProduto(@PathVariable Integer idProduto) {
        return new HashSet<Atributo>(service.findByProduto(idProduto));
    }

    private Atributo prepareAtributo(AtributoProduto model, Atributo atributo) {

        SecurityUser user = getAthenticatedUser(session);

        BeanUtils.copyProperties(model, atributo, getNullPropertyNames(model));

        atributo = service.save(atributo);
        return atributo;
    }
}