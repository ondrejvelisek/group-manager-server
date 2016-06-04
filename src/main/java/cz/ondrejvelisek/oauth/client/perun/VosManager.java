package cz.ondrejvelisek.oauth.client.perun;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.ondrejvelisek.oauth.client.model.Group;
import cz.ondrejvelisek.oauth.client.model.PerunException;
import cz.ondrejvelisek.oauth.client.model.Vo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class VosManager extends Manager {

    public VosManager(String token) {
        super(token);
    }

    public Vo getVoByShortName(String shortName) throws PerunException {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("shortName", shortName);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(get("getVoByShortName", params), Vo.class);
        } catch (IOException e) {
            throw new PerunException(e);
        }

    }

    @Override
    protected String getManager() {
        return "vosManager";
    }
}
