package xyz.grafana.dyupspersistence.controller;

import xyz.grafana.dyupspersistence.model.UpstreamConfigWithBLOBs;
import xyz.grafana.dyupspersistence.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping
public class IndexController {
    @Autowired
    ConfigService configService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(ModelMap map){
        List<UpstreamConfigWithBLOBs> configList = configService.selectConfig();
        map.addAttribute("configList", configList);
        return "config";
    }

    @RequestMapping(value = "/configForm", method = RequestMethod.GET)
    public String configForm(ModelMap map, @RequestParam int id){
        UpstreamConfigWithBLOBs upstreamConfig = new UpstreamConfigWithBLOBs();

        if(id != 0){
            upstreamConfig = configService.selectById(id);
        }

        map.addAttribute("config", upstreamConfig);

        return "configForm";
    }
}
