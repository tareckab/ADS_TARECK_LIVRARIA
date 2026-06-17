package com.upf.livraria.converter;

import com.upf.livraria.entity.Livraria;
import com.upf.livraria.facade.LivrariaFacade;
import jakarta.inject.Inject;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(value = "livrariaConverter", managed = true)
public class LivrariaConverter implements Converter<Livraria> {

    @Inject
    private LivrariaFacade livrariaFacade;

    @Override
    public Livraria getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            Integer id = Integer.valueOf(value);
            return livrariaFacade.buscarPorId(id);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Livraria value) {
        if (value == null || value.getId() == null) {
            return "";
        }
        return String.valueOf(value.getId());
    }
}
